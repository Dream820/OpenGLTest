package com.comet.myopengl.dice;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.comet.myopengl.MyApplication;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Myrender implements GLSurfaceView.Renderer {
    Background mBackground;
    //关于摄像机的变量
    float cx = 0;//摄像机x位置
    float cy = 0;//摄像机y位置
    float cz = -50;//摄像机z位置

    float tx = 0;//目标点x位置
    float ty = 0;//目标点y位置
    float tz = 0;//目标点z位置

    float lx = 0;//x位置
    float ly = 0;//y位置
    float lz = 0;//z位置

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        MatrixState.setInitStack();
        mBackground = new Background(MyApplication.myApplication);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        //计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("onDrawFrameee", "11111");
        //清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        //设置camera位置,在上面往下面看
        MatrixState.setCamera
                (
                        cx,   //人眼位置的X
                        cy,    //人眼位置的Y
                        cz,   //人眼位置的Z
                        tx,    //人眼球看的点X
                        ty,   //人眼球看的点Y
                        tz,   //人眼球看的点Z
                        0,    //up位置
                        1,
                        0
                );
        //初始化光源位置
        MatrixState.setLightLocation(lx, ly, lz);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glCullFace(GLES20.GL_BACK);

        MatrixState.pushMatrix();
        mBackground.drawSelf();
        MatrixState.popMatrix();
    }
}
