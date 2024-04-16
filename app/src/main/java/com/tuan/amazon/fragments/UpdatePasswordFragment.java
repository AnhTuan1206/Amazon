package com.tuan.amazon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuan.amazon.databinding.FragmentUpdatePasswordBinding;


public class UpdatePasswordFragment extends BottomSheetDialogFragment {

    private FragmentUpdatePasswordBinding binding;

    public UpdatePasswordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdatePasswordBinding.inflate(inflater, null, false);
        eventClick();
        return binding.getRoot();
    }

    private void eventClick(){
        binding.btnUpdatePw.setOnClickListener(view -> {
            if(checkEditText()){
                xacThucNguoiDung();
            }
        });
    }

    private void xacThucNguoiDung(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider
                .getCredential(user.getEmail(), binding.etPassword.getText().toString().trim());
        user.reauthenticate(authCredential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                updatePassword(binding.etNewPW.getText().toString().trim());
            }else {
                showToast("Mật khẩu hiện tại không chính xác");
            }
        });
    }

    private void updatePassword(String s){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(s).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                showToast("Đổi mật khẩu thành công");
            }else {
                showToast("Đổi mật khẩu thất bại");
            }
        });
    }

    private boolean checkEditText(){
        if(binding.etPassword.getText().toString().trim().isEmpty()){
            showToast("Mật khẩu hiện tại không được để trống");
            return false;
        }else if(binding.etNewPW.getText().toString().trim().isEmpty()){
            showToast("Mật khẩu mới không được để trống");
            return false;
        }else if(binding.etReNewPW.getText().toString().trim().isEmpty()){
            showToast("Xác nhận mật khẩu mới không được để trống");
            return false;
        }else if(!binding.etNewPW.getText().toString().equals(binding.etReNewPW.getText().toString())){
            showToast("Xác nhận mật khẩu mới không giống với mật khẩu mới");
            return false;
        }else return true;
    }

    private void showToast(String s){
        Toast.makeText(getActivity().getApplicationContext(),s, Toast.LENGTH_SHORT).show();
    }
}