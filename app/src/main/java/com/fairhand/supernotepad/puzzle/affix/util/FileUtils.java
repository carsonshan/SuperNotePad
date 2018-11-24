package com.fairhand.supernotepad.puzzle.affix.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.fairhand.supernotepad.puzzle.affix.PhotoSaveCallback;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.TimeUtil;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 *
 * @author Phanton
 * @date 11/21/2018 - Wednesday - 7:05 PM
 */
public class FileUtils implements PreferenceManager.OnActivityDestroyListener {
    
    private static String getFolderName(String name) {
        File mediaStorageDir =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name);
        
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return "";
            }
        }
        return mediaStorageDir.getAbsolutePath();
    }
    
    public static File getNewFile(String folderName) {
        String timeStamp = TimeUtil.getFormatTime();
        String path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return new File(path);
    }
    
    private static Bitmap createBitmap(PuzzleView puzzleView) {
        puzzleView.clearHandling();
        puzzleView.invalidate();
        
        Bitmap bitmap = Bitmap.createBitmap(puzzleView.getWidth(), puzzleView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        puzzleView.draw(canvas);
        
        return bitmap;
    }
    
    private static Bitmap bitmap = null;
    
    /**
     * 保存图片
     */
    public static Bitmap savePuzzle(PuzzleView puzzleView, File file, int quality, PhotoSaveCallback photoSaveCallback) {
        FileOutputStream outputStream = null;
        
        try {
            bitmap = createBitmap(puzzleView);
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            
            if (!file.exists()) {
                Logger.d("notifySystemGallery: the file do not exist.");
                return null;
            }
            
            try {
                MediaStore.Images.Media.insertImage(puzzleView.getContext().getContentResolver(),
                        file.getAbsolutePath(), file.getName(), null);
                Logger.d("222路径：" + file.getAbsolutePath());
                Logger.d("222名字：" + file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            
            // 通知图库刷新
            puzzleView.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            
            if (photoSaveCallback != null) {
                photoSaveCallback.onSuccess();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            if (photoSaveCallback != null) {
                photoSaveCallback.onFailed();
            }
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
    
    @Override
    public void onActivityDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
    
}
