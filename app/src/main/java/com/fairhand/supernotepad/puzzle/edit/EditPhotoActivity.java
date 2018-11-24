package com.fairhand.supernotepad.puzzle.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.fragment.PhotoFilterFragment;
import com.fairhand.supernotepad.fragment.PhotoStickerFragment;
import com.fairhand.supernotepad.puzzle.affix.PickFilterKindCallBack;
import com.fairhand.supernotepad.puzzle.affix.util.PickStickerWhichCallBack;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.fairhand.supernotepad.view.DiyFontDialog;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

/**
 * 编辑图片界面
 *
 * @author Phanton
 * @date 2018/11/22 - 星期四 - 9:48AM
 */
public class EditPhotoActivity extends AppCompatActivity
        implements View.OnClickListener, PickFilterKindCallBack, PickStickerWhichCallBack {
    
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_complete)
    ImageView ivComplete;
    @BindView(R.id.tv_font)
    TextView tvFont;
    @BindView(R.id.tv_sticker)
    TextView tvSticker;
    @BindView(R.id.gpu_image_view)
    ImageView gpuImageView;
    @BindView(R.id.stick_view)
    StickerView stickView;
    @BindView(R.id.tv_filter)
    TextView tvFilter;
    @BindView(R.id.bottom_edit_tool_bar)
    FrameLayout bottomEditToolBar;
    @BindView(R.id.group)
    Group group;
    
    private TextSticker textSticker;
    private String photoFilePath;
    private String databasePath;
    private GPUImage gpuImage;
    private Bitmap bitmap;
    private Bitmap rootBitmap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        ButterKnife.bind(this);
        initData();
        setData();
    }
    
    private void initData() {
        gpuImage = new GPUImage(this);
        // 获取传过来的照片地址
        Intent intent = getIntent();
        photoFilePath = intent.getStringExtra("photo_path");
        databasePath = intent.getStringExtra("database_path");
        bitmap = BitmapFactory.decodeFile(photoFilePath);
        rootBitmap = bitmap;
        gpuImageView.setImageBitmap(bitmap);
        
        // 默认边框
        stickView.configDefaultIcons();
    }
    
    private void setData() {
        ivBack.setOnClickListener(this);
        ivComplete.setOnClickListener(this);
        tvFont.setOnClickListener(this);
        tvSticker.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        
        PhotoFilterFragment.setCallBack(this);
        PhotoStickerFragment.setCallBack(this);
        
        stickView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
            }
            
            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                Logger.d("sticker", "单击");
                if (sticker instanceof TextSticker) {
                    // 文字贴纸
                    Logger.d("sticker", "文字");
                    DiyFontDialog dialog = new DiyFontDialog(EditPhotoActivity.this, R.style.DiyDialogStyle);
                    dialog.setOnConfirmListener(v -> {
                        String text = dialog.getInputText();
                        Typeface typeface = dialog.getTextTypeface();
                        int color = dialog.getTextColor();
                        textSticker.setText(text);
                        textSticker.setTextColor(color);
                        textSticker.setTypeface(typeface);
                        textSticker.resizeText();
                        dialog.cancel();
                    });
                    dialog.show();
                } else {
                    Logger.d("sticker", "图片");
                }
            }
            
            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
            }
            
            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
            }
            
            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
            }
            
            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
            }
            
            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Logger.d("sticker", "双击");
            }
        });
    }
    
    private boolean isSaved;
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.iv_complete:
                reSave();
                break;
            // 文字
            case R.id.tv_font:
                createFont();
                break;
            // 贴纸
            case R.id.tv_sticker:
                showStickerPanel();
                break;
            // 滤镜
            case R.id.tv_filter:
                showFilterPanel();
                break;
            default:
                break;
        }
    }
    
    private void back() {
        if (isSaved) {
            finish();
        } else {
            DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
            dialog.setCancelable(false);
            dialog.setTitle("提示")
                    .setMessage("是否确认放弃编辑？")
                    .setOnNegativeClickListener("放弃编辑", v12 -> {
                        dialog.dismiss();
                        finish();
                    })
                    .setOnPositiveClickedListener("继续编辑", v1 -> dialog.dismiss())
                    .show();
        }
    }
    
    private void createFont() {
        textSticker = new TextSticker(this);
        textSticker.setMinTextSize(12);
        textSticker.setMaxTextSize(24);
        textSticker.setTextColor(Color.WHITE);
        textSticker.setText("点击输入文字");
        textSticker.resizeText();
        stickView.addSticker(textSticker, Sticker.Position.CENTER);
    }
    
    private void showFilterPanel() {
        replaceFragment(new PhotoFilterFragment());
        bottomEditToolBar.setVisibility(View.VISIBLE);
        group.setVisibility(View.GONE);
    }
    
    private void showStickerPanel() {
        replaceFragment(new PhotoStickerFragment());
        bottomEditToolBar.setVisibility(View.VISIBLE);
        group.setVisibility(View.GONE);
    }
    
    private void reSave() {
        stickView.save(new File(photoFilePath));
        stickView.save(new File(databasePath));
        Snackbar.make(ivComplete, "保存成功", Snackbar.LENGTH_SHORT).show();
        isSaved = true;
    }
    
    @Override
    public void onCancel() {
        bottomEditToolBar.setVisibility(View.GONE);
        group.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onPickStickerCat() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_sticker_cat);
        stickView.addSticker(new DrawableSticker(drawable));
    }
    
    @Override
    public void onPickStickerDog() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_sticker_dog);
        stickView.addSticker(new DrawableSticker(drawable));
    }
    
    @Override
    public void onPickStickerEmoji() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_sticker_emoji);
        stickView.addSticker(new DrawableSticker(drawable));
    }
    
    @Override
    public void onPickStickerRabbit() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_sticker_rabbit);
        stickView.addSticker(new DrawableSticker(drawable));
    }
    
    @Override
    public void onPickStickerFish() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_sticker_fish);
        stickView.addSticker(new DrawableSticker(drawable));
    }
    
    @Override
    public void onPickOriginPhoto() {
        gpuImage.setImage(rootBitmap);
        gpuImage.setFilter(new GPUImageBrightnessFilter());
        bitmap = gpuImage.getBitmapWithFilterApplied();
        gpuImageView.setImageBitmap(bitmap);
    }
    
    @Override
    public void onPickGrayFilter() {
        gpuImage.setImage(rootBitmap);
        gpuImage.setFilter(new GPUImageGrayscaleFilter());
        bitmap = gpuImage.getBitmapWithFilterApplied();
        gpuImageView.setImageBitmap(bitmap);
    }
    
    @Override
    public void onPickCartoonFilter() {
        gpuImage.setImage(rootBitmap);
        gpuImage.setFilter(new GPUImageToonFilter());
        bitmap = gpuImage.getBitmapWithFilterApplied();
        gpuImageView.setImageBitmap(bitmap);
    }
    
    @Override
    public void onPickNostalgiaFilter() {
        gpuImage.setImage(rootBitmap);
        gpuImage.setFilter(new GPUImageMonochromeFilter());
        bitmap = gpuImage.getBitmapWithFilterApplied();
        gpuImageView.setImageBitmap(bitmap);
    }
    
    @Override
    public void onPickSketchFilter() {
        gpuImage.setImage(rootBitmap);
        gpuImage.setFilter(new GPUImageSketchFilter());
        bitmap = gpuImage.getBitmapWithFilterApplied();
        gpuImageView.setImageBitmap(bitmap);
    }
    
    /**
     * 动态更改显示布局
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.bottom_edit_tool_bar, fragment);
        transaction.commit();
    }
    
    @Override
    public void onBackPressed() {
        back();
    }
    
}
