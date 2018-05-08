package com.comet.myopengl.zero;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private AdvanceRenderer advanceRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        advanceRenderer = new AdvanceRenderer();
        setRenderer(advanceRenderer);
    }


    private float oldX;
    private float TOUCH_SCALE = 0.2f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Log.d("onTouchEventtt", advanceRenderer.yrot + "");
            float dx = x - oldX;
            if (y >= 0) {
                advanceRenderer.yrot += dx * TOUCH_SCALE;
            }
            oldX = x;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            oldX = x;
        }
        return true;
    }
}
