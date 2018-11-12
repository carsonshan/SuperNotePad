package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.util.CacheUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.ItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 记事本
 *
 * @author Phanton
 * @date 2018/11/10 - 星期六 - 17:56
 */
public class NotePadActivity extends AppCompatActivity implements View.OnClickListener {
    
    @BindView(R.id.iv_back)
    ImageView ivAffixBack;
    @BindView(R.id.iv_default_note_pad)
    ItemView ivDefaultNotePad;
    @BindView(R.id.iv_secret_note_pad)
    ItemView ivSecretNotePad;
    @BindView(R.id.gifImageView)
    ImageView gifImageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pad);
        ButterKnife.bind(this);
        
        initData();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            ivDefaultNotePad.setRightText("当前记事本");
            ivSecretNotePad.setRightText("");
        } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
            ivSecretNotePad.setRightText("当前记事本");
            ivDefaultNotePad.setRightText("");
        }
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        Glide.with(this).load(R.drawable.halloween_trailer)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(32)))
                .into(gifImageView);
        ivAffixBack.setOnClickListener(this);
        ivDefaultNotePad.setOnClickListener(this);
        ivSecretNotePad.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            // 默认记事本
            case R.id.iv_default_note_pad:
                ivDefaultNotePad.setRightText("当前记事本");
                ivSecretNotePad.setRightText("");
                if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                    Toaster.showShort(this, "当前已在默认记事本，无需切换");
                } else {
                    Toaster.showShort(this, "已切换到默认记事本");
                    CacheUtil.putCurrentPad(this, Config.DEFAULT_PAD);
                    Config.currentPad = Config.DEFAULT_PAD;
                    mUpdateCallBack.updateMainView();
                }
                break;
            // 私密记事本
            case R.id.iv_secret_note_pad:
                if (Config.currentPad.equals(Config.SECRET_PAD)) {
                    Toaster.showShort(this, "当前已在私密记事本，无需切换");
                } else {
                    Intent intent = new Intent(NotePadActivity.this, LockActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
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
