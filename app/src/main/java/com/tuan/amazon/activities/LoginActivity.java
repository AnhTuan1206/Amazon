package com.tuan.amazon.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        events();
    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

    }

    private void events(){
        binding.btnLogin.setOnClickListener(v -> {
            isValidSignupDetails();
            signIn();
        });

        binding.tvCreatAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateAccountActivity.class));
        });

        binding.tvForgotPW.setOnClickListener(view -> {
            startActivity(new Intent(this, ResetPWActivity.class));
        });
    }

    private void signIn(){
        String email = binding.etInputEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        sendUserToMainActivity();
                    }else {
                        showToast("Đăng nhập thất bại, hãy kiểm tra lại email hoặc mật khẩu");
                    }
                });

    }

    private void sendUserToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private Boolean isValidSignupDetails(){
        if(binding.etInputEmail.getText().toString().trim().isEmpty()){
            showToast("Email không được để trống");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.getText().toString()).matches()){
            showToast("Hãy nhập đúng kiểu email");
            return false;
        }else if (binding.etPassword.getText().toString().trim().isEmpty()){
            showToast("Mật khẩu không được để trống");
            return false;
        }else
            return true;
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

}