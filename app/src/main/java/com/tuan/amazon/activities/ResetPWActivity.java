package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ActivityResetPwactivityBinding;

public class ResetPWActivity extends AppCompatActivity {
    private ActivityResetPwactivityBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPwactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        eventClick();
    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void eventClick(){
        binding.btnResetPW.setOnClickListener(view -> {
            resetPW();
        });

        binding.tvBackLogin.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void resetPW() {
        if(isValidEmail()){
            String email = binding.etInputEmail.getText().toString().trim();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            showToask("Hãy vào email để thay đổi mật khẩu");
                        }else {
                            showToask("Thay đổi mật khẩu thất bại");
                        }
                    });
        }
    }

    private Boolean isValidEmail(){
        if(binding.etInputEmail.getText().toString().trim().isEmpty()){
            showToask("Email không được để trống");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.getText().toString()).matches()){
            showToask("Hãy nhập đúng kiểu email");
            return false;
        }else return true;
    }

    private void showToask(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}