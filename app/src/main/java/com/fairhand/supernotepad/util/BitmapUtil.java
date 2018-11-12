package com.fairhand.supernotepad.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.text.TextUtils;

/**
 * Bitmap工具类，获取Bitmap对象
 *
 * @author FairHand
 * @date 2018/10/20
 */
public class BitmapUtil {
    
    private BitmapUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    /**
     * 根据资源id获取指定大小的Bitmap对象
     *
     * @param context 应用程序上下文
     * @param id      资源id
     * @param height  高度
     * @param width   宽度
     */
    public static Bitmap getBitmapFromResource(Context context, int id, int height, int width) {
        Options options = new Options();
        // 只读取图片，不加载到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id, options);
        options.inSampleSize = calculateSampleSize(height, width, options);
        //加载到内存中
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }
    
    /**
     * 根据文件路径获取指定大小的Bitmap对象
     *
     * @param path   文件路径
     * @param height 高度
     * @param width  宽度
     */
    public static Bitmap getBitmapFromFile(String path, int height, int width) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("参数为空，请检查你选择的路径:" + path);
        }
        Options options = new Options();
        // 只读取图片，不加载到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateSampleSize(height, width, options);
        // 加载到内存中
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    
    /**
     * 获取指定大小的Bitmap对象
     *
     * @param bitmap Bitmap对象
     * @param height 高度
     * @param width  宽度
     */
    public static Bitmap getThumbnailsBitmap(Bitmap bitmap, int height, int width) {
        if (bitmap == null) {
            throw new IllegalArgumentException("图片为空，请检查你的参数");
        }
        return ThumbnailUtils.extractThumbnail(bitmap, width, height);
    }
    
    /**
     * 将Bitmap对象转换成Drawable对象
     *
     * @param context 应用程序上下文
     * @param bitmap  Bitmap对象
     * @return 返回转换后的Drawable对象
     */
    public static Drawable bitmap2Drawable(Context context, Bitmap bitmap) {
        if (context == null || bitmap == null) {
            throw new IllegalArgumentException("参数不合法，请检查你的参数");
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }
    
    /**
     * 将Drawable对象转换成Bitmap对象
     *
     * @param drawable Drawable对象
     * @return 返回转换后的Bitmap对象
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable为空，请检查你的参数");
        }
        Bitmap bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() !=
                                PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    /**
     * 将Bitmap对象转换为byte[]数组
     *
     * @param bitmap Bitmap对象
     * @return 返回转换后的数组
     */
    public static byte[] bitmapToByte(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap为空，请检查你的参数");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    
    /**
     * 计算所需图片的缩放比例
     *
     * @param height  高度
     * @param width   宽度
     * @param options options选项
     */
    private static int calculateSampleSize(int height, int width, Options options) {
        int realHeight = options.outHeight;
        int realWidth = options.outWidth;
        int heightScale = realHeight / height;
        int widthScale = realWidth / width;
        if (widthScale > heightScale) {
            return widthScale;
        } else {
            return heightScale;
        }
    }
    
    /**
     * 将Bitmap以指定格式保存到指定路径
     *
     * @param bitmap 要保存的bitmap
     * @param file   保存到的位置
     * @param format 压缩格式
     */
    public static void saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
        FileOutputStream out;
        try {
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据 路径 得到 file 得到 bitmap
     */
    public static Bitmap decodeFile(String filePath) throws IOException {
        Bitmap b;
        int imageMaxSize = 600;
        
        File f = new File(filePath);
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        
        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();
        
        int scale = 1;
        if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
            scale = (int) Math.pow(2,
                    (int) Math.round(Math.log(imageMaxSize / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
        
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }
    
    /**
     * 获取视频缩略图
     *
     * @param videoPath 视频地址
     */
    public static Bitmap getVideoThumb(final String videoPath) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        if (videoPath != null) {
            mediaMetadataRetriever.setDataSource(videoPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                return mediaMetadataRetriever.getFrameAtTime();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
}
