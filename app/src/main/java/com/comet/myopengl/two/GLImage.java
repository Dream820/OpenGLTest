package com.comet.myopengl.two;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.comet.myopengl.R;

public class GLImage {
    public static Bitmap iBitmap;
    public static Bitmap jBitmap;
    public static Bitmap kBitmap;
    public static Bitmap lBitmap;
    public static Bitmap mBitmap;
    public static Bitmap nBitmap;
    public static Bitmap close_Bitmap;


    public static void load(Resources resources) {
        iBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        jBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        kBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        lBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        nBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
        close_Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
    }
}  