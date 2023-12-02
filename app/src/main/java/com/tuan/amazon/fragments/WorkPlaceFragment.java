package com.tuan.amazon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuan.amazon.R;
import com.tuan.amazon.databinding.FragmentWorkPlaceBinding;


public class WorkPlaceFragment extends Fragment {

    private FragmentWorkPlaceBinding binding;

    public WorkPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWorkPlaceBinding.inflate(inflater, null, false);
        return binding.getRoot();
    }
}