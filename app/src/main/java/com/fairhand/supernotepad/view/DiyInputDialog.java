package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

/**
 * DIY输入对话框
 *
 * @author FairHand
 * @date 2018/11/3
 */
public class DiyInputDialog extends Dialog {
    
    private String title;
    
    private String cancelText;
    private String confirmText;
    
    private EditText message;
    
    private View.OnClickListener onPositiveClickedListener;
    private View.OnClickListener onNegativeClickedListener;
    
    public DiyInputDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_input);
        init();
    }
    
    private void init() {
        TextView title = findViewById(R.id.tv_title_message);
        message = findViewById(R.id.et_input_message);
        TextView cancel = findViewById(R.id.tv_cancel);
        TextView confirm = findViewById(R.id.tv_confirm);
        
        if (onNegativeClickedListener != null) {
            if (!TextUtils.isEmpty(this.cancelText)) {
                cancel.setText(this.cancelText);
            }
            cancel.setOnClickListener(onNegativeClickedListener);
        }
        if (onPositiveClickedListener != null) {
            if (!TextUtils.isEmpty(this.confirmText)) {
                confirm.setText(this.confirmText);
            }
            confirm.setOnClickListener(onPositiveClickedListener);
        }
        
        if (!TextUtils.isEmpty(this.title)) {
            title.setVisibility(View.VISIBLE);
            title.setText(this.title);
        } else {
            title.setVisibility(View.GONE);
        }
    }
    
    /**
     * 确认监听
     */
    public DiyInputDialog setOnPositiveClickedListener(
            String confirmText,
            View.OnClickListener onPositiveClickedListener) {
        this.confirmText = confirmText;
        this.onPositiveClickedListener = onPositiveClickedListener;
        return this;
    }
    
    /**
     * 取消监听
     */
    public DiyInputDialog setOnNegativeClickListener(
            String cancelText,
            View.OnClickListener onNegativeClickedListener) {
        this.cancelText = cancelText;
        this.onNegativeClickedListener = onNegativeClickedListener;
        return this;
    }
    
    /**
     * 设置标题
     */
    public DiyInputDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    /**
     * 获取输入的记事标题
     */
    public String getMessage() {
        return message.getText().toString();
    }
    
}
