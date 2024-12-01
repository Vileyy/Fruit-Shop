package com.example.fruit_shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fruit_shop.User.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CongratsBottomSheetFragment extends BottomSheetDialogFragment {
    public CongratsBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_congrats_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button goBack = view.findViewById(R.id.goHomeButton);
        goBack.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HomeActivity.class));
        });
    }
}