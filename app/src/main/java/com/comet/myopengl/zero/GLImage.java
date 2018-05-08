package com.comet.myopengl.zero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comet.myopengl.R;

class GLImage {
    public static Bitmap mBitmap1;
    public static Bitmap mBitmap2;
    public static Bitmap mBitmap3;
    public static Bitmap mBitmap4;
    public static Bitmap mBitmap5;
    public static Bitmap mBitmap6;

    public static void load(Resources resources) {
        mBitmap1 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_5);
        mBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_4);
        mBitmap3 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_3);
        mBitmap4 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_2);
        mBitmap5 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_1);
        mBitmap6 = BitmapFactory.decodeResource(resources, R.drawable.keyboard_5);
    }
}