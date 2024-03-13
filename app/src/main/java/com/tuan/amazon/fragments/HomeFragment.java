package com.tuan.amazon.fragments;



import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.adapters.PostAdapter;
import com.tuan.amazon.databinding.FragmentHomeBinding;

import com.tuan.amazon.listeners.HomeFlagmentListner;
import com.tuan.amazon.models.Post;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment implements HomeFlagmentListner {

    private FragmentHomeBinding binding;
    private FirebaseFirestore firestore;
    private String image;
    private String name;
    private PreferenceManager preferenceManager;
    private PostAdapter adapter;
    private List<Post> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        init();
        loadImageProfile();
        eventsClick();
        listenPost();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        list = new ArrayList<>();
        adapter = new PostAdapter(list, this);
        binding.recyclerListPost.setAdapter(adapter);
    }
    private void listenPost(){
            firestore.collection(Constants.KEY_POSTS)
                    .addSnapshotListener(listenerPost);
    }
    private final EventListener<QuerySnapshot> listenerPost = (value, error) -> {
        if(error != null){
            return;
        } if(value != null){
            int count = list.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    Post post = new Post();
                    post.setPostId(documentChange.getDocument().getId());
                    post.setCreatorName(documentChange.getDocument().getString(Constants.KEY_NAME));
                    post.setImgCreator(documentChange.getDocument().getString(Constants.KEY_USER_IMAGE));
                    post.setCreatorId(documentChange.getDocument().getString(Constants.KEY_CREATOR_ID));
                    post.setCaption(documentChange.getDocument().getString(Constants.KEY_CAPTION));
                    post.setCountComment(documentChange.getDocument().getLong(Constants.KEY_COUNT_COMMENT).intValue());
                    post.setCountLike(documentChange.getDocument().getLong(Constants.KEY_COUNT_LIKE).intValue());
                    post.setImagePost(documentChange.getDocument().getString(Constants.KEY_IMAGE_POST));
                    Boolean check = documentChange.getDocument().contains(userCurrentID);
                    if(check){
                        post.setLike(true);
                    }else post.setLike(false);
                    post.setDateObject(documentChange.getDocument().getDate(Constants.KEY_TIME_CREATE_POST));
                    post.setDateTime(getReadableDataTime(documentChange.getDocument().getDate(Constants.KEY_TIME_CREATE_POST)));
                    list.add(post);
                    list.sort((obj1, obj2) -> obj1.getDateObject().compareTo(obj2.getDateObject()));
                    if(count == 0){
                        adapter.notifyDataSetChanged();
                    }else {
                        adapter.notifyItemRangeRemoved(list.size(), list.size());
                        binding.recyclerListPost.smoothScrollToPosition(list.size() -1);
                    }
                }
            }
        }
    };

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void loadImageProfile(){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userCurrentID)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        name = (String) task.getResult().get(Constants.KEY_NAME);
                        image = (String) task.getResult().get(Constants.KEY_USER_IMAGE);
                        preferenceManager.putString(Constants.KEY_USER_IMAGE, image);
                        preferenceManager.putString(Constants.KEY_NAME, name);
                        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }
                });
    }

    private void eventsClick(){
        binding.imageProfile.setOnClickListener(v -> {
            chuyenManHinh();
        });
    }

    private void chuyenManHinh(){
        Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, userCurrentID);
        bundle.putString(Constants.KEY_NAME, name);
        bundle.putString(Constants.KEY_USER_IMAGE, image);
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);
    }

    @Override
    public void like(Post post) {
        Map map = new HashMap();
        if(post.getLike()){
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                           int countLike =  task.getResult().getLong(Constants.KEY_COUNT_LIKE).intValue() + 1;
                            firestore.collection(Constants.KEY_POSTS)
                                    .document(post.getPostId())
                                    .update(Constants.KEY_COUNT_LIKE, countLike);
                        }
                    });
            map.put(userCurrentID,userCurrentID);
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .update(map);
        }
        else {
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int countLike =  task.getResult().getLong(Constants.KEY_COUNT_LIKE).intValue() - 1;
                            firestore.collection(Constants.KEY_POSTS)
                                    .document(post.getPostId())
                                    .update(Constants.KEY_COUNT_LIKE, countLike);
                        }
                    });
            map.put(userCurrentID, FieldValue.delete());
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .update(map);
        }
    }

    @Override
    public void comment(Post post) {
        BottomSheetDialogComment bottomSheetDialogComment = new BottomSheetDialogComment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID_POST,post.getPostId());
        bottomSheetDialogComment.setArguments(bundle);
        bottomSheetDialogComment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogComment.getTag());
    }
}