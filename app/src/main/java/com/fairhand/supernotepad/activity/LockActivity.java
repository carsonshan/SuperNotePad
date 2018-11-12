package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.util.CacheUtil;
import com.fairhand.supernotepad.view.DiyLockPanel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 密码锁
 *
 * @author Phanton
 * @date 2018/11/10 - 星期六 - 20:10
 */
public class LockActivity extends AppCompatActivity implements DiyLockPanel.OnPasswordSetComplete {
    
    @BindView(R.id.diy_lock_panel)
    DiyLockPanel diyLockPanel;
    
    private int temp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        temp = getIntent().getIntExtra("FROM_LOGIN", 0);
        
        diyLockPanel.setCallBack(this);
    }
    
    @Override
    public void onCheckSuccess() {
        if (temp == 1) {
            startActivity(new Intent(LockActivity.this, MainActivity.class));
            finish();
        } else {
            CacheUtil.putCurrentPad(this, Config.SECRET_PAD);
            Config.currentPad = Config.SECRET_PAD;
            mUpdateCallBack.updateMainView();
            finish();
        }
    }
    
    private static UpdateNote mUpdateCallBack;
    
    /**
     * 设置回调接口
     */
    public static void setCallBack(UpdateNote updateCallBack) {
        mUpdateCallBack = updateCallBack;
    }
    
}
