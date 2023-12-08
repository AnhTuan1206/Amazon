package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import com.tuan.amazon.databinding.ActivityProfileBinding;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private String userId ="";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        Intent intent = getIntent();
        userId = intent.getStringExtra(Constants.KEY_CURRENT_USER_ID);
        user = (User) intent.getSerializableExtra(Constants.KEY_USER_PROFILE);
    }

    public String getCurrentUserId(){
        return userId;
    }

    public User getUser(){
        return user;
    }


}