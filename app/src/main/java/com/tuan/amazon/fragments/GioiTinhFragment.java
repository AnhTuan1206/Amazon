package com.tuan.amazon.fragments;

import static com.tuan.amazon.activities.MainActivity.userCurrentID;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

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
import com.tuan.amazon.databinding.FragmentGioiTinhBinding;
import com.tuan.amazon.utilities.Constants;

import java.util.HashMap;
import java.util.Map;


public class GioiTinhFragment extends Fragment implements TextWatcher {

    private FragmentGioiTinhBinding binding;
    private FirebaseFirestore firestore;
    private  String gioiTinh ;
    public GioiTinhFragment() {
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
        binding = FragmentGioiTinhBinding.inflate(inflater, null, false);
        initView();
        eventClick();
        return binding.getRoot();
    }

    private void init(){
        gioiTinh = getArguments().getString(Constants.KEY_GIOI_TINH);
        firestore = FirebaseFirestore.getInstance();
    }

    private void initView(){
        Log.d("com.tuan.amazon.test", getArguments().getString(Constants.KEY_CONG_KHAI_GIOI_TINH));
        if(gioiTinh.equals("Nam")){
            binding.rbNam.setChecked(true);
        }else binding.rbNu.setChecked(true);
        binding.btnCheDoCongKhai.setText(getArguments().getString(Constants.KEY_CONG_KHAI_GIOI_TINH));
        switch (getArguments().getString(Constants.KEY_CONG_KHAI_GIOI_TINH)){
            case Constants.KEY_CDCK_BAN_BE:
                binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sex, 0, R.drawable.ic_down, 0);
                break;
            case Constants.KEY_CDCK_MINH_TOI:
                binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_only_me,0,R.drawable.ic_down,0);
                break;
        }
        binding.btnCheDoCongKhai.addTextChangedListener(this);
        radioButtonCheckChanged();
    }

    private void radioButtonCheckChanged(){
        binding.rbNam.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                switch (gioiTinh){
                    case "Nam":
                        binding.btnSave.setTextColor(Color.GRAY);
                        Resources res = getResources();
                        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
                        binding.btnSave.setBackground(drawable);
                        binding.btnSave.setEnabled(false);
                        break;
                    case "Nữ":
                        setEnableBtnSave();
                        break;
                }
            }else {
                switch (gioiTinh){
                    case "Nữ":
                        binding.btnSave.setTextColor(Color.GRAY);
                        Resources res = getResources();
                        Drawable drawable = ResourcesCompat.getDrawable(res, R.drawable.background_btn_whiter, null);
                        binding.btnSave.setBackground(drawable);
                        binding.btnSave.setEnabled(false);
                        break;
                    case "Nam":
                        setEnableBtnSave();
                        break;
                }
            }
        });

    }

    private void setEnableBtnSave(){
        binding.btnSave.setTextColor(Color.WHITE);
        binding.btnSave.setBackgroundColor(Color.BLUE);
        binding.btnSave.setEnabled(true);
    }

    private void eventClick(){
        binding.btnSave.setOnClickListener(view -> {
            String gioiTinh;
            if(binding.rbNam.isChecked()){
                gioiTinh = "Nam";
            }else gioiTinh = "Nữ";
            Map<String, Object> map = new HashMap<>();
            map.put(Constants.KEY_GIOI_TINH, gioiTinh);
            map.put(Constants.KEY_CONG_KHAI_GIOI_TINH, binding.btnCheDoCongKhai.getText().toString());
            firestore.collection(Constants.KEY_PERSONAL_INFORMATION)
                    .document(userCurrentID)
                    .collection(Constants.KEY_GIOI_TINH)
                    .document(userCurrentID)
                    .set(map)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Thêm thông tin giới tính thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.btnCheDoCongKhai.setOnClickListener(view -> {
            dieuChinhCongKhai();
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
            binding.btnCheDoCongKhai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sex, 0, R.drawable.ic_down, 0);
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