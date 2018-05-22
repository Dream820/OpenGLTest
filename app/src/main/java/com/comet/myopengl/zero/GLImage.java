package com.comet.myopengl.zero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comet.myopengl.R;

public class GLImage {
    public static Bitmap mBitmap1;
    public static Bitmap mBitmap2;
    public static Bitmap mBitmap3;
    public static Bitmap mBitmap4;
    public static Bitmap mBitmap5;
    public static Bitmap mBitmap6;
    public static Bitmap mBitmap7;

    public static void load(Resources resources) {
        mBitmap1 = loadBitmapOne(resources, R.drawable.keyboard_5);
        mBitmap2 = loadBitmapOne(resources, R.drawable.keyboard_3);
        mBitmap3 = loadBitmapOne(resources, R.drawable.keyboard_4);
        mBitmap4 = loadBitmapOne(resources, R.drawable.keyboard_7);
        mBitmap5 = loadBitmapOne(resources, R.drawable.keyboard_1);
        mBitmap6 = loadBitmapOne(resources, R.drawable.keyboard_6);
        mBitmap7 = loadBitmapOne(resources, R.drawable.keyboard_2);
    }

    public static Bitmap loadBitmapOne(Resources resources, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeResource(resources, id, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        return BitmapFactory.decodeResource(resources, id, options); // 解码文件
    }
}