package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.adapters.CommentAdapter;
import com.tuan.amazon.databinding.BottomsheetCommentBinding;
import com.tuan.amazon.models.Comment;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomSheetDialogComment extends BottomSheetDialogFragment implements TextWatcher {

    private BottomsheetCommentBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;
    private String idPost;
    private List<Comment> list;
    private CommentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetCommentBinding.inflate(inflater, container, false);
        init();
        eventClick();
        listenComment();
        return binding.getRoot();
    }

    private void init(){
        idPost = getArguments().getString(Constants.KEY_ID_POST);
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        list = new ArrayList<>();
        adapter = new CommentAdapter(list);
        binding.recyclerComment.setAdapter(adapter);
        binding.etComment.addTextChangedListener(this);
    }

    private void eventClick(){
        binding.btnBack.setOnClickListener(view -> {

        });
        binding.btnSend.setOnClickListener(view -> {
            Comment();
        });
    }
    private void Comment(){
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_ID_POST, idPost);
        map.put(Constants.KEY_COMMENT, binding.etComment.getText().toString().trim());
        map.put(Constants.KEY_USER_ID, userCurrentID);
        map.put(Constants.KEY_USER_IMAGE, preferenceManager.getString(Constants.KEY_USER_IMAGE));
        map.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        map.put(Constants.KEY_TIME_SEND, new Date());
        firestore.collection(Constants.KEY_COMMENT)
                .add(map);
        firestore.collection(Constants.KEY_POSTS)
                        .document(idPost).get()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                int count = task.getResult().getDouble(Constants.KEY_COUNT_COMMENT).intValue();
                                firestore.collection(Constants.KEY_POSTS)
                                        .document(idPost)
                                        .update(Constants.KEY_COUNT_COMMENT, count + 1);
                            }
                        });
        binding.etComment.setText(null);
    }

    private void listenComment(){
        firestore.collection(Constants.KEY_COMMENT)
                .addSnapshotListener(listenComment);
    }

    private final EventListener<QuerySnapshot> listenComment = (value, error) -> {
        if(error != null) {
            return;
        }if(value != null){
            int count = list.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Comment comment = new Comment();
                    comment.setId(documentChange.getDocument().getId());
                    comment.setComment(documentChange.getDocument().getString(Constants.KEY_COMMENT));
                    Log.d("com.tuan.amazon.test", documentChange.getDocument().getString(Constants.KEY_COMMENT));
                    comment.setIdUser(documentChange.getDocument().getString(Constants.KEY_USER_ID));
                    comment.setName(documentChange.getDocument().getString(Constants.KEY_NAME));
                    comment.setImgAvatar(documentChange.getDocument().getString(Constants.KEY_USER_IMAGE));
                    comment.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND));
                    comment.setDateTime(getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND)));
//                    firestore.collection(Constants.KEY_USER_PROFILE)
//                            .document(documentChange.getDocument().getString(Constants.KEY_USER_ID))
//                            .get()
//                            .addOnCompleteListener(task -> {
//                                if(task.isSuccessful()){
//                                    comment.setName(task.getResult().getString(Constants.KEY_NAME));
//                                    comment.setImgAvatar(task.getResult().getString(Constants.KEY_USER_IMAGE));
//                                }
//                            });
                    list.add(comment);
                    list.sort((obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));
                    if(count == 0){
                        adapter.notifyDataSetChanged();
                    }else {
                        adapter.notifyItemRangeRemoved(list.size(), list.size());
                        binding.recyclerComment.smoothScrollToPosition(list.size() -1);
                    }
                }
            }
        }
    };

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(binding.etComment.getText().toString().isEmpty()){
            binding.btnSend.setVisibility(View.GONE);
        }
        else binding.btnSend.setVisibility(View.VISIBLE);
    }
}
