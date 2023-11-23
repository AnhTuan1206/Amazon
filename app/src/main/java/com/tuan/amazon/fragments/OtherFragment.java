package com.tuan.amazon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuan.amazon.databinding.FragmentOtherBinding;


public class OtherFragment extends Fragment {

    private FragmentOtherBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtherBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
}