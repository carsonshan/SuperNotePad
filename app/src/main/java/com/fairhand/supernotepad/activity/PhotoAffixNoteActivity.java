package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.util.UnitConversionUtil;
import com.fairhand.supernotepad.view.ConnectImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 拼图记事界面
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class PhotoAffixNoteActivity extends AppCompatActivity implements View.OnClickListener {
    
    @BindView(R.id.iv_affix_back)
    ImageView ivAffixBack;
    @BindView(R.id.iv_template_one)
    ImageView ivTemplateOne;
    @BindView(R.id.iv_template_two)
    ImageView ivTemplateTwo;
    @BindView(R.id.iv_template_three)
    ImageView ivTemplateThree;
    @BindView(R.id.ll_bottom_template)
    LinearLayout llBottomTemplate;
    @BindView(R.id.tv_template)
    TextView templateAffix;
    @BindView(R.id.tv_freedom)
    TextView freedomAffix;
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    @BindView(R.id.civ_vertical_image)
    ConnectImageView civVerticalImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_affix_note);
        ButterKnife.bind(this);
        
        initData();
    }
    
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    
    /**
     * 初始化数据
     */
    private void initData() {
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        // 拼接图的宽度
        float mWidth = metrics.widthPixels - UnitConversionUtil.dp2px(this, 24);
        
        // 获取选择的图片
        Intent intent = getIntent();
        ArrayList<String> path = intent.getStringArrayListExtra("path");
        for (String item : path) {
            bitmaps.add(BitmapFactory.decodeFile(item).copy(Bitmap.Config.ARGB_8888, true));
        }
        civVerticalImage.mergeBitmap(bitmaps, mWidth);
        ivAffixBack.setOnClickListener(this);
        tvConnect.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.iv_affix_back:
                onBackPressed();
                break;
            // 模板拼图一
            case R.id.iv_template_one:
                break;
            // 模板拼图二
            case R.id.iv_template_two:
                break;
            // 模板拼图三
            case R.id.iv_template_three:
                break;
            // 模板拼图
            case R.id.tv_template:
                break;
            // 自由拼图
            case R.id.tv_freedom:
                break;
            // 拼接
            case R.id.tv_connect:
                break;
            default:
                break;
        }
    }
    
}
