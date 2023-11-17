package com.tuan.amazon.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.ActivityLoginBinding;
import com.tuan.amazon.utilities.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void events(){
        binding.btnLogin.setOnClickListener(v -> {
            isValidSignupDetails();
            signIn();
        });

        binding.tvCreatAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, CreatAccountActivity.class));
        });

        /*binding.tvLoginGoogle.setOnClickListener(view -> {
            signInWithGG();
        });*/
    }

    private void signIn(){
        String email = binding.etInputEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        loading(true);
                        sendUserToMainActivity();
                    }else {
                        loading(false);
                        showToask("Đăng nhập thất bại, hãy kiểm tra lại email hoặc mật khẩu");
                    }
                });

    }


    private void sendUserToMainActivity() {
        binding.progressBar.setVisibility(View.GONE);
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    private void signInWithGG(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }

                    }
                });

    }


    private void updateUi(FirebaseUser firebaseUser){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        Map<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, account.getDisplayName());
        user.put(Constants.KEY_EMAIL, account.getEmail());
        user.put(Constants.KEY_USER_ID, account.getId());
        user.put(Constants.KEY_USER_IMAGE, account.getPhotoUrl());

        firestore.collection(Constants.KEY_COLLECTION_USERS)
                .document(account.getId())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        loading(true);
                        sendUserToMainActivity();
                    }else {
                        loading(false);
                        showToask("Đăng nhập thất bại");
                    }
                });

    }

    private Boolean isValidSignupDetails(){
        if(binding.etInputEmail.getText().toString().trim().isEmpty()){
            showToask("Email không được để trống");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etInputEmail.getText().toString()).matches()){
            showToask("Hãy nhập đúng kiểu email");
            return false;
        }else if (binding.etPassword.getText().toString().trim().isEmpty()){
            showToask("Mật khẩu không được để trống");
            return false;
        }else
            return true;
    }

    private void showToask(String message){
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnLogin.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}