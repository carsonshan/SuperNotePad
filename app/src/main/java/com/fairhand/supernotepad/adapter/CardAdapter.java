package com.fairhand.supernotepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.entity.Card;
import com.fairhand.supernotepad.R;

import java.util.List;

/**
 * 顶部卡片GridView的适配器类
 *
 * @author FairHand
 * @date 2018/10/30
 */
public class CardAdapter extends BaseAdapter {
    
    private Context mContext;
    private List<Card> cards;
    
    public CardAdapter(Context context, List<Card> cards) {
        mContext = context;
        this.cards = cards;
    }
    
    @Override
    public int getCount() {
        return cards.size();
    }
    
    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view_card, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.iv_image);
            holder.name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setImageResource(cards.get(position).getImageId());
        holder.name.setText(cards.get(position).getName());
        return convertView;
    }
    
    class ViewHolder {
        ImageView image;
        TextView name;
    }
    
}
