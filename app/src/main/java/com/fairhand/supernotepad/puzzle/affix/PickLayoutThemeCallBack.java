package com.fairhand.supernotepad.puzzle.affix;

/**
 * 选择拼图布局主题回调接口
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 9:57 PM
 */
public interface PickLayoutThemeCallBack {
    
    /**
     * 拼图
     *
     * @param type    布局类型
     * @param themeId 布局主题
     */
    void template(int type, int themeId);
    
}
