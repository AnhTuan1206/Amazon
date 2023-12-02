package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.tuan.amazon.databinding.ActivityProfileBinding;
import com.tuan.amazon.fragments.ProfilePersonalFragment;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


}