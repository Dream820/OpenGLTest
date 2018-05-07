package com.comet.myopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
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
    private float TOUCH_SCALE = 0.2f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - oldX;
            if (y >= 0) {
                Log.d("advanceRendererrr", advanceRenderer.yrot + "");
                advanceRenderer.yrot += dx * TOUCH_SCALE;
            }
            oldX = x;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 抬起的时候 查看角度 重新复制

            Log.d("ACTION_UPACTION_UP", "ACTION_UP");
        }
        return true;
    }
}
