package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fairhand.supernotepad.app.Config;

/**
 * 闪屏页
 *
 * @author FairHand
 * @date 2018/11/1
 */
public class SplashActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Config.isLogin) {
            // 已登录
            if (Config.currentPad.equals(Config.SECRET_PAD)) {
                // 私密记事，打开锁
                Intent intent = new Intent(SplashActivity.this, LockActivity.class);
                intent.putExtra("FROM_LOGIN", 1);
                startActivity(intent);
            } else {
                // 启动主页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        } else {
            // 未登录，启动欢迎界面
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
        }
        finish();
    }
    
}
