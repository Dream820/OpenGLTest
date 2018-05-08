package com.comet.myopengl.zero;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Cube2 {

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

    public Cube2() {

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

    public void draw(GL10 gl) {

    }

    public void loadGLTexture(GL10 gl, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.keyboard_1);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}
