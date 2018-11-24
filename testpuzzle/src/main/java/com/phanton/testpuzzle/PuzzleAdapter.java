package com.phanton.testpuzzle;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phanton.testpuzzle.layout.slant.NumberSlantLayout;
import com.phanton.testpuzzle.layout.straight.NumberStraightLayout;
import com.xiaopo.flying.photolayout.R;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.SquarePuzzleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wupanjie
 */
public class PuzzleAdapter extends RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder> {
    
    private List<PuzzleLayout> layoutData = new ArrayList<>();
    private List<Bitmap> bitmapData = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    
    @NonNull
    @Override
    public PuzzleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle, parent, false);
        return new PuzzleViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PuzzleViewHolder holder, int position) {
        final PuzzleLayout puzzleLayout = layoutData.get(position);
        
        holder.puzzleView.setNeedDrawLine(true);
        holder.puzzleView.setNeedDrawOuterLine(true);
        holder.puzzleView.setTouchEnable(false);
        
        holder.puzzleView.setPuzzleLayout(puzzleLayout);
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int theme = 0;
                    if (puzzleLayout instanceof NumberSlantLayout) {
                        theme = ((NumberSlantLayout) puzzleLayout).getTheme();
                    } else if (puzzleLayout instanceof NumberStraightLayout) {
                        theme = ((NumberStraightLayout) puzzleLayout).getTheme();
                    }
                    onItemClickListener.onItemClick(puzzleLayout, theme);
                }
            }
        });
        
        if (bitmapData == null) {
            return;
        }
        
        final int bitmapSize = bitmapData.size();
        
        if (puzzleLayout.getAreaCount() > bitmapSize) {
            for (int i = 0; i < puzzleLayout.getAreaCount(); i++) {
                holder.puzzleView.addPiece(bitmapData.get(i % bitmapSize));
            }
        } else {
            holder.puzzleView.addPieces(bitmapData);
        }
    }
    
    @Override
    public int getItemCount() {
        return layoutData == null ? 0 : layoutData.size();
    }
    
    public void refreshData(List<PuzzleLayout> layoutData, List<Bitmap> bitmapData) {
        this.layoutData = layoutData;
        this.bitmapData = bitmapData;
        
        notifyDataSetChanged();
    }
    
    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    
    static class PuzzleViewHolder extends RecyclerView.ViewHolder {
        
        SquarePuzzleView puzzleView;
        
        PuzzleViewHolder(View itemView) {
            super(itemView);
            puzzleView = itemView.findViewById(R.id.puzzle);
        }
    }
    
    public interface OnItemClickListener {
        void onItemClick(PuzzleLayout puzzleLayout, int themeId);
    }
}
