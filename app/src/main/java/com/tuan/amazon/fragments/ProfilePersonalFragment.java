package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.listMyFriend;
import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tuan.amazon.R;
import com.tuan.amazon.activities.ChatActivity;
import com.tuan.amazon.activities.ProfileActivity;
import com.tuan.amazon.adapters.PostAdapter;
import com.tuan.amazon.databinding.FragmentProfilePersonalBinding;
import com.tuan.amazon.listeners.HomeFlagmentListner;
import com.tuan.amazon.models.Post;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProfilePersonalFragment extends Fragment implements HomeFlagmentListner {
   
    private FragmentProfilePersonalBinding binding;
    private FirebaseFirestore firestore;
    private PreferenceManager preferenceManager;
    private ProfileActivity profileActivity = (ProfileActivity) getActivity();
    private String userId;
    private static String congKhaiNoiSong="", ckQueQuan="", ckNLV="", ckGT = "", ckNS = "";
    private PostAdapter adapter;
    private List<Post> list;
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
        getPost();
        eventsClick();
        return binding.getRoot();
    }


    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
        list = new ArrayList<>();
    }

    private void getInitView(){
        profileActivity  = (ProfileActivity) getActivity();
         userId = profileActivity.getId();
        if(userCurrentID.equals(userId)){
            binding.tvCountFriend2.setText(listMyFriend.size() + " bạn bè");
            binding.tvName.setText(profileActivity.getName());
            loadImage(profileActivity.getImage());
        } else {
            binding.tvName.setText(profileActivity.getName());
            loadImage(profileActivity.getImage());
            binding.tvCountFriend2.setVisibility(View.GONE);
//            binding.btnAddTin.setVisibility(View.GONE);
//            binding.btnSettingPersonalPage.setVisibility(View.GONE);
            binding.layoutNoiSong.setEnabled(false);
            binding.layoutQueQuan.setEnabled(false);
            binding.btnAllFriend.setVisibility(View.GONE);
            binding.layoutNoiLamViec.setEnabled(false);
            binding.layoutGioiTinh.setEnabled(false);
            binding.layoutYearOfBirth.setEnabled(false);
            binding.btnChat.setVisibility(View.VISIBLE);
            if(!listMyFriend.contains(userId)){
                binding.btnaddFriend.setVisibility(View.VISIBLE);
            }
        }
        getDataProfileNoiSong(userId);
        getDataProfileNoiLamViec(userId);
        getDataProfileQueQuan(userId);
        getDataGioiTinh(userId);
        getDataNamSinh(userId);

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
                            binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().getString(Constants.KEY_NOI_O));
                            congKhaiNoiSong = task.getResult().getString(Constants.KEY_CONG_KHAI_NOI_O);
                        }else {
                            switch (task.getResult().getString(Constants.KEY_CONG_KHAI_NOI_O)){
                                case Constants.KEY_CDCK_CONG_KHAI:
                                    binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().getString(Constants.KEY_NOI_O));
                                    break;
                                case Constants.KEY_CDCK_BAN_BE:
                                    if(listMyFriend.contains(id)){
                                        binding.tvNoiSong.setText("Hiện đang sống tại " + task.getResult().getString(Constants.KEY_NOI_O));
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
                                binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().getString(Constants.KEY_NOI_LAM_VIEC));
                                ckNLV = task.getResult().get(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC).toString();
                            }else
                            {
                                switch (task.getResult().getString(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC)){
                                    case Constants.KEY_CDCK_CONG_KHAI:
                                        binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().getString(Constants.KEY_NOI_LAM_VIEC));
                                        break;
                                    case Constants.KEY_CDCK_BAN_BE:
                                        if(listMyFriend.contains(id)){
                                            binding.tvNoiLamViec.setText("Hiện đang làm việc tại " + task.getResult().getString(Constants.KEY_NOI_LAM_VIEC));
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
                                binding.tvQueQuan.setText("Quê quán tại " + task.getResult().getString(Constants.KEY_HOME_TOWN));
                                ckQueQuan = task.getResult().getString(Constants.KEY_CONG_KHAI_HOME_TOWN);
                            }else
                            {
                                switch (task.getResult().getString(Constants.KEY_CONG_KHAI_HOME_TOWN)){
                                    case Constants.KEY_CDCK_CONG_KHAI:
                                        binding.tvQueQuan.setText("Quê quán tại " + task.getResult().getString(Constants.KEY_HOME_TOWN));
                                        break;
                                    case Constants.KEY_CDCK_BAN_BE:
                                        if(listMyFriend.contains(id)){
                                            binding.tvQueQuan.setText("Quê quán tại " + task.getResult().getString(Constants.KEY_HOME_TOWN));
                                            break;
                                        }
                                }
                            }
                    }
                });
    }
    private void getDataGioiTinh(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .collection(Constants.KEY_GIOI_TINH)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().get(Constants.KEY_GIOI_TINH) != null){
                        if(userCurrentID.equals(id)){
                            binding.tvGioiTinh.setText(task.getResult().getString(Constants.KEY_GIOI_TINH));
                            ckGT = task.getResult().getString(Constants.KEY_CONG_KHAI_GIOI_TINH);
                        }else {
                            switch (task.getResult().getString(Constants.KEY_CONG_KHAI_GIOI_TINH)){
                                case Constants.KEY_CDCK_CONG_KHAI:
                                    binding.tvGioiTinh.setText(task.getResult().getString(Constants.KEY_GIOI_TINH));
                                    break;
                                case Constants.KEY_CDCK_BAN_BE:
                                    if(listMyFriend.contains(id)){
                                        binding.tvGioiTinh.setText(task.getResult().getString(Constants.KEY_GIOI_TINH));
                                        break;
                                    }
                            }
                        }
                    }
                });
    }

    private void getDataNamSinh(String id){
        firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                .document(id)
                .collection(Constants.KEY_NAM_SINH)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult().getString(Constants.KEY_NAM_SINH) != null){
                        if(userCurrentID.equals(id)){
                            binding.tvYearOfBirth.setText(task.getResult().getString(Constants.KEY_NAM_SINH));
                            ckNS = task.getResult().getString(Constants.KEY_CONG_KHAI_NAM_SINH);
                        }else {
                            switch (task.getResult().getString(Constants.KEY_CONG_KHAI_NAM_SINH)){
                                case Constants.KEY_CDCK_CONG_KHAI:
                                    binding.tvYearOfBirth.setText(task.getResult().getString(Constants.KEY_NAM_SINH));
                                    break;
                                case Constants.KEY_CDCK_BAN_BE:
                                    if(listMyFriend.contains(id)){
                                        binding.tvYearOfBirth.setText(task.getResult().getString(Constants.KEY_NAM_SINH));
                                        break;
                                    }
                            }
                        }
                    }
                });
    }

    private void getPost(){
        firestore.collection(Constants.KEY_POSTS)
                .whereEqualTo(Constants.KEY_CREATOR_ID, userId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            Post post = new Post();
                            post.setPostId(queryDocumentSnapshot.getId());
                            post.setCountLike(queryDocumentSnapshot.getLong(Constants.KEY_COUNT_LIKE).intValue());
                            post.setCountComment(queryDocumentSnapshot.getLong(Constants.KEY_COUNT_COMMENT).intValue());
                            post.setImagePost(queryDocumentSnapshot.getString(Constants.KEY_IMAGE_POST));
                            post.setCreatorId(userId);
                            post.setCaption(queryDocumentSnapshot.getString(Constants.KEY_CAPTION));
                            post.setCreatorName(profileActivity.getName());
                            post.setImgCreator(profileActivity.getImage());
                            boolean check = queryDocumentSnapshot.contains(userCurrentID);
                            post.setLike(check);
                            post.setDateObject(queryDocumentSnapshot.getDate(Constants.KEY_TIME_CREATE_POST));
                            post.setDateTime(getReadableDataTime(queryDocumentSnapshot.getDate(Constants.KEY_TIME_CREATE_POST)));
                            list.add(post);
                        }
                    }
                    if (list.size() > 0) {
                        adapter = new PostAdapter(list, this);
                        binding.recyclerListPost.setAdapter(adapter);
                    }
                });
    }

    private String getReadableDataTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }


    private void loadImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void gotoChat(){
        preferenceManager.putBoolean(Constants.KEY_CHECK_CHAT_FROM_PP, true);
        profileActivity = (ProfileActivity) getActivity();
        Intent intent = new Intent(getActivity().getApplicationContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_NAME, profileActivity.getName());
        bundle.putString(Constants.KEY_USER_IMAGE, profileActivity.getImage());
        bundle.putString(Constants.KEY_USER_ID, profileActivity.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void eventsClick() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.btnChat.setOnClickListener(view -> {
            gotoChat();
        });
        binding.layoutNoiSong.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String noiO="";
            if(!binding.tvNoiSong.getText().toString().equals("Tỉnh/Thành phố hiện tại")){
                noiO = binding.tvNoiSong.getText().toString().substring(19);
            }
            bundle.putString(Constants.KEY_NOI_O, noiO);
            bundle.putString(Constants.KEY_CONG_KHAI_NOI_O, congKhaiNoiSong);
            Navigation.findNavController(v).navigate(R.id.noiSongHienTaiFragment, bundle);
        });

        binding.layoutNoiLamViec.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String noiLamViec="";
            if(!binding.tvNoiLamViec.getText().toString().equals("Nơi làm việc")){
                noiLamViec = binding.tvNoiLamViec.getText().toString().substring(23);
            }
            bundle.putString(Constants.KEY_NOI_LAM_VIEC, noiLamViec);
            bundle.putString(Constants.KEY_CONG_KHAI_NOI_LAM_VIEC, ckNLV);
            Navigation.findNavController(v).navigate(R.id.workPlaceFragment, bundle);
        });

        binding.layoutQueQuan.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String queQuan="";
            if(!binding.tvQueQuan.getText().toString().equals("Quê quán")){
                queQuan = binding.tvQueQuan.getText().toString().substring(13);
            }
            bundle.putString(Constants.KEY_HOME_TOWN, queQuan);
            bundle.putString(Constants.KEY_CONG_KHAI_HOME_TOWN, ckQueQuan);
            Navigation.findNavController(v).navigate(R.id.homeTownFragment, bundle);
        });
        binding.tvSearchFriend.setVisibility(View.VISIBLE);
        binding.tvSearchFriend.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.friendFragment);
        });

        binding.btnAllFriend.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.friendActivity);
        });
        binding.layoutGioiTinh.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            String gioiTinh="";
            if(!binding.tvGioiTinh.getText().toString().equals("Giới tính")){
                gioiTinh = binding.tvGioiTinh.getText().toString();
            }
            bundle.putString(Constants.KEY_GIOI_TINH, gioiTinh);
            bundle.putString(Constants.KEY_CONG_KHAI_GIOI_TINH, ckGT);
            Navigation.findNavController(view).navigate(R.id.gioiTinhFragment, bundle);
        });
        binding.layoutYearOfBirth.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            String namSinh = null;
            if(!binding.tvYearOfBirth.getText().toString().equals("Năm sinh")){
                namSinh = binding.tvYearOfBirth.getText().toString();
            }
            bundle.putString(Constants.KEY_NAM_SINH, namSinh);
            bundle.putString(Constants.KEY_CONG_KHAI_NAM_SINH, ckNS);
            Navigation.findNavController(view).navigate(R.id.birthTimeFragment, bundle);
        });

        binding.btnCopyId.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("simple text", userId);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity().getApplicationContext(), "Đã copy id thành công", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void like(Post post) {
        Map map = new HashMap();
        if(post.getLike()){
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int countLike =  task.getResult().getLong(Constants.KEY_COUNT_LIKE).intValue() + 1;
                            firestore.collection(Constants.KEY_POSTS)
                                    .document(post.getPostId())
                                    .update(Constants.KEY_COUNT_LIKE, countLike);
                        }
                    });
            map.put(userCurrentID,userCurrentID);
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .update(map);
        }
        else {
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int countLike =  task.getResult().getLong(Constants.KEY_COUNT_LIKE).intValue() - 1;
                            firestore.collection(Constants.KEY_POSTS)
                                    .document(post.getPostId())
                                    .update(Constants.KEY_COUNT_LIKE, countLike);
                        }
                    });
            map.put(userCurrentID, FieldValue.delete());
            firestore.collection(Constants.KEY_POSTS)
                    .document(post.getPostId())
                    .update(map);
        }
    }

    @Override
    public void comment(Post post) {
        BottomSheetDialogComment bottomSheetDialogComment = new BottomSheetDialogComment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID_POST,post.getPostId());
        bottomSheetDialogComment.setArguments(bundle);
        bottomSheetDialogComment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogComment.getTag());
    }

    @Override
    public void share(Post post) {
        SharePostFragment sharePostFragment = new SharePostFragment();
        sharePostFragment.show(getActivity().getSupportFragmentManager(), sharePostFragment.getTag());
    }
}