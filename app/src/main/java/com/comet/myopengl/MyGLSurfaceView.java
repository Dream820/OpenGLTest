package com.comet.myopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    AdvanceRenderer advanceRenderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        advanceRenderer = new AdvanceRenderer(context);
        setRenderer(advanceRenderer);
    }

    public MyGLSurfaceView(Context context) {
        super(context);
        advanceRenderer = new AdvanceRenderer(context);
        setRenderer(advanceRenderer);
    }

    private float oldX;
    private float oldY;
    private static final float TOUCH_SCALE = 0.2f;
    private float z = -5.0f; // Depth Into The Screen

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - oldX;
            float dy = y - oldY;
            int upperArea = 0;

            if (y < upperArea) {
                z -= dx * TOUCH_SCALE / 2;
            } else {
                advanceRenderer.yrot += dx * TOUCH_SCALE;
            }
        }
        oldX = x;
        oldY = y;
        requestRender();
        return true;
    }
}
