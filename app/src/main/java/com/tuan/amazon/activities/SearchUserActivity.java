package com.tuan.amazon.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.adapters.UserAdapter;
import com.tuan.amazon.databinding.ActivitySearchUserBinding;
import com.tuan.amazon.listeners.SearchUserListener;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchUserActivity extends AppCompatActivity implements SearchUserListener {
    private ActivitySearchUserBinding binding;
    private FirebaseFirestore firestore;
    private static List<User> list;
    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        searchView();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter = new UserAdapter(list,6, null, null, null, null, null, this);
        binding.recyclerSearch.setAdapter(adapter);
    }

    private void searchView(){
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                filterList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void filterList(String s){
        list.clear();
       firestore.collection(Constants.KEY_COLLECTION_USERS)
//               .orderBy(Constants.KEY_NAME).startAt(s.toUpperCase()).endAt(s.toUpperCase()+'\uf8ff')
               .whereEqualTo(Constants.KEY_USER_ID, s)
               .get()
               .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                           User user = new User();
                           user.setId(queryDocumentSnapshot.getString(Constants.KEY_USER_ID));
                           user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                           user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                           list.add(user);
                       }
                   }
                   if (list.size()>0){
                       adapter.notifyDataSetChanged();
                   }
               });
    }

    @Override
    public void goToProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_USER_ID, user.getId());
        bundle.putString(Constants.KEY_USER_IMAGE, user.getImage());
        bundle.putString(Constants.KEY_NAME, user.getName());
        intent.putExtra(Constants.KEY_USER_PROFILE, bundle);
        startActivity(intent);
    }
}