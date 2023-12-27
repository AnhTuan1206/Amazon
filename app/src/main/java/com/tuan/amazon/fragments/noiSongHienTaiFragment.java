package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentNoiSongHienTaiBinding;
import com.tuan.amazon.utilities.Constants;
import com.tuan.amazon.utilities.PreferenceManager;

import java.util.HashMap;
import java.util.Map;


public class noiSongHienTaiFragment extends Fragment implements TextWatcher {
    private FragmentNoiSongHienTaiBinding binding;
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
        binding = FragmentNoiSongHienTaiBinding.inflate(inflater, null, false);
        initView();
        eventsClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getActivity().getApplicationContext());
    }

    private void initView(){
        binding.etThemNoiSong.setText(preferenceManager.getString(Constants.KEY_NOI_O));
        binding.btnCheDoCongKhai.setText(preferenceManager.getString(Constants.KEY_CONG_KHAI_NOI_O));
        switch (preferenceManager.getString(Constants.KEY_CONG_KHAI_NOI_O)){
            case Constants.KEY_CDCK_BAN_BE:
                binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_relationship, 0, R.drawable.ic_down, 0);
                break;
            case Constants.KEY_CDCK_MINH_TOI:
                binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_only_me,0,R.drawable.ic_down,0);
                break;
        }
        Log.d("com.tuan.amazon.test", preferenceManager.getString(Constants.KEY_CONG_KHAI_NOI_O).toString());
        binding.etThemNoiSong.addTextChangedListener(this);
        binding.btnCheDoCongKhai.addTextChangedListener(this);
    }

    private void eventsClick(){
        binding.btnSave.setOnClickListener(v -> {
                Map map = new HashMap();
                map.put(Constants.KEY_NOI_O, binding.etThemNoiSong.getText().toString());
                map.put(Constants.KEY_CONG_KHAI_NOI_O, binding.btnCheDoCongKhai.getText().toString());
                firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                        .document(userCurrentID)
                        .collection(Constants.KEY_NOI_O)
                        .document(userCurrentID)
                        .set(map)
                        .addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), "Thêm thông tin nơi sống hiện tại thành công", Toast.LENGTH_SHORT).show();
                        });

            });

        binding.btnCheDoCongKhai.setOnClickListener(v ->{
            dieuChinhCongKhai();
        });

        binding.btnBack.setOnClickListener(v ->{
//            Navigation.findNavController(v).navigate(R.id.profilePersonalFragment);
        });
    }

    private void dieuChinhCongKhai(){
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_congkhai, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();

        view.findViewById(R.id.layoutOnlyMe).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            binding.btnCheDoCongKhai.setText(Constants.KEY_CDCK_MINH_TOI);
            binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_only_me,0,R.drawable.ic_down,0);

        });
        view.findViewById(R.id.layoutCongKhai).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            binding.btnCheDoCongKhai.setText(Constants.KEY_CDCK_CONG_KHAI);
            binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_public, 0, R.drawable.ic_down, 0);
        });
        view.findViewById(R.id.layoutFriend1).setOnClickListener(v ->{
            bottomSheetDialog.dismiss();
            binding.btnCheDoCongKhai.setText(Constants.KEY_CDCK_BAN_BE);
            binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_relationship, 0, R.drawable.ic_down, 0);
        });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!binding.etThemNoiSong.getText().toString().equals(preferenceManager.getString(Constants.KEY_NOI_O))
         || !binding.btnCheDoCongKhai.getText().toString().equals(preferenceManager.getString(Constants.KEY_CONG_KHAI_NOI_O))){
            binding.btnSave.setTextColor(Color.WHITE);
            binding.btnSave.setBackgroundColor(Color.BLUE);
            binding.btnSave.setEnabled(true);
        } else {
            binding.btnSave.setTextColor(Color.GRAY);
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
            binding.btnSave.setBackground(drawable);
            binding.btnSave.setEnabled(false);
        }
    }
}