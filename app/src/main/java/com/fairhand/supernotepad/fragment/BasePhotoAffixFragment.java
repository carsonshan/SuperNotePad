package com.fairhand.supernotepad.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fairhand.supernotepad.puzzle.affix.PickLayoutThemeCallBack;

/**
 * 图片拼接的布局主题的基类Fragment
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 10:18 PM
 */
public abstract class BasePhotoAffixFragment extends Fragment {
    
    public ImageView ivTemplateOne;
    public ImageView ivTemplateTwo;
    public ImageView ivTemplateThree;
    
    public static PickLayoutThemeCallBack mCallBack;
    
    public static void setCallBack(PickLayoutThemeCallBack callBack) {
        mCallBack = callBack;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setView(), container, false);
    }
    
    /**
     * 设置视图
     *
     * @return 布局id
     */
    public abstract int setView();
    
    /**
     * 初始化
     *
     * @param view 当前视图
     */
    public abstract void init(View view);
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }
    
}
