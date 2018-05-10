package com.comet.myopengl.dice;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * Created by wuchunhui on 17-2-21.
 */

public class Background {

    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mVerticesTextureBuffer;

    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinationHandle;

    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private int mTextureId;

    private int mProgram;
    private Context mContext;

    public Background(Context context) {
        mContext = context;

        initData();
        initShader();
    }

    //初始化数据
    private void initData() {
        final float[] VerticesData = {
                // X, Y, Z,
                -30.0f, 0.0f, -16.0f,
                30.0f, 0.0f, -16.0f,
                30.0f, 0.0f, 16.0f,
                -30.0f, 0.0f, -16.0f,
                30.0f, 0.0f, 16.0f,
                -30.0f, 0.0f, 16.0f,
        };

        final float[] TextureCoordinateData = {
                0, 0,
                1, 0,
                1, 1,
                0, 0,
                1, 1,
                0, 1
        };

        // Initialize the buffers.
        mVerticesBuffer = ByteBuffer.allocateDirect(VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesBuffer.put(VerticesData).position(0);
        mVerticesTextureBuffer = ByteBuffer.allocateDirect(TextureCoordinateData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVerticesTextureBuffer.put(TextureCoordinateData).position(0);

    }

    private void initShader() {
        mProgram = MyGLUtils.buildProgram(mContext, R.raw.dice_bg_vertex, R.raw.dice_bg_fragment);
        mTextureId = MyGLUtils.loadTexture(mContext, R.drawable.container_marble, new int[2]);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mTextureCoordinationHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate");

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    }

    public void drawSelf() {

        GLES20.glUseProgram(mProgram);
        Log.d("drawSelf111", "drawSelf");
        mVerticesBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                3 * 4, mVerticesBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mVerticesTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinationHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mVerticesTextureBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordinationHandle);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

    }

}