package com.fairhand.supernotepad.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.util.Logger;

/**
 * 欢迎界面
 *
 * @author FairHand
 * @date 2018/10/22
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener, SignInActivity.LoginCallBack {
    
    private TextView signIn;
    private TextView signUp;
    private View signInView;
    private ImageView loginFace;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        initView();
        initData();
    }
    
    /**
     * 初始化视图
     */
    private void initView() {
        // 改状态栏字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        
        loginFace = findViewById(R.id.iv_login_face);
        signIn = findViewById(R.id.tv_sign_in);
        signUp = findViewById(R.id.tv_sign_up);
        signInView = findViewById(R.id.view_sign_in);
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        signInView.setOnClickListener(this);
        
        SignInActivity.setCallBack(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_input_mob_number:
            case R.id.view_sign_in:
                // 跳转到登录界面
                Intent intentSignIn = new Intent(WelcomeActivity.this, SignInActivity.class);
                // 共享元素转场特效
                startActivity(intentSignIn, ActivityOptions.makeSceneTransitionAnimation(
                        WelcomeActivity.this,
                        new Pair<>(signIn, "share_sign_in"),
                        new Pair<>(loginFace, "share_image"),
                        new Pair<>(signInView, "share_purple")
                ).toBundle());
                break;
            // 跳转到注册界面
            case R.id.tv_sign_up:
                Intent intentSignUp = new Intent(WelcomeActivity.this, SignUpActivity.class);
                // 共享元素转场特效
                startActivity(intentSignUp, ActivityOptions.makeSceneTransitionAnimation(
                        WelcomeActivity.this,
                        new Pair<>(signUp, "share_sign_up"),
                        new Pair<>(loginFace, "share_image")
                ).toBundle());
                break;
            default:
                break;
        }
    }
    
    @Override
    public void loginSuccess() {
        Logger.d("回调销毁");
        finish();
    }
    
}
