package com.fairhand.supernotepad.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.WelcomeActivity;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.util.CacheUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Phanton
 * @date 11/24/2018 - Saturday - 3:45 PM
 */
public class GuideFragmentTh extends Fragment {
    
    private Context context;
    
    @BindView(R.id.btn_start_app)
    Button btnStartApp;
    Unbinder unbinder;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_3, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnStartApp.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), WelcomeActivity.class));
            Objects.requireNonNull(getActivity()).finish();
            CacheUtil.putFirstUse(context, false);
            Config.isFirstUse = false;
        });
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    
}
