package com.tuan.amazon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuan.amazon.databinding.FragmentHomeTownBinding;


public class HomeTownFragment extends Fragment {

    private FragmentHomeTownBinding binding;


    public HomeTownFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeTownBinding.inflate(inflater, null, false);
        return binding.getRoot();
    }
}