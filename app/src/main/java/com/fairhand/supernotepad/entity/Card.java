package com.fairhand.supernotepad.entity;

/**
 * 顶部卡片布局
 *
 * @author FairHand
 * @date 2018/10/30
 */
public class Card {
    
    private String name;
    
    private int imageId;
    
    public Card(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }
    
    public String getName() {
        return name;
    }
    
    public int getImageId() {
        return imageId;
    }
    
}
