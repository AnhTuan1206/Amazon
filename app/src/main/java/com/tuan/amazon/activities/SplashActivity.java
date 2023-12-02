package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        start();
    }

    private void start(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                moveInLogin();
            }
        }, 2500);
    }

    private void moveInLogin(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}