package com.fairhand.supernotepad.puzzle.affix.util;

/**
 * 选择贴纸回调接口
 *
 * @author Phanton
 * @date 11/22/2018 - Thursday - 10:29 PM
 */
public interface PickStickerWhichCallBack {
    
    /**
     * 隐藏贴纸面板
     */
    void onCancel();
    
    /**
     * 猫贴纸
     */
    void onPickStickerCat();
    
    /**
     * 狗贴纸
     */
    void onPickStickerDog();
    
    /**
     * 表情贴纸
     */
    void onPickStickerEmoji();
    
    /**
     * 兔子贴纸
     */
    void onPickStickerRabbit();
    
    /**
     * 鱼贴纸
     */
    void onPickStickerFish();
    
}
