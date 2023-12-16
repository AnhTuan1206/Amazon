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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentNoiSongHienTaiBinding;
import com.tuan.amazon.utilities.Constants;

import java.util.HashMap;
import java.util.Map;


public class noiSongHienTaiFragment extends Fragment implements TextWatcher {
    public noiSongHienTaiFragment() {
    }

    private FragmentNoiSongHienTaiBinding binding;
    private FirebaseFirestore firestore;
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
        evnetsClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView(){
        binding.etThemNoiSong.addTextChangedListener(this);
    }

    private void evnetsClick(){
        binding.btnSave.setOnClickListener(v -> {
                Map map = new HashMap();
                String city = binding.etThemNoiSong.getText().toString();
                String cheDo = binding.btnCheDoCongKhai.getText().toString();
                map.put(Constants.KEY_NOI_O, city);
                map.put(Constants.KEY_CONG_KHAI_NOI_O, cheDo);
                firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                        .document(userCurrentID)
                        .set(map)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(),"Thêm tỉnh/thành phố hiện tại thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
            });

        binding.btnCheDoCongKhai.setOnClickListener(v ->{

        });

        binding.btnBack.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.profilePersonalFragment);
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
        if(!binding.etThemNoiSong.getText().toString().trim().isEmpty()){
            binding.btnSave.setTextColor(Color.WHITE);
            binding.btnSave.setBackgroundColor(Color.BLUE);
            binding.btnSave.setEnabled(true);
        } else if(binding.etThemNoiSong.getText().toString().trim().isEmpty()){
            binding.btnSave.setTextColor(Color.GRAY);
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
            binding.btnSave.setBackground(drawable);
            binding.btnSave.setEnabled(false);
        }
    }
}