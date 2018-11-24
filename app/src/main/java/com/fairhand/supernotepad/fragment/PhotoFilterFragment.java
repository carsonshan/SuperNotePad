package com.fairhand.supernotepad.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.puzzle.affix.PickFilterKindCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 照片滤镜
 *
 * @author Phanton
 * @date 11/22/2018 - Thursday - 9:20 PM
 */
public class PhotoFilterFragment extends Fragment implements View.OnClickListener {
    
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.tv_origin_photo)
    TextView tvOriginPhoto;
    @BindView(R.id.tv_gray_photo)
    TextView tvGrayPhoto;
    @BindView(R.id.tv_cartoon_photo)
    TextView tvCartoonPhoto;
    @BindView(R.id.tv_nostalgia_photo)
    TextView tvNostalgiaPhoto;
    @BindView(R.id.tv_sketch_photo)
    TextView tvSketchPhoto;
    Unbinder unbinder;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    
    private void initData() {
        ivCancel.setOnClickListener(this);
        tvOriginPhoto.setOnClickListener(this);
        tvGrayPhoto.setOnClickListener(this);
        tvCartoonPhoto.setOnClickListener(this);
        tvNostalgiaPhoto.setOnClickListener(this);
        tvSketchPhoto.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                mCallBack.onCancel();
                break;
            case R.id.tv_origin_photo:
                mCallBack.onPickOriginPhoto();
                break;
            case R.id.tv_gray_photo:
                mCallBack.onPickGrayFilter();
                break;
            case R.id.tv_cartoon_photo:
                mCallBack.onPickCartoonFilter();
                break;
            case R.id.tv_nostalgia_photo:
                mCallBack.onPickNostalgiaFilter();
                break;
            case R.id.tv_sketch_photo:
                mCallBack.onPickSketchFilter();
                break;
            default:
                break;
        }
    }
    
    protected static PickFilterKindCallBack mCallBack;
    
    public static void setCallBack(PickFilterKindCallBack callBack) {
        mCallBack = callBack;
    }
    
}
