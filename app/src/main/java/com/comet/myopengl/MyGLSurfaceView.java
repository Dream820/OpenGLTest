package com.comet.myopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private AdvanceRenderer advanceRenderer;

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
    private static final float TOUCH_SCALE = 0.2f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - oldX;
            if (y >= 0) {
                advanceRenderer.yrot += dx * TOUCH_SCALE;
            }
            oldX = x;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

        }
        requestRender();
        return true;
    }
}
