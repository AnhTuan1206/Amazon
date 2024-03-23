package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.adapters.CommentAdapter;
import com.tuan.amazon.databinding.BottomsheetCommentBinding;
import com.tuan.amazon.databinding.LayoutEditCommentBinding;
import com.tuan.amazon.listeners.CommentListener;
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

public class BottomSheetDialogComment extends BottomSheetDialogFragment implements TextWatcher, CommentListener {

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
        adapter = new CommentAdapter(list, this);
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
                    comment.setIdPost(documentChange.getDocument().getString(Constants.KEY_ID_POST));
                    comment.setIdUser(documentChange.getDocument().getString(Constants.KEY_USER_ID));
                    comment.setName(documentChange.getDocument().getString(Constants.KEY_NAME));
                    comment.setImgAvatar(documentChange.getDocument().getString(Constants.KEY_USER_IMAGE));
                    comment.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND));
                    comment.setDateTime(getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIME_SEND)));
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

    @Override
    public void EditComment(Comment comment) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_edit_comment);
        EditText et = dialog.findViewById(R.id.etEdit);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        et.setText(comment.getComment());

        btnSave.setOnClickListener(view -> {
            int i = list.indexOf(comment);
            list.remove(comment);
            comment.setComment(et.getText().toString());
            list.add(i,comment);
            firestore.collection(Constants.KEY_COMMENT)
                    .document(comment.getId())
                    .update(Constants.KEY_COMMENT, et.getText().toString());
            adapter.notifyDataSetChanged();
            dialog.cancel();
        });

        btnCancel.setOnClickListener(view -> {
            dialog.cancel();
        });
        dialog.show();
    }


    @Override
    public void DeleteComment(Comment comment) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Thông báo");
        alertDialog.setIcon(R.drawable.ic_warning);
        alertDialog.setMessage("Bạn có chắc muốn xoá bình luận này không ?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                list.remove(comment);
                adapter.notifyDataSetChanged();
                firestore.collection(Constants.KEY_COMMENT)
                        .document(comment.getId())
                        .delete();
                firestore.collection(Constants.KEY_POSTS)
                        .document(comment.getIdPost())
                        .get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                int count = task.getResult().getDouble(Constants.KEY_COUNT_COMMENT).intValue();
                                firestore.collection(Constants.KEY_POSTS)
                                        .document(comment.getIdPost())
                                        .update(Constants.KEY_COUNT_COMMENT, count - 1);
                            }
                        });
            }
        });
        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Override
    public void goToProfile(String idUser, String img, String name) {
        Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, idUser);
        bundle.putString(Constants.KEY_NAME, name);
        bundle.putString(Constants.KEY_USER_IMAGE, img);
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);
    }


}
