package com.comet.myopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jinyalin
 * @since 2017/7/24.
 */

public class AdvanceRenderer implements GLSurfaceView.Renderer {
    // Proved to be good for normal rotation
    /* Rotation speed values */
    private static final float xspeed = 0.5f; // X Rotation Speed
    private static final float yspeed = 0.5f; // Y Rotation Speed

    private Cube2 cube;
    private Context context;

    public float yrot; // Y Rotation

    private float oldX;
    private float oldY;


    public AdvanceRenderer(Context context) {
        this.cube = new Cube2();
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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
        cube.loadGLTexture(gl, context);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {
            height = 1;
        }
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
        gl.glLoadIdentity(); // Reset The Modelview Matrix

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f,
                0.4f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1, 0f);
        gl.glEnable(GL10.GL_DEPTH_TEST); // 开启时时只绘制前面的一层

        gl.glScalef(0.2f, 0.2f, 0.2f);

        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // Y
        cube.draw(gl, 0);
    }
}
