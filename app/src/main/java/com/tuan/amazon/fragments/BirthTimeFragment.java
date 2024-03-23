package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentBirthTimeBinding;
import com.tuan.amazon.utilities.Constants;

import java.util.HashMap;
import java.util.Map;


public class BirthTimeFragment extends Fragment implements TextWatcher {
    private FragmentBirthTimeBinding binding;
    private FirebaseFirestore firestore;
    private String namSinh;
    private static int day , month , year;

    public BirthTimeFragment() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBirthTimeBinding.inflate(inflater, null, false);
        initView();
        eventClick();
        return binding.getRoot();
    }

    private void init(){
        firestore = FirebaseFirestore.getInstance();
        namSinh = getArguments().getString(Constants.KEY_NAM_SINH);
        if(namSinh!= null){
            day = Integer.parseInt(namSinh.substring(0,2));
            month = Integer.parseInt(namSinh.substring(3,5));
            year = Integer.parseInt(namSinh.substring(6));
        }
    }

    private void initView(){
        binding.dpBirthTime.updateDate(year, month-1, day);
        binding.btnCheDoCongKhai.addTextChangedListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.dpBirthTime.setOnDateChangedListener((datePicker, i, i1, i2) -> {
                if(i != year || i1+1 != month || i2 != day){
                    setEnableBtnSave();
                }
                else {
                    binding.btnSave.setTextColor(Color.GRAY);
                    Resources res = getResources();
                    Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
                    binding.btnSave.setBackground(drawable);
                    binding.btnSave.setEnabled(false);
                }
            });
        }
    }


    private void eventClick(){
        binding.btnSave.setOnClickListener(view -> {
            String day = null, month = null, year;
            if(binding.dpBirthTime.getDayOfMonth()<10){
                day = "0"+ String.valueOf(binding.dpBirthTime.getDayOfMonth());
            }else day = String.valueOf(binding.dpBirthTime.getDayOfMonth());
            if(binding.dpBirthTime.getMonth()<9){
                month = "0"+String.valueOf(binding.dpBirthTime.getMonth()+1);
            }else String.valueOf(binding.dpBirthTime.getMonth()+1);
            year = String.valueOf(binding.dpBirthTime.getYear());
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.KEY_NAM_SINH,day + "/" + month + "/" + year);
            map.put(Constants.KEY_CONG_KHAI_NAM_SINH, binding.btnCheDoCongKhai.getText().toString());
            firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                    .document(userCurrentID)
                    .collection(Constants.KEY_NAM_SINH)
                    .document(userCurrentID)
                    .set(map)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Thêm thông tin năm sinh thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void setEnableBtnSave(){
        binding.btnSave.setTextColor(Color.WHITE);
        binding.btnSave.setBackgroundColor(Color.BLUE);
        binding.btnSave.setEnabled(true);
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!binding.btnCheDoCongKhai.getText().toString().equals(getArguments().getString(Constants.KEY_CONG_KHAI_GIOI_TINH))){
            setEnableBtnSave();
        }else {
            binding.btnSave.setTextColor(Color.GRAY);
            Resources res = getResources();
            Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
            binding.btnSave.setBackground(drawable);
            binding.btnSave.setEnabled(false);
        }
    }
}