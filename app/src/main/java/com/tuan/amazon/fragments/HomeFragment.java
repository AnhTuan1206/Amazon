package com.tuan.amazon.fragments;



import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.firebase.firestore.FirebaseFirestore;

import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.databinding.FragmentHomeBinding;

import com.tuan.amazon.utilities.Constants;




public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore firestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        loadImageProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        eventsClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
    }

    private void loadImageProfile(){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userCurrentID)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String codeImageProfile = (String) task.getResult().get(Constants.KEY_USER_IMAGE);
                        byte[] bytes = Base64.decode(codeImageProfile, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        binding.imageProfile.setImageBitmap(bitmap);
                    }
                });
    }

    private void eventsClick(){
        binding.imageProfile.setOnClickListener(v -> {
            chuyenManHinh();
        });
    }

    private void chuyenManHinh(){
        Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
        intent.putExtra(Constants.KEY_CURRENT_USER_ID, userCurrentID);
        startActivity(intent);
    }
}