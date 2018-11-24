package com.fairhand.supernotepad.puzzle.affix.straight;

import android.util.Log;

import com.xiaopo.flying.puzzle.straight.StraightPuzzleLayout;

/**
 * @author wupanjie
 */
public abstract class AbstractNumberStraightLayout extends StraightPuzzleLayout {
    
    private static final String TAG = "NumberStraightLayout";
    
    protected int theme;
    
    AbstractNumberStraightLayout(int theme) {
        if (theme >= getThemeCount()) {
            Log.e(TAG, "The most theme count is "
                               + getThemeCount()
                               + " ,you should let theme from 0 to "
                               + (getThemeCount() - 1)
                               + " .");
        }
        this.theme = theme;
    }
    
    /**
     * 获取总共支持主题数量
     *
     * @return 总共支持主题数量
     */
    public abstract int getThemeCount();
    
    public int getTheme() {
        return theme;
    }
    
}
