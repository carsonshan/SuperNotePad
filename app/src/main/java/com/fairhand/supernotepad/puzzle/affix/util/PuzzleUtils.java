package com.fairhand.supernotepad.puzzle.affix.util;

import com.fairhand.supernotepad.puzzle.affix.slant.ThreeSlantLayoutAbstract;
import com.fairhand.supernotepad.puzzle.affix.slant.TwoSlantLayoutAbstract;
import com.fairhand.supernotepad.puzzle.affix.straight.FiveStraightLayout;
import com.fairhand.supernotepad.puzzle.affix.straight.FourStraightLayout;
import com.fairhand.supernotepad.puzzle.affix.straight.ThreeStraightLayoutAbstract;
import com.fairhand.supernotepad.puzzle.affix.straight.TwoStraightLayout;
import com.xiaopo.flying.puzzle.PuzzleLayout;

/**
 * 拼图工具类
 *
 * @author wupanjie
 */
public class PuzzleUtils {
    
    private PuzzleUtils() {
        //no instance
    }
    
    /**
     * 获取拼图布局
     *
     * @param type      样式   0斜线 1直线
     * @param pieceSize 图片张数
     * @param themeId   布局主题
     */
    public static PuzzleLayout getPuzzleLayout(int type, int pieceSize, int themeId) {
        if (type == 0) {
            switch (pieceSize) {
                case 2:
                    return new TwoSlantLayoutAbstract(themeId);
                case 3:
                    return new ThreeSlantLayoutAbstract(themeId);
                default:
                    return new TwoSlantLayoutAbstract(themeId);
            }
        } else {
            switch (pieceSize) {
                case 2:
                    return new TwoStraightLayout(1f / 2, themeId);
                case 3:
                    return new ThreeStraightLayoutAbstract(themeId);
                case 4:
                    return new FourStraightLayout(themeId);
                case 5:
                    return new FiveStraightLayout(themeId);
                default:
                    return new TwoStraightLayout(1f / 2, themeId);
            }
        }
    }
    
}
