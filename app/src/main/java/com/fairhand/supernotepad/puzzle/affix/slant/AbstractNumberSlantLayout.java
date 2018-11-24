package com.fairhand.supernotepad.puzzle.affix.slant;

import android.util.Log;

import com.xiaopo.flying.puzzle.slant.SlantPuzzleLayout;

/**
 * @author wupanjie
 */

public abstract class AbstractNumberSlantLayout extends SlantPuzzleLayout {
    
    private static final String TAG = "NumberSlantLayout";
    protected int theme;
    
    AbstractNumberSlantLayout(int theme) {
        if (theme >= getThemeCount()) {
            Log.e(TAG, "AbstractNumberSlantLayout: the most theme count is "
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
