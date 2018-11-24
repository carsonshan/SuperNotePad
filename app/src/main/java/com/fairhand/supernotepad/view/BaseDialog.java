package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author FairHand
 * @date 2018/10/6
 */
public abstract class BaseDialog extends Dialog {
    
    BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Window window = getWindow();
        assert window != null;
        setTranslucentStatus(window);
        setContentView(setView());
        // 设置Dialog全屏
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        initData();
    }
    
    /**
     * 设置Dialog视图
     *
     * @return view id
     */
    protected abstract int setView();
    
    /**
     * 初始化数据
     */
    protected abstract void initData();
    
    /**
     * 透明状态栏
     */
    private void setTranslucentStatus(Window window) {
        if (window != null) {
            // 5.0 全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {// 4.4 全透明
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }
    
}
