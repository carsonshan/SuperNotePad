package com.fairhand.supernotepad.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.puzzle.affix.util.PickStickerWhichCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 照片贴纸
 *
 * @author Phanton
 * @date 11/22/2018 - Thursday - 10:24 PM
 */
public class PhotoStickerFragment extends Fragment implements View.OnClickListener {
    
    @BindView(R.id.iv_sticker_cat)
    ImageView ivStickerCat;
    @BindView(R.id.iv_sticker_dog)
    ImageView ivStickerDog;
    @BindView(R.id.iv_sticker_fish)
    ImageView ivStickerFish;
    @BindView(R.id.iv_sticker_emoji)
    ImageView ivStickerEmoji;
    @BindView(R.id.iv_sticker_rabbit)
    ImageView ivStickerRabbit;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    Unbinder unbinder;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_sticker, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }
    
    private void initData() {
        ivCancel.setOnClickListener(this);
        ivStickerCat.setOnClickListener(this);
        ivStickerDog.setOnClickListener(this);
        ivStickerFish.setOnClickListener(this);
        ivStickerEmoji.setOnClickListener(this);
        ivStickerRabbit.setOnClickListener(this);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                mCallBack.onCancel();
                break;
            case R.id.iv_sticker_cat:
                mCallBack.onPickStickerCat();
                break;
            case R.id.iv_sticker_dog:
                mCallBack.onPickStickerDog();
                break;
            case R.id.iv_sticker_fish:
                mCallBack.onPickStickerFish();
                break;
            case R.id.iv_sticker_emoji:
                mCallBack.onPickStickerEmoji();
                break;
            case R.id.iv_sticker_rabbit:
                mCallBack.onPickStickerRabbit();
                break;
            default:
                break;
        }
    }
    
    public static PickStickerWhichCallBack mCallBack;
    
    public static void setCallBack(PickStickerWhichCallBack callBack) {
        mCallBack = callBack;
    }
    
}
