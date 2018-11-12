package com.fairhand.supernotepad.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.view.PinchImageView;

/**
 * 浏览已选图片<br/>
 * 每一张图片就是一个GalleryFragment
 *
 * @author FairHand
 * @date 2018/11/2
 */
public class GalleryFragment extends Fragment {
    
    private static final String PICK_IMAGE_URI = "PICK_IMAGE_URI";
    private Context mContext;
    
    /**
     * 实例化此Fragment
     *
     * @param uri 图片路径
     */
    public static Fragment newInstance(String uri) {
        Bundle bundle = new Bundle();
        bundle.putString(PICK_IMAGE_URI, uri);
        GalleryFragment fragment = new GalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        PinchImageView galleryImage = view.findViewById(R.id.fiv_gallery_item);
        
        // 获取传递过来的图片地址并加载
        assert getArguments() != null;
        String uri = getArguments().getString(PICK_IMAGE_URI);
        Glide.with(mContext).load(uri).into(galleryImage);
        
        return view;
    }
    
}

// 22:54休息
