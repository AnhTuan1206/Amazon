package com.tuan.amazon.activities;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentName;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.adapters.FriendAdapter;
import com.tuan.amazon.databinding.ActivityFriendBinding;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    private ActivityFriendBinding binding;
    private List<User> list;
    private FriendAdapter adapter;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        init();
    }

    private void initView(){
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(userCurrentName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        if(listMyFriend != null){
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            int count = 0;
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(listMyFriend.contains(queryDocumentSnapshot.getId()) && count < listMyFriend.size()){
                                    User user = new User();
                                    user.setName(queryDocumentSnapshot.getString(Constants.KEY_NAME));
                                    user.setImage(queryDocumentSnapshot.getString(Constants.KEY_USER_IMAGE));
                                    list.add(user);
                                    count++;
                                }
                            }
                        }
                        if(list.size() > 0){
                            adapter = new FriendAdapter(list);
                            binding.recyclerFriend.setAdapter(adapter);
                        }
                    });
        }
    }
}