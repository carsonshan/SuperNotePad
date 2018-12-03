package com.fairhand.supernotepad.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.GalleryActivity;

import java.util.ArrayList;
import java.util.List;

import static com.fairhand.supernotepad.adapter.PictureAdapter.KEY_ALL_PICK_IMAGE;
import static com.fairhand.supernotepad.adapter.PictureAdapter.KEY_PICK_POSITION;

/**
 * @author Phanton
 * @date 12/1/2018 - Saturday - 5:42 PM
 */
public class DiyObservePictureDialog extends BaseDialog {
    
    TextView tvTitle;
    TextView tvContent;
    RecyclerView recyclerViewPicture;
    
    private Context context;
    
    private String title;
    private String content;
    private List<String> data;
    
    public DiyObservePictureDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    
    @Override
    protected int setView() {
        return R.layout.dialog_diy_observe_picture;
    }
    
    @Override
    protected void initData() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        recyclerViewPicture = findViewById(R.id.recyclerView_picture);
        
        tvTitle.setText(title);
        tvContent.setText(content);
        
        recyclerViewPicture.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewPicture.setAdapter(new Adapter(data, context));
    }
    
    public DiyObservePictureDialog setTvTitle(String title) {
        this.title = title;
        return this;
    }
    
    public DiyObservePictureDialog setContent(String content) {
        this.content = content;
        return this;
    }
    
    public DiyObservePictureDialog setPictureData(List<String> data) {
        this.data = data;
        return this;
    }
    
    static class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        List<String> data;
        Context context;
        
        Adapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup container, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_picture, container, false);
            ViewHolder holder = new ViewHolder(itemView);
            holder.materialCardView.setOnClickListener(v -> {
                // 启动画廊
                Intent intentGallery = new Intent(context, GalleryActivity.class);
                // 传递所有已选图片数据
                intentGallery.putStringArrayListExtra(KEY_ALL_PICK_IMAGE, (ArrayList<String>) data);
                // 传递点击位置
                intentGallery.putExtra(KEY_PICK_POSITION, holder.getAdapterPosition());
                context.startActivity(intentGallery);
            });
            return holder;
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context).load(data.get(position)).into(holder.picture);
        }
        
        @Override
        public int getItemCount() {
            return data.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            MaterialCardView materialCardView;
            ImageView picture;
            
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                materialCardView = itemView.findViewById(R.id.material_card_view);
                picture = itemView.findViewById(R.id.iv_picture);
            }
        }
    }
    
}
