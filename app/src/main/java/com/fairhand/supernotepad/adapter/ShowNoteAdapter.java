package com.fairhand.supernotepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.Note;

import java.util.List;

/**
 * 主界面展示所以已保存记事的适配器类
 *
 * @author FairHand
 * @date 2018/11/3
 */
public class ShowNoteAdapter extends BaseAdapter {
    
    private Context mContext;
    
    /**
     * 数据源
     */
    private List<Note> data;
    
    public ShowNoteAdapter(Context context, List<Note> data) {
        mContext = context;
        this.data = data;
    }
    
    /**
     * 更新并刷新数据
     */
    public void updateData(List<Note> data) {
        this.data = data;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return data.size();
    }
    
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view_note, parent, false);
            holder.noteImage = convertView.findViewById(R.id.iv_note_image);
            holder.noteTitle = convertView.findViewById(R.id.tv_note_title);
            holder.noteTime = convertView.findViewById(R.id.iv_note_time);
            holder.playVideo = convertView.findViewById(R.id.iv_play);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        // 获取记事的类型
        Note note = data.get(position);
        int key = note.getKey();
        // 加载数据
        Glide.with(mContext)
                .load(key == Config.TYPE_COMMON || key == Config.TYPE_RECORDING ?
                              note.getNoteImageId() : note.getNoteImagePath())
                .apply(RequestOptions.bitmapTransform(
                        new RoundedCorners(6)))
                .into(holder.noteImage);
        holder.noteTime.setText(note.getNoteTime());
        holder.noteTitle.setText(note.getNoteTitle());
        if (key == Config.TYPE_VIDEO) {
            holder.playVideo.setVisibility(View.VISIBLE);
        } else {
            holder.playVideo.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    class ViewHolder {
        ImageView noteImage;
        TextView noteTitle;
        TextView noteTime;
        ImageView playVideo;
    }
    
}
