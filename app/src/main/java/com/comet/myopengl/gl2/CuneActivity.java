package com.comet.myopengl.gl2;

import android.app.Activity;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.comet.myopengl.R;
import com.comet.myopengl.zero.GLImage;

public class CuneActivity extends Activity {

    float[] first = new float[]{
            1, 1, 1, 1,
            2, 2, 2, 2,
            3, 3, 3, 3,
            4, 4, 4, 4,
    };
    float[] second = new float[]{
            1, 2, 3, 4,
            1, 2, 3, 4,
            1, 2, 3, 4,
            1, 2, 3, 4,
    };
    float[] third = new float[]{
            1, 2, 3, 4,
            1, 2, 3, 4,
            1, 2, 3, 4,
            1, 2, 3, 4,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maincobine);


        Matrix.multiplyMM(first, 0, second, 0, third, 0);
        Log.d("multiplyMMff", "first|||||");
        for (int i = 0, lenth = first.length; i < lenth; i++) {
            Log.d("multiplyMMff", first[i] + "");
        }
        Log.d("multiplyMMff", ">>>");

        Log.d("multiplyMMff", "second|||||");
        for (int i = 0, lenth = second.length; i < lenth; i++) {
            Log.d("multiplyMMff", second[i] + "");
        }
        Log.d("multiplyMMff", "third|||||");
        for (int i = 0, lenth = third.length; i < lenth; i++) {
            Log.d("multiplyMMff", third[i] + "");
        }
        System.arraycopy(first, 0, third, 0, 16);
        Log.d("multiplyMMff", "###");
        for (int i = 0, lenth = third.length; i < lenth; i++) {
            Log.d("multiplyMMff", third[i] + "");
        }
        Log.d("multiplyMMff", "@@@");
    }
} 