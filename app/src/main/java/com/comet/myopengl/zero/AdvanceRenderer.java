package com.comet.myopengl.zero;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author jinyalin
 * @since 2017/7/24.
 */

public class AdvanceRenderer implements GLSurfaceView.Renderer {

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private ByteBuffer indexBuffer;
    private ByteBuffer indexBuffer1;

    private int[] textures = new int[6];

    private float picBufferLenth = 0.1f;
    private float picBufferX = picBufferLenth / 2;
    private float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);

    private float vertices[] = {

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

    private float texture[] = {
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

    private byte indices[] = {
            0, 1, 3, 0, 3, 2,
//            4, 5, 7, 4, 7, 6,
//            8, 9, 11, 8, 11, 10,
//            12, 13, 15, 12, 15, 14,
//            16, 17, 19, 16, 19, 18,
//            20, 21, 23, 20, 23, 22,
    };

    private byte indices1[] = {
//            0, 1, 3, 0, 3, 2,
            4, 5, 7, 4, 7, 6,
//            8, 9, 11, 8, 11, 10,
//            12, 13, 15, 12, 15, 14,
//            16, 17, 19, 16, 19, 18,
//            20, 21, 23, 20, 23, 22,
    };

    private Cube2 cube;
    private Context context;

    public float yrot; // Y Rotation


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


        ByteBuffer byteBuf
                = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);

        indexBuffer1 = ByteBuffer.allocateDirect(indices.length);
        indexBuffer1.put(indices1);
        indexBuffer1.position(0);
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
        Log.d("onDrawFrame", "1111");
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f,
                0.4f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1, 0f);
        gl.glEnable(GL10.GL_DEPTH_TEST); // 开启时时只绘制前面的一层

        gl.glScalef(0.2f, 0.2f, 0.2f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); // Y

//        IntBuffer textureBufferii = IntBuffer.allocate(6);
//        gl.glGenTextures(6, textureBufferii);
//        textures = textureBufferii.array();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);//设置纹理

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
//        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer1);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }
}
