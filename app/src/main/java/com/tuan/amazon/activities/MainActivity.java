package com.tuan.amazon.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.adapters.MainAdapterViewPager2;
import com.tuan.amazon.databinding.ActivityMainBinding;
import com.tuan.amazon.utilities.Constants;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainAdapterViewPager2 mainAdapterViewPager2;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public static String userCurrentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        mainAdapterViewPager2 = new MainAdapterViewPager2(this);
        binding.viewPager2.setAdapter(mainAdapterViewPager2);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setIcon(R.drawable.ic_home);
                    break;
                case 1:
                    tab.setIcon(R.drawable.ic_friend);
                    break;
                case 2:
                    tab.setIcon(R.drawable.ic_other);
            }
        }).attach();

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userCurrentID = firebaseUser.getUid();

    }


}