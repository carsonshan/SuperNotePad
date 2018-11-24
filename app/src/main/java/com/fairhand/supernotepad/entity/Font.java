package com.fairhand.supernotepad.entity;

import android.graphics.Typeface;

/**
 * @author Phanton
 * @date 11/23/2018 - Friday - 11:36 AM
 */
public class Font {
    
    private String text;
    private Typeface typeface;
    
    public Font(String text, Typeface typeface) {
        this.text = text;
        this.typeface = typeface;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Typeface getTypeface() {
        return typeface;
    }
    
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
    
}
