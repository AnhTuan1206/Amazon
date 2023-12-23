package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
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
    private String usertId ="";
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
        usertId = profileActivity.getId();
        if(usertId.equals(userCurrentID)){
            binding.tvCountFriend2.setText(listMyFriend.size() + " bạn bè");
            binding.tvName.setText(profileActivity.getName());
            loadImage(profileActivity.getImage());

        } else {
            binding.tvName.setText(profileActivity.getName());
            loadImage(profileActivity.getImage());
            binding.tvCountFriend2.setVisibility(View.GONE);
            binding.btnAddTin.setVisibility(View.GONE);
            binding.btnSettingPersonalPage.setVisibility(View.GONE);
            binding.layoutNoiSong.setEnabled(false);
            binding.layoutQueQuan.setEnabled(false);
            binding.layoutNoiLamViec.setEnabled(false);
            binding.layoutMoiQuanHe.setEnabled(false);
        }
    }

    private void getDataProfileNoiO(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(id.equals(userCurrentID)){
                            if(task.getResult().get(Constants.KEY_NOI_O) != null){
                                binding.tvNoiSong.setText(task.getResult().get(Constants.KEY_NOI_O).toString());
                            }
                            if(task.getResult().get(Constants.KEY_NOI_LAM_VIEC) != null){
                                binding.tvNoiLamViec.setText(task.getResult().get(Constants.KEY_NOI_LAM_VIEC).toString());
                            }
                            if(task.getResult().get(Constants.KEY_HOME_TOWN) != null){
                                binding.tvQueQuan.setText(task.getResult().get(Constants.KEY_HOME_TOWN).toString());
                            }
                        }
                    }
                });
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