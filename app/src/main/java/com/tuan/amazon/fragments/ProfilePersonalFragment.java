package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentProfilePersonalBinding;
import com.tuan.amazon.utilities.Constants;


public class ProfilePersonalFragment extends Fragment {
   
    private FragmentProfilePersonalBinding binding;
    private FirebaseFirestore firestore;

    public ProfilePersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfilePersonalBinding.inflate(inflater, null, false);
        getImageProfile();
        eventsClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
    }

    private void getImageProfile(){
        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(userCurrentID)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String image = task.getResult().getString(Constants.KEY_USER_IMAGE);
                        if(image!= null){
                            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            binding.imageProfile.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    private void eventsClick(){
        binding.layoutNoiSong.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.noiSongHienTaiFragment);
        });

        binding.layoutNoiLamViec.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.workPlaceFragment);
        });

        binding.layoutQueQuan.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.homeTownFragment);
        });
    }
}