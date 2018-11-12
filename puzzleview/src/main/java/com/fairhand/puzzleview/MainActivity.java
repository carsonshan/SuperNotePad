package com.fairhand.puzzleview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * @author FairHand
 */
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        // 拼接图的宽度
        float mWidth = metrics.widthPixels;
        
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.fd));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.sa));
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ddf));
        
        ConnectImageView imageView = findViewById(R.id.civ);
        imageView.mergeBitmap(bitmaps, mWidth);
    }
    
}
