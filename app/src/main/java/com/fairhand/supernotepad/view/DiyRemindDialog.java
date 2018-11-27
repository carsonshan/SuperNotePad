package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 事件记事，提不提醒对话框
 *
 * @author Phanton
 * @date 11/25/2018 - Sunday - 12:19 PM
 */
public class DiyRemindDialog extends Dialog {
    
    @BindView(R.id.tv_remind_punctuality)
    TextView tvRemindPunctuality;
    @BindView(R.id.tv_not_remind)
    TextView tvNotRemind;
    
    private View.OnClickListener onRemindPunctualityListener;
    private View.OnClickListener onNotRemindListener;
    
    public DiyRemindDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_dialog_remind);
        ButterKnife.bind(this);
        
        if (onRemindPunctualityListener != null) {
            tvRemindPunctuality.setOnClickListener(onRemindPunctualityListener);
        }
        if (onNotRemindListener != null) {
            tvNotRemind.setOnClickListener(onNotRemindListener);
        }
    }
    
    public DiyRemindDialog setOnRemindPunctualityListener(View.OnClickListener onRemindPunctualityListener) {
        this.onRemindPunctualityListener = onRemindPunctualityListener;
        return this;
    }
    
    public DiyRemindDialog setOnNotRemindListener(View.OnClickListener onNotRemindListener) {
        this.onNotRemindListener = onNotRemindListener;
        return this;
    }
    
}
