package com.comet.myopengl.four;

import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.KeyEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements GLSurfaceView.Renderer {
    float xrot = 0.0f;
    float yrot = 0.0f;
    float z = -5.0f;
    int one = 65536;

    //光线参数
    FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
    FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
    FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{0.0f, 0.0f, 2.0f, 1.0f});

    int[] texture;

    private float picBufferLenth = 0.1f;
    private float picBufferX = picBufferLenth / 2;
    private float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);

    private float vertices1[] = {
            //front
            -1.0f + picBufferLenth, 0.614f, (float) Math.sqrt(3),//left_top
            -1.0f + picBufferLenth, -0.614f, (float) Math.sqrt(3),// x y z left_bottom
            1.0f - picBufferLenth, 0.614f, (float) Math.sqrt(3),//right_top
            1.0f - picBufferLenth, -0.614f, (float) Math.sqrt(3),//right_bottom

            //right roar
            2 - picBufferX, 0.614f, 0 - picBufferZ,
            2 - picBufferX, -0.614f, 0 - picBufferZ,
            1.0f + picBufferX, 0.614f, -(float) Math.sqrt(3) + picBufferZ,
            1.0f + picBufferX, -0.614f, -(float) Math.sqrt(3) + picBufferZ,

            //roar
            -1.0f + picBufferLenth, 0.614f, -(float) Math.sqrt(3),
            -1.0f + picBufferLenth, -0.614f, -(float) Math.sqrt(3),
            1.0f - picBufferLenth, 0.614f, -(float) Math.sqrt(3),
            1.0f - picBufferLenth, -0.614f, -(float) Math.sqrt(3),

            //left front
            -2 + picBufferX, 0.614f, 0 + picBufferZ,
            -2 + picBufferX, -0.614f, 0 + picBufferZ,
            -1.0f - picBufferX, 0.614f, (float) Math.sqrt(3) - picBufferZ,
            -1.0f - picBufferX, -0.614f, (float) Math.sqrt(3) - picBufferZ,

            //left roar
            -1.0f - picBufferX, 0.614f, -(float) Math.sqrt(3) + picBufferZ,
            -1.0f - picBufferX, -0.614f, -(float) Math.sqrt(3) + picBufferZ,
            -2 + picBufferX, 0.614f, 0 - picBufferZ,
            -2 + picBufferX, -0.614f, 0 - picBufferZ,

            //right front
            1.0f + picBufferX, 0.614f, (float) Math.sqrt(3) - picBufferZ,
            1.0f + picBufferX, -0.614f, (float) Math.sqrt(3) - picBufferZ,
            2.0f - picBufferX, 0.614f, 0 + picBufferZ,
            2.0f - picBufferX, -0.614f, 0 + picBufferZ,
    };

    int[] normal = new int[]{
            0, 0, one,
            0, 0, one,
            0, 0, one,
            0, 0, one,

            0, 0, one,
            0, 0, one,
            0, 0, one,
            0, 0, one,

            0, one, 0,
            0, one, 0,
            0, one, 0,
            0, one, 0,

            0, -one, 0,
            0, -one, 0,
            0, -one, 0,
            0, -one, 0,

            one, 0, 0,
            one, 0, 0,
            one, 0, 0,
            one, 0, 0,

            -one, 0, 0,
            -one, 0, 0,
            -one, 0, 0,
            -one, 0, 0,
    };
    IntBuffer normals = GLTool.getIntBu(normal);

    ByteBuffer indices1 = ByteBuffer.wrap(new byte[]{
            0, 1, 3, 2,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
    });
    ByteBuffer indices2 = ByteBuffer.wrap(new byte[]{
            0, 0, 0, 0,
            4, 5, 7, 6,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
    });
    ByteBuffer indices3 = ByteBuffer.wrap(new byte[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
            8, 9, 11, 10,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
    });
    ByteBuffer indices4 = ByteBuffer.wrap(new byte[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            12, 13, 15, 14,
            0, 0, 0, 0,
            0, 0, 0, 0,
    });
    ByteBuffer indices5 = ByteBuffer.wrap(new byte[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            16, 17, 19, 18,
            0, 0, 0, 0,
    });
    ByteBuffer indices6 = ByteBuffer.wrap(new byte[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            20, 21, 23, 22,
    });

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // 重置当前的模型观察矩阵
        gl.glLoadIdentity();

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glTranslatef(0.0f, 0.0f, z);

        //设置旋转
        gl.glRotatef(xrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(yrot, 1.0f, 0.0f, 0.0f);

        gl.glNormalPointer(GL10.GL_FIXED, 0, normals);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //绘制四边形
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices1);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 8, GL10.GL_UNSIGNED_BYTE, indices2);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 12, GL10.GL_UNSIGNED_BYTE, indices3);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 16, GL10.GL_UNSIGNED_BYTE, indices4);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 20, GL10.GL_UNSIGNED_BYTE, indices5);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24, GL10.GL_UNSIGNED_BYTE, indices6);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        //修改旋转角度
//        xrot += 0.3f;
//        yrot += 0.2f;

        gl.glEnable(GL10.GL_DEPTH_TEST);    // 打开深度测试
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) width / height;
        //设置OpenGL场景的大小
        gl.glViewport(0, 0, width, height);
        //设置投影矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        //重置投影矩阵
        gl.glLoadIdentity();
        // 设置视口的大小
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        // 选择模型观察矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // 重置模型观察矩阵
        gl.glLoadIdentity();
    }

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private float texture1[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        ByteBuffer byteBuf
                = ByteBuffer.allocateDirect(vertices1.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices1);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(texture1.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture1);
        textureBuffer.position(0);


//        gl.glDisable(GL10.GL_DITHER);
//
//        // 告诉系统对透视进行修正
//        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
//        // 黑色背景
//        gl.glClearColor(0, 0, 0, 0);
//
//        gl.glEnable(GL10.GL_CULL_FACE);
//        // 启用阴影平滑
//        gl.glShadeModel(GL10.GL_SMOOTH);
//        // 启用深度测试
//        gl.glEnable(GL10.GL_DEPTH_TEST);
//
//        //设置光线,,1.0f为全光线，a=50%
//        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
//        // 基于源象素alpha通道值的半透明混合函数
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

        //纹理相关      
        IntBuffer textureBuffer = IntBuffer.allocate(6);
        gl.glGenTextures(6, textureBuffer);
        texture = textureBuffer.array();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap1, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[1]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap2, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[2]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap3, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[3]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap4, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[4]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap5, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[5]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap6, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);


        gl.glEnable(GL10.GL_LIGHT0); // Enable Light 0

        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f); // Full Brightness. 50% Alpha ( NEW )
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Set The Blending Function For Translucency ( NEW )

        gl.glDisable(GL10.GL_DITHER); // Disable dithering
        gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping
        gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
        gl.glClearDepthf(1.0f); // Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

        // Really Nice Perspective Calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }
}