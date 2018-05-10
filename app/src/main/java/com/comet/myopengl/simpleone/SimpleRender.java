package com.comet.myopengl.simpleone;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

import com.comet.myopengl.MyApplication;
import com.comet.myopengl.R;
import com.comet.myopengl.dice.MatrixState;
import com.comet.myopengl.dice.MyGLUtils;
import com.comet.myopengl.zero.GLImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
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

    private FloatBuffer vertexBuffer;
    private FloatBuffer floatBuffer;
    private ByteBuffer indexBuffer0;
    private int mMVPMatrixHandle;
    private int mProgram;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = MyGLUtils.buildProgram(MyApplication.myApplication, R.raw.dice_bg_vertex, R.raw.dice_bg_fragment);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");

        gl.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA); // Set The Blending Function For Translucency ( NEW )
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        IntBuffer textureBufferTmp = IntBuffer.allocate(6);
        gl.glGenTextures(6, textureBufferTmp);
        textures = textureBufferTmp.array();

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap1, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        ByteBuffer byteBuf
                = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        floatBuffer = byteBuf.asFloatBuffer();
        floatBuffer.put(texture);
        floatBuffer.position(0);

        indexBuffer0 = ByteBuffer.allocateDirect(indices0.length);
        indexBuffer0.put(indices0);
        indexBuffer0.position(0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {
            height = 1;
        }
        gl.glMatrixMode(GL10.GL_PROJECTION);
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f,
                0.6f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1, 0f);
        gl.glScalef(0.2f, 0.2f, 0.2f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, floatBuffer);//设置纹理

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        gl.glDrawElements(GL10.GL_TRIANGLES, indices0.length, GL10.GL_UNSIGNED_BYTE, indexBuffer0);
    }
}
