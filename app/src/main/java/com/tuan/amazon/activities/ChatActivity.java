package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tuan.amazon.databinding.ActivityChatBinding;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private String userId ="";
    private String image = "";
    private String name = "";
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
//        getData();
    }

    private void getData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString(Constants.KEY_NAME, "");
        image = bundle.getString(Constants.KEY_USER_IMAGE, "");
    }

    public String getImage(){
        return image;
    }
    public String getName(){
        return name;
    }
}