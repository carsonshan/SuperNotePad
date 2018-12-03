package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.util.CacheUtil;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.fairhand.supernotepad.view.ItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 *
 * @author Phanton
 * @date 11/28/2018 - Wednesday - 6:08 PM
 */
public class SettingActivity extends AppCompatActivity {
    
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_about_app)
    ItemView ivAboutApp;
    @BindView(R.id.iv_exit_app)
    ItemView ivExitApp;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settinig);
        ButterKnife.bind(this);
    }
    
    @OnClick( {R.id.iv_back, R.id.iv_about_app, R.id.iv_exit_app})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_about_app:
                about();
                break;
            case R.id.iv_exit_app:
                exit();
                break;
            default:
                break;
        }
    }
    
    private void about() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.TransparentBottomSheetStyle);
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_about, rootView, false);
        dialog.setContentView(view);
        dialog.show();
        TextView checkUpdate = view.findViewById(R.id.tv_check_update);
        checkUpdate.setOnClickListener(v -> check());
    }
    
    private void check() {
    
    }
    
    /**
     * 退出程序
     */
    private void exit() {
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("提示")
                .setMessage("确认退出？")
                .setOnPositiveClickedListener("确认", v -> {
                    startActivity(new Intent(SettingActivity.this, WelcomeActivity.class));
                    CacheUtil.putTouristYet(getApplicationContext(), false);
                    Config.isTourist = false;
                    finishAffinity();
                    dialog.dismiss();
                })
                .setOnNegativeClickListener("取消", v -> dialog.dismiss())
                .show();
    }
    
}
