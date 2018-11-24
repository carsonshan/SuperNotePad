package com.fairhand.supernotepad.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fairhand.supernotepad.R;

/**
 * @author Phanton
 * @date 11/24/2018 - Saturday - 3:44 PM
 */
public class GuideFragmentT extends Fragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_2, container, false);
    }
    
}
