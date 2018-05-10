package com.comet.myopengl.simpleone;

import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.comet.myopengl.zero.GLImage;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleRender implements GLSurfaceView.Renderer {

    private float picBufferLenth = 0.1f;
    private float picUpY = 1f;
    private float vertices[] = {
            //front
            -1.0f + picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//left_top
            -1.0f + picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),// x y z left_bottom
            1.0f - picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//right_top
            1.0f - picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),//right_bottom
    };

    private int[] textures = new int[6];

    private float texture[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    private byte indices0[] = {
            0, 1, 3, 0, 3, 2
    };

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_LIGHT0); // Enable Light 0
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Set The Blending Function For Translucency ( NEW )
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1f); // Full Brightness. 50% Alpha ( NEW )

        gl.glDisable(GL10.GL_DITHER); // Disable dithering
        gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping
        gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading

        gl.glClearDepthf(1.0f); // Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        IntBuffer textureBufferTmp = IntBuffer.allocate(6);
        gl.glGenTextures(6, textureBufferTmp);
        textures = textureBufferTmp.array();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap1, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
