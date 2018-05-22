package com.comet.myopengl.tietu;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 0.1f;
    private float mPreviousX=0;
    private float mPreviousY=0;
    LessonOneRenderer lessonOneRenderer;
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        lessonOneRenderer=new LessonOneRenderer(context);
        this.setRenderer(lessonOneRenderer);
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                /*// reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }*/

                lessonOneRenderer.yAngle=lessonOneRenderer.yAngle+((dx + 0) * TOUCH_SCALE_FACTOR);
//                    renderer.setAngle(renderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
//                    Log.i(TAG, "onTouchEvent: "+mRenderer.triangle.xAngle);
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
//        return super.onTouchEvent(event);
    }
}
