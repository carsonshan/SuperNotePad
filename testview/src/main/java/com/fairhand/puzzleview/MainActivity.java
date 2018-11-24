package com.fairhand.puzzleview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

/**
 * @author FairHand
 */
public class MainActivity extends AppCompatActivity {
    
    Bitmap bitmap;
    
    GPUImageView gpuimageview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        gpuimageview = findViewById(R.id.image);
        Button first = findViewById(R.id.first);
        Button second = findViewById(R.id.second);
        Button third = findViewById(R.id.third);
        Button forth = findViewById(R.id.forth);
        Button fifth = findViewById(R.id.fifth);
        Button button = findViewById(R.id.button2);
        
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_photo_filter_sample);
        gpuimageview.setImage(bitmap);
        
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 黑白
                gpuimageview.setFilter(new GPUImageGrayscaleFilter());
            }
        });
        
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 素描
                gpuimageview.setFilter(new GPUImageSketchFilter());
            }
        });
        
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 像素画
                gpuimageview.setFilter(new GPUImagePixelationFilter());
            }
        });
        
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 卡通
                gpuimageview.setFilter(new GPUImageToonFilter());
            }
        });
        
        fifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 水粉画模糊
                gpuimageview.setFilter(new GPUImageKuwaharaFilter());
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpuimageview.setFilter(new GPUImageMonochromeFilter());
            }
        });
        
    }
    
}
