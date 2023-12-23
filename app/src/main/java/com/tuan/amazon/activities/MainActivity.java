package com.tuan.amazon.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.adapters.MainAdapterViewPager2;
import com.tuan.amazon.databinding.ActivityMainBinding;
import com.tuan.amazon.utilities.Constants;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainAdapterViewPager2 mainAdapterViewPager2;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public static String userCurrentID;
    public static String userCurrentName;
    public static List<String> listMyFriend;

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
//        userCurrentName = firebaseUser.getDisplayName();
        listMyFriend = new ArrayList<>();
        getData();
    }

    private void getData(){
         firestore.collection(Constants.KEY_FRIEND)
                 .document(userCurrentID)
                 .collection(Constants.KEY_YOUR_FRIENDS)
                 .get()
                 .addOnCompleteListener(task -> {
                     if(task.isSuccessful()){
                         for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                             listMyFriend.add(queryDocumentSnapshot.getString(Constants.KEY_ID));
                         }
                     }
                 });
    }

}