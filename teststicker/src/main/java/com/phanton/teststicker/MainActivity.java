package com.phanton.teststicker;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

/**
 * @author FairHand
 */
public class MainActivity extends AppCompatActivity {
    
    StickerView stickerView;
    Button button;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stickerView = findViewById(R.id.sticker_view);
        button = findViewById(R.id.btn);
        
        final Drawable drawable = ContextCompat.getDrawable(this, R.drawable.iv_secret);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stickerView.addSticker(new DrawableSticker(drawable));
                stickerView.addSticker(
                        new TextSticker(getApplicationContext())
                                .setText("哈哈")
                                .setMaxTextSize(24)
                                .resizeText()
                        , Sticker.Position.CENTER);
            }
        });
        
    }
    
}
