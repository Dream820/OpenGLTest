package com.comet.myopengl;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Cube3 {

    /**
     * The buffer holding the vertices
     */
    private FloatBuffer vertexBuffer;
    /**
     * The buffer holding the texture coordinates
     */
    private FloatBuffer textureBuffer;
    /**
     * The buffer holding the indices
     */
    private ByteBuffer indexBuffer;

    private int[] textures = new int[3];

    private float picBufferLenth = 0.1f;
    private float picBufferX = picBufferLenth / 2;
    private float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);

    private float vertices[] = {
            //front
            -1.0f + picBufferLenth, 0.614f, (float) Math.sqrt(3),//left_top
            -1.0f + picBufferLenth, -0.614f, (float) Math.sqrt(3),// x y z left_bottom
            1.0f - picBufferLenth, 0.614f, (float) Math.sqrt(3),//right_top
            1.0f - picBufferLenth, -0.614f, (float) Math.sqrt(3),//right_bottom
    };

    private float texture[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private byte indices[] = {
            0, 1, 3, 0, 3, 2
    };

    public Cube3() {

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
    }



    public void draw() {



        // 用 glDrawElements 来绘制,mVertexIndexBuffer 指定了顶点绘制顺序
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertices.length,
                GLES20.GL_UNSIGNED_SHORT, vertexBuffer);
    }

    public void loadGLTexture(GL10 gl, Context context) {
        int[] texNames = new int[2];
        GLES20.glGenTextures(1, texNames, 0);
        int mTexName = texNames[1];
        Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.myApplication.getResources(),
                R.drawable.keyboard_1);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}
