package com.fairhand.supernotepad.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.entity.Font;
import com.fairhand.supernotepad.view.DiyFontDialog;

import java.util.List;

/**
 * 字体适配器
 *
 * @author Phanton
 * @date 11/23/2018 - Friday - 11:34 AM
 */
public class FontAdapter extends BaseAdapter {
    
    private Context context;
    private List<Font> data;
    
    public FontAdapter(List<Font> data, Context context) {
        this.data = data;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view_font, parent, false);
            holder.fontChoose = convertView.findViewById(R.id.iv_font_typeface_choose);
            holder.fontTypeface = convertView.findViewById(R.id.tv_font_typeface);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Typeface typeface = data.get(position).getTypeface();
        if (typeface != null) {
            holder.fontTypeface.setTypeface(typeface);
        }
        holder.fontTypeface.setText(data.get(position).getText());
        if (position == DiyFontDialog.currentFont) {
            holder.fontChoose.setVisibility(View.VISIBLE);
        } else {
            holder.fontChoose.setVisibility(View.INVISIBLE);
        }
        
        return convertView;
    }
    
    static class ViewHolder {
        ImageView fontChoose;
        TextView fontTypeface;
    }
    
}
