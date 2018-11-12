package com.fairhand.supernotepad.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.GalleryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片返回GridView的适配器类
 *
 * @author FairHand
 * @date 2018/11/2
 */
public class PictureAdapter extends BaseAdapter {
    
    /**
     * Intent传递数据Key
     */
    public static final String KEY_ALL_PICK_IMAGE = "KEY_ALL_PICK_IMAGE";
    public static final String KEY_PICK_POSITION = "KEY_PICK_POSITION";
    
    /**
     * 数据源
     */
    private List<String> data;
    
    private Context mContext;
    private ScaleAnimation scaleAnimation;
    
    public PictureAdapter(Context context) {
        mContext = context;
        
        // 初始化缩放动画
        scaleAnimation = new ScaleAnimation(
                1, 0, 1, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(320);
    }
    
    /**
     * 更新并刷新适配器数据
     */
    public void updateData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }
    
    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return data == null ? 0 : position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (data == null) {
            return null;
        } else {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_grid_view_picture, parent, false);
                holder = new ViewHolder();
                holder.view = convertView.findViewById(R.id.view);
                holder.pickImage = convertView.findViewById(R.id.iv_pick_image_from_album);
                holder.deleteImage = convertView.findViewById(R.id.iv_delete_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 从路径中加载图片
            Glide.with(mContext).load(data.get(position)).into(holder.pickImage);
            // 删除图片的监听
            holder.deleteImage.setOnClickListener(v -> {
                // 对要删除的图片做缩放动画
                holder.view.startAnimation(scaleAnimation);
                // 监听动画结束删除
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // 移除选中图片
                        data.remove(data.get(position));
                        notifyDataSetChanged();
                    }
                    
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            });
            // 浏览已选图片的监听
            holder.pickImage.setOnClickListener(v -> {
                // 启动画廊
                Intent intentGallery = new Intent(mContext, GalleryActivity.class);
                // 传递所有已选图片数据
                intentGallery.putStringArrayListExtra(KEY_ALL_PICK_IMAGE, (ArrayList<String>) data);
                // 传递点击位置
                intentGallery.putExtra(KEY_PICK_POSITION, position);
                mContext.startActivity(intentGallery);
            });
            return convertView;
        }
    }
    
    class ViewHolder {
        ConstraintLayout view;
        ImageView pickImage;
        ImageView deleteImage;
    }
    
}
