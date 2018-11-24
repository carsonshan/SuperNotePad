package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fairhand.supernotepad.R;

/**
 * 自定义查看拼图记事Dialog
 *
 * @author Phanton
 * @date 11/23/2018 - Friday - 5:15 PM
 */
public class DiyObservePuzzleDialog extends BaseDialog {
    
    private String contentShow;
    private Bitmap bitmap;
    
    private Context context;
    
    public DiyObservePuzzleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    
    @Override
    protected int setView() {
        return R.layout.dialog_diy_observe_puzzle;
    }
    
    @Override
    protected void initData() {
        TextView tvContentShow = findViewById(R.id.tv_content_show);
        ImageView ivPhotoShow = findViewById(R.id.iv_photo_show);
        
        if (!TextUtils.isEmpty(contentShow)) {
            tvContentShow.setText(contentShow);
        }
        if (bitmap != null) {
            Glide.with(context).load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(ivPhotoShow);
        }
    }
    
    public DiyObservePuzzleDialog setTvContentShow(String contentShow) {
        this.contentShow = contentShow;
        return this;
    }
    
    public DiyObservePuzzleDialog setIvPhotoShow(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }
    
}
