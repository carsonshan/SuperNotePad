package com.fairhand.supernotepad.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

/**
 * 数字指标
 *
 * @author Phanton
 * @date 12/1/2018 - Saturday - 3:00 PM
 */
public class NumberIndicator extends LinearLayout {
    
    TextView tvCurrentNumber;
    TextView tvTotalNumber;
    
    public NumberIndicator(Context context) {
        super(context);
        initView(context);
    }
    
    public NumberIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    
    public NumberIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    
    private void initView(Context context) {
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.indicator_number, this);
        
        tvCurrentNumber = findViewById(R.id.tv_current_number);
        tvTotalNumber = findViewById(R.id.tv_total_number);
    }
    
    public void setTvCurrentNumber(String number) {
        tvCurrentNumber.setText(number);
    }
    
    public void setTvTotalNumber(String number) {
        tvTotalNumber.setText(number);
    }
    
}
