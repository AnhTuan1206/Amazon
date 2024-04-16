package com.tuan.amazon.fragments;


import static android.app.Activity.RESULT_OK;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentBaiVietBinding;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BaiVietFragment extends Fragment implements TextWatcher {
    private FragmentBaiVietBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore firestore;
    private String endcodedImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBaiVietBinding.inflate(inflater, null, false);
        eventsClick();
        initView();
        return binding.getRoot();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getContext());
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView(){
       loadImage(preferenceManager.getString(Constants.KEY_USER_IMAGE));
       binding.tvName.setText(preferenceManager.getString(Constants.KEY_NAME));
       binding.etBaiViet.addTextChangedListener(this);
    }

    private void loadImage(String image){
        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void eventsClick(){
        binding.layoutImageAndVideo.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.btnDangBai.setOnClickListener(view -> {
            dangBai();
        });
    }

    private void dangBai(){
        Map<String,Object> map = new HashMap<>();
        map.put(Constants.KEY_CREATOR_ID, userCurrentID);
        map.put(Constants.KEY_NAME, preferenceManager.getString(Constants.KEY_NAME));
        map.put(Constants.KEY_USER_IMAGE,preferenceManager.getString(Constants.KEY_USER_IMAGE));
        map.put(Constants.KEY_CAPTION, binding.etBaiViet.getText().toString());
        map.put(Constants.KEY_COUNT_LIKE,0);
        map.put(Constants.KEY_COUNT_COMMENT, 0);
        if(binding.imgBaiViet.getDrawable() == null){
            map.put(Constants.KEY_IMAGE_POST, "null");
        }else {
            map.put(Constants.KEY_IMAGE_POST,endcodedImage);
        }
        map.put(Constants.KEY_TIME_CREATE_POST, new Date());
        firestore.collection(Constants.KEY_POSTS).add(map);

    }

    private String encodeImageCaption(Bitmap bitmap){
        int previewWith = 150;
        int previewHeight = bitmap.getHeight()*previewWith / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imgBaiViet.setImageBitmap(bitmap);
                            endcodedImage = encodeImageCaption(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!binding.etBaiViet.getText().toString().isEmpty()){
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_blue, null);
            binding.btnDangBai.setBackground(drawable);
            binding.btnDangBai.setEnabled(true);
        }
    }
}