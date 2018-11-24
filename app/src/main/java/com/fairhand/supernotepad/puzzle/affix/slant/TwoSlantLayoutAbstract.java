package com.fairhand.supernotepad.puzzle.affix.slant;

import com.xiaopo.flying.puzzle.Line;

/**
 * @author wupanjie
 */

public class TwoSlantLayoutAbstract extends AbstractNumberSlantLayout {
    
    public TwoSlantLayoutAbstract(int theme) {
        super(theme);
    }
    
    @Override
    public int getThemeCount() {
        return 2;
    }
    
    @Override
    public void layout() {
        switch (theme) {
            case 0:
                addLine(0, Line.Direction.HORIZONTAL, 0.56f, 0.44f);
                break;
            case 1:
                addLine(0, Line.Direction.VERTICAL, 0.56f, 0.44f);
                break;
            default:
                break;
        }
    }
    
}
