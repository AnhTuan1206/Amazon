package com.tuan.amazon.fragments;


import static com.tuan.amazon.activities.MainActivity.userID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.adapters.AddFriendAdapter;
import com.tuan.amazon.databinding.FragmentFriendBinding;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment {

    private FragmentFriendBinding binding;
    private FirebaseFirestore firestore;
    private String userCurrent;
    private List<User> list;
    private AddFriendAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        getUser();
//        setView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void init(){
        userCurrent = userID;
        list = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
    }
    private void getUser(){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(userCurrent.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            User user = new User();
                            user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                            user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                            user.setId(queryDocumentSnapshot.getId());
                            list.add(user);
                        }
                        if(list.size() > 0){
                            adapter = new AddFriendAdapter(list);
                            binding.recyclerListFriendFragment.setAdapter(adapter);
                        }
                    }
                });
    }

//    private void setView(){
//        if(list.size() > 0){
//            adapter = new AddFriendAdapter(list);
//            binding.recyclerListFriendFragment.setAdapter(adapter);
//        }
//    }
}