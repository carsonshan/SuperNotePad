package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查看头像详情Dialog
 *
 * @author Phanton
 * @date 11/28/2018 - Wednesday - 10:12 PM
 */
public class DiyShowAvatarDialog extends Dialog {
    
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_change_avatar)
    TextView tvChangeAvatar;
    @BindView(R.id.dialog_avatar_layout)
    ConstraintLayout dialogAvatarLayout;
    
    private View.OnClickListener onChangeAvatarClickListener;
    
    public DiyShowAvatarDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_show_avatar);
        ButterKnife.bind(this);
        
        dialogAvatarLayout.setOnClickListener(v -> dismiss());
        
        if (onChangeAvatarClickListener != null) {
            tvChangeAvatar.setOnClickListener(onChangeAvatarClickListener);
        }
    }
    
    public DiyShowAvatarDialog setOnChangeAvatarClickListener(View.OnClickListener onChangeAvatarClickListener) {
        this.onChangeAvatarClickListener = onChangeAvatarClickListener;
        return this;
    }
    
}
