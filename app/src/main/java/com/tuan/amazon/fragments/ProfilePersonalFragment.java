package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.databinding.FragmentProfilePersonalBinding;
import com.tuan.amazon.models.User;
import com.tuan.amazon.utilities.Constants;


public class ProfilePersonalFragment extends Fragment {
   
    private FragmentProfilePersonalBinding binding;
    private FirebaseFirestore firestore;
    private ProfileActivity profileActivity;
    private String currentId ="";
    private User user;
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
        getInitView();
        eventsClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
    }


    private void getInitView(){
        profileActivity = (ProfileActivity) getActivity();
        currentId = profileActivity.getCurrentUserId();
        user = profileActivity.getUser();
        if(currentId != null){
            binding.tvCountFriend2.setText(listMyFriend.size() + " bạn bè");
            firestore.collection(Constants.KEY_COLLECTION_USERS)
                    .document(currentId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            binding.tvName.setText(task.getResult().getString(Constants.KEY_NAME));
                            String image = task.getResult().getString(Constants.KEY_USER_IMAGE);
                            if(image!= null){
                                loadImage(image);
                            }
                        }
                    });
        } else if(user != null){
            binding.tvName.setText(user.getName());
            loadImage(user.getImage());
            binding.tvCountFriend2.setVisibility(View.GONE);
            binding.btnAddTin.setVisibility(View.GONE);
            binding.btnSettingPersonalPage.setVisibility(View.GONE);
            binding.layoutNoiSong.setEnabled(false);
            binding.layoutQueQuan.setEnabled(false);
            binding.layoutNoiLamViec.setEnabled(false);
            binding.layoutMoiQuanHe.setEnabled(false);
        }
    }

    private void loadImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void eventsClick() {
        binding.layoutNoiSong.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.noiSongHienTaiFragment);
        });

        binding.layoutNoiLamViec.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.workPlaceFragment);
        });

        binding.layoutQueQuan.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.homeTownFragment);
        });
        binding.tvSearchFriend.setVisibility(View.VISIBLE);
        binding.tvSearchFriend.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.friendFragment);
        });

        binding.btnAllFriend.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.friendActivity);
        });
    }

}