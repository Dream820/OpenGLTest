package com.comet.myopengl.tietu;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {
    private static final String TAG = MyGLSurfaceView.class.getSimpleName();
    private final float TOUCH_SCALE_FACTOR = 0.12f;
    private float mPreviousX=0;
    private float mPreviousY=0;
    MyRenderer lessonOneRenderer;
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        //设置透明背景
        this.setZOrderOnTop(true);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        lessonOneRenderer=new MyRenderer(context);
        this.setRenderer(lessonOneRenderer);
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float roateAngle=0;

        switch (event.getAction()) {
//            case  MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;

                roateAngle=lessonOneRenderer.yAngle+((dx + 0) * TOUCH_SCALE_FACTOR);
                roateAngle%=360;
                roateAngle=roateAngle>0?roateAngle:roateAngle+360;
                lessonOneRenderer.yAngle=roateAngle;
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                roateAngle=lessonOneRenderer.yAngle;
                if ((roateAngle>=0&&roateAngle<=30)||(roateAngle>330&&roateAngle<=360)) {
                    roateAngle=0;
                }else if (roateAngle>30&&roateAngle<=90){
                    roateAngle=60;
                }else if (roateAngle>90&&roateAngle<=150){
                    roateAngle=120;
                }else if (roateAngle>150&&roateAngle<=210){
                    roateAngle=180;
                }else if (roateAngle>210&&roateAngle<=270){
                    roateAngle=240;
                }else if (roateAngle>270&&roateAngle<=330){
                    roateAngle=300;
                }
                lessonOneRenderer.yAngle=roateAngle;
                break;
        }

        Log.i(TAG, "onTouchEvent: roateAngle= "+roateAngle);
        Log.i(TAG, "onTouchEvent: mPreviousX= "+mPreviousX);
        mPreviousX = x;
        return true;
    }
}
