package com.tuan.amazon.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuan.amazon.R;
import com.tuan.amazon.activities.LoginActivity;
import com.tuan.amazon.databinding.FragmentOtherBinding;
import com.tuan.amazon.utilities.PreferenceManager;


public class OtherFragment extends Fragment{

    private FragmentOtherBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherBinding.inflate(inflater, container, false);
        eventClick();
        return binding.getRoot();

    }
    private void eventClick(){
        binding.layoutPassword.setOnClickListener(view -> {
            UpdatePasswordFragment updatePWFragment = new UpdatePasswordFragment();
            updatePWFragment.show(getActivity().getSupportFragmentManager(), updatePWFragment.getTag());
        });

        binding.layoutLogOut.setOnClickListener(view -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Đăng xuất");
            alertDialog.setMessage("Bạn có chắc muốn đăng xuất");
            alertDialog.setIcon(R.drawable.ic_logout);
            alertDialog.setPositiveButton("Có", (dialogInterface, i) -> {
                preferenceManager = new PreferenceManager(getContext());
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                preferenceManager.clear();
            });
            alertDialog.setNegativeButton("Không",(dialogInterface, i) -> {

            });
            alertDialog.show();
        });
    }


}