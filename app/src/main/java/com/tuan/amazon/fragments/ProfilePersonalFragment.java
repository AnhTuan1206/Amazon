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
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;


public class ProfilePersonalFragment extends Fragment {
   
    private FragmentProfilePersonalBinding binding;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
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
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
    }

    private void getInitView(){
        ProfileActivity profileActivity = (ProfileActivity) getActivity();
        String userId = profileActivity.getId();
        if(userCurrentID.equals(userId)){
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
        getDataProfileNoiSong(userId);
        getDataProfileNoiLamViec(userId);
        getDataProfileQueQuan(userId);
    }

    private void getDataProfileNoiSong(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .collection(Constants.KEY_NOI_O)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().get(Constants.KEY_NOI_O) != null){
                        if(userCurrentID.equals(id)){
                            binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().get(Constants.KEY_NOI_O));
                            preferenceManager.putString(Constants.KEY_NOI_O, task.getResult().get(Constants.KEY_NOI_O).toString());
                            preferenceManager.putString(Constants.KEY_CONG_KHAI_NOI_O, task.getResult().get(Constants.KEY_CONG_KHAI_NOI_O).toString());
                        }else {
                            switch (task.getResult().get(Constants.KEY_CONG_KHAI_NOI_O).toString()){
                                case Constants.KEY_CDCK_CONG_KHAI:
                                    binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().get(Constants.KEY_NOI_O));
                                    break;
                                case Constants.KEY_CDCK_BAN_BE:
                                    if(listMyFriend.contains(id)){
                                        binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().get(Constants.KEY_NOI_O));
                                        break;
                                      }

                            }
                        }

                    }

                });
    }

    private void getDataProfileNoiLamViec(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .collection(Constants.KEY_NOI_LAM_VIEC)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()  && task.getResult().get(Constants.KEY_NOI_LAM_VIEC) != null){
                            if(userCurrentID.equals(id)){
                                binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().get(Constants.KEY_NOI_LAM_VIEC));
                                preferenceManager.putString(Constants.KEY_NOI_LAM_VIEC, task.getResult().get(Constants.KEY_NOI_LAM_VIEC).toString());
                                preferenceManager.putString(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC, task.getResult().get(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC).toString());
                            }else
                            {
                                switch (task.getResult().get(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC).toString()){
                                    case Constants.KEY_CDCK_CONG_KHAI:
                                        binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().get(Constants.KEY_NOI_LAM_VIEC));
                                        break;
                                    case Constants.KEY_CDCK_BAN_BE:
                                        if(listMyFriend.contains(id)){
                                            binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().get(Constants.KEY_NOI_LAM_VIEC));
                                            break;
                                        }
                                }
                            }

                    }
                });
    }

    private void getDataProfileQueQuan(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .collection(Constants.KEY_HOME_TOWN)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().get(Constants.KEY_HOME_TOWN) != null){
                            if(userCurrentID.equals(id)){
                                binding.tvQueQuan.setText("Quê quán tại " + task.getResult().get(Constants.KEY_HOME_TOWN));
                                preferenceManager.putString(Constants.KEY_HOME_TOWN, task.getResult().get(Constants.KEY_HOME_TOWN).toString());
                                preferenceManager.putString(Constants.KEY_CONG_KHAI_HOME_TOWN, task.getResult().get(Constants.KEY_CONG_KHAI_HOME_TOWN).toString());
                            }else
                            {
                                switch (task.getResult().get(Constants.KEY_CONG_KHAI_HOME_TOWN).toString()){
                                    case Constants.KEY_CDCK_CONG_KHAI:
                                        binding.tvQueQuan.setText("Quê quán tại " + task.getResult().get(Constants.KEY_HOME_TOWN));
                                        break;
                                    case Constants.KEY_CDCK_BAN_BE:
                                        if(listMyFriend.contains(id)){
                                            binding.tvQueQuan.setText("Quê quán tại " + task.getResult().get(Constants.KEY_HOME_TOWN));
                                            break;
                                        }
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