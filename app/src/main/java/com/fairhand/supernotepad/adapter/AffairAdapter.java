package com.fairhand.supernotepad.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.affair.AffairDetailActivity;
import com.fairhand.supernotepad.entity.Affair;
import com.fairhand.supernotepad.util.Logger;

import java.util.List;
import java.util.Random;

/**
 * 事务记事适配器
 *
 * @author Phanton
 * @date 11/24/2018 - Saturday - 7:30 PM
 */
public class AffairAdapter extends RecyclerView.Adapter<AffairAdapter.ViewHolder> {
    
    private Context context;
    private List<Affair> affairs;
    
    public AffairAdapter(Context context, List<Affair> affairs) {
        this.context = context;
        this.affairs = affairs;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_affair, viewGroup, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.cardViewAffair.setOnClickListener(v -> {
            Intent intent = new Intent(context, AffairDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("affair_entity", affairs.get(holder.getAdapterPosition()));
            intent.putExtras(bundle);
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                    (Activity) context,
                    new Pair<>(holder.tvAffairTitle, "transition_title"),
                    new Pair<>(holder.tvAffairTime, "transition_time"),
                    new Pair<>(holder.tvAffairBackup, "transition_backup")).toBundle());
        });
        return holder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Affair affair = affairs.get(i);
        holder.affairKind.setImageResource(affair.getKindIcon());
        holder.tvAffairTitle.setText(affair.getTitle());
        holder.tvAffairRemind.setText(affair.isRemind() ? "整点提醒" : "不提醒");
        holder.tvAffairBackup.setText(affair.getBackup() == null ? "" : affair.getBackup());
        holder.tvAffairTime.setText(affair.getTime());
        holder.cardViewAffair.setCardBackgroundColor(Color.parseColor(getRandomColor()));
    }
    
    @Override
    public int getItemCount() {
        return affairs.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView affairKind;
        TextView tvAffairTitle;
        TextView tvAffairRemind;
        TextView tvAffairBackup;
        TextView tvAffairTime;
        CardView cardViewAffair;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            affairKind = itemView.findViewById(R.id.iv_kind_icon);
            tvAffairTitle = itemView.findViewById(R.id.tv_kind_name);
            tvAffairRemind = itemView.findViewById(R.id.tv_affair_remind);
            tvAffairBackup = itemView.findViewById(R.id.tv_affair_backup);
            tvAffairTime = itemView.findViewById(R.id.tv_affair_time);
            cardViewAffair = itemView.findViewById(R.id.cardView_affair);
        }
    }
    
    /**
     * 获取随机颜色
     */
    private String getRandomColor() {
        int colorStrLength = 6;
        StringBuilder colorValue = new StringBuilder("#");
        String[] colorItem = {"A", "B", "C", "D", "E", "F", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Random random = new Random();
        for (int i = 0; i < colorStrLength; i++) {
            colorValue.append(colorItem[random.nextInt(colorItem.length)]);
        }
        Logger.d("颜色：" + colorValue);
        return colorValue.toString();
    }
    
}
