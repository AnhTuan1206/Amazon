package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.os.Bundle;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ActivityProfileBinding;
import com.tuan.amazon.utilities.Constants;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private String userId ="";
    private String image = "";
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.KEY_USER_PROFILE);
        userId = bundle.getString(Constants.KEY_USER_ID);
        image = bundle.getString(Constants.KEY_USER_IMAGE);
        name = bundle.getString(Constants.KEY_NAME);


    }


    public String getId(){
        return userId;
    }
    public String getImage(){
        return image;
    }
    public String getName(){
        return name;
    }

}