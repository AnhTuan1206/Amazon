package com.tuan.amazon.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.databinding.ActivityCreatAccountBinding;
import com.tuan.amazon.utilities.Constants;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CreatAccountActivity extends AppCompatActivity {

    private ActivityCreatAccountBinding binding;
    private FirebaseAuth firebaseAuth;
    private String endcodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        events();
    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void events(){
        binding.layoutImageProfile.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImageProfile.launch(intent);
        });
        binding.btnCreatAccount.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etInputEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String encodeImage = endcodedImage;
            if(!isValidSignupDetails())
            {
                return;
            }
            creatAccount(name, email, password, encodeImage);
        });

        binding.tvBackLogin.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void creatAccount(final String name, final String email, String password, String endcodedImage){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            UserProfileChangeRequest.Builder requset =new UserProfileChangeRequest.Builder();
                            requset.setDisplayName(name);
                            firebaseUser.updateProfile(requset.build());
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                showToask("Hãy vô email để xác nhận sau đó quay lại đăng nhập");
                                            }
                                        }
                                    });
                            uploadUser(firebaseUser, name, email, endcodedImage);
                        }else {
                            showToask("Tài khoản email này đã tồn tại");
//                            loading(false);
                        }
                    }
                });
    }

    private ActivityResultLauncher<Intent> pickImageProfile = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageProfileUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageProfileUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.tvAddImageProfile.setVisibility(View.GONE);
                            endcodedImage = encodeImageProfile(bitmap);

                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private String encodeImageProfile(Bitmap bitmap){
        int previewWith = 150;
        int previewHeight = bitmap.getHeight()*previewWith / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWith, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void uploadUser(FirebaseUser firebaseUser, String name, String email, String endcodedImage){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, name);
        user.put(Constants.KEY_EMAIL, email);
        user.put(Constants.KEY_USER_ID, firebaseUser.getUid());
        user.put(Constants.KEY_USER_IMAGE,endcodedImage);

        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(firebaseUser.getUid())
                .set(user)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(e -> {
//                    loading(false);
                    showToask(e.getMessage());
                });
    }

//    private void loading(Boolean isLoading){
//        if(isLoading){
//            binding.btnCreatAccount.setVisibility(View.INVISIBLE);
//            binding.progressBar.setVisibility(View.VISIBLE);
//        }else {
//            binding.btnCreatAccount.setVisibility(View.VISIBLE);
//            binding.progressBar.setVisibility(View.INVISIBLE);
//        }
//    }

    private Boolean isValidSignupDetails() {
        if(endcodedImage == null){
            showToask("Hãy chọn ảnh đại diện");
            return false;
        }else if(binding.etName.getText().toString().trim().isEmpty()) {
            showToask("Tên không được để trống");
            return false;
        } else if (binding.etInputEmail.getText().toString().trim().isEmpty()) {
            showToask("Email không được để trống");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.getText().toString()).matches()) {
            showToask("Hãy nhập đúng kiểu email");
            return false;
        } else if (binding.etPassword.getText().toString().trim().isEmpty()) {
            showToask("Mật khẩu không được để trống");
            return false;
        } else if (binding.etConfirmPassword.getText().toString().trim().isEmpty()) {
            showToask("Xác nhận mật khẩu không đươc để trống");
            return false;
        } else if (!binding.etPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString())) {
            showToask("Xác nhận mật khẩu không giống với mật khẩu");
            return false;
        } else
            return true;
    }


    private void showToask(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }
}