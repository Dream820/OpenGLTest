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

        // If a touch is moved on the screen
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Calculate the change
            float dx = x - oldX;
            float dy = y - oldY;
            // Define an upper area of 10% on the screen
            int upperArea = 0;

            // Zoom in/out if the touch move has been made in the upper
            if (y < upperArea) {
                z -= dx * TOUCH_SCALE / 2;

                // Rotate around the axis otherwise
            } else {
                advanceRenderer.yrot += dx * TOUCH_SCALE;
            }
        }

        // Remember the values
        oldX = x;
        oldY = y;

        requestRender();
        return true;
    }
}
