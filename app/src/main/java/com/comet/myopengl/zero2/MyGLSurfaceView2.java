package com.comet.myopengl.zero2;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView2 extends GLSurfaceView {
    private AdvanceRenderer2 advanceRenderer;

    public MyGLSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setEGLContextClientVersion(1);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        advanceRenderer = new AdvanceRenderer2();
        setRenderer(advanceRenderer);
    }


    private float oldX;
    private float TOUCH_SCALE = 0.2f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!advanceRenderer.isCorrecting) {
            advanceRenderer.isCorrecting = true;
            float x = event.getX();
            float y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                float dx = x - oldX;
                if (y >= 0) {
                    advanceRenderer.yrot += dx * TOUCH_SCALE;
                }
                oldX = x;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("onTouchEventtt", "ACTION_UP");
                //抬起的时候需要计算 接近那个点
                float ramain = advanceRenderer.yrot % 60;
                if (ramain >= 0 && ramain < 30) {
                    advanceRenderer.plusOrMinus = -1;
                    advanceRenderer.time = (int) (ramain / 3f) + 1;
                } else if (ramain >= 30 && ramain <= 60f) {
                    advanceRenderer.plusOrMinus = 1;
                    advanceRenderer.time = (int) ((60f - ramain) / 3f) + 1;
                } else if (ramain <= 0 && ramain > -30) {
                    advanceRenderer.plusOrMinus = -1;
                    advanceRenderer.time = (int) (ramain / 3f) + 1;
                } else if (ramain <= -30 && ramain >= -60) {
                    advanceRenderer.plusOrMinus = 1;
                    advanceRenderer.time = (int) (-60f - ramain) / 3 + 1;
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d("onTouchEventtt", "ACTION_DOWN");
                advanceRenderer.animatorStatus = 4;
                oldX = x;
            }
        }
        return true;
    }

    public void changeAnimatorStatus(int a) {
        advanceRenderer.animatorStatus = a;
    }
}
