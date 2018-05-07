package com.comet.myopengl.sample2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import com.comet.myopengl.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class is an object representation of
 * a Cube containing the vertex information,
 * texture coordinates, the vertex indices
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */
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
    /**
     * The buffer holding the normals
     */
    private FloatBuffer normalBuffer;

    /**
     * Our texture pointer
     */
    private int[] textures = new int[3];

    //second is right;fourth is left;first is front;third is roar
    private float vertices[] = {//四个点
            //front
            -1.0f, -1.0f, (float) Math.sqrt(3),// x y z left_bottom
            1.0f, -1.0f, (float) Math.sqrt(3),//right_bottom
            -1.0f, 1.0f, (float) Math.sqrt(3),//left_top
            1.0f, 1.0f, (float) Math.sqrt(3),//right_top

            //right front
            2, -1.0f, 0,
            1.0f, -1.0f, -1.0f,
            2, 1.0f, 0,
            1.0f, 1.0f, -1.0f,

            //roar
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            //left front
            -2, -1.0f, 0,
            -1.0f, -1.0f, 1.0f,
            -2, 1.0f, 0,
            -1.0f, 1.0f, 1.0f,

            //left roar
            -1.0f, -1.0f, -1.0f,
            -2, -1.0f, 0,
            -1.0f, 1.0f, -1.0f,
            -2, 1.0f, 0,

            //right roar
            1.0f, -1.0f, 1.0f,
            2.0f, -1.0f, 0,
            1.0f, 1.0f, 1.0f,
            2.0f, 1.0f, 0,

    };

    /**
     * The initial normals for the lighting calculations
     */
    private float normals[] = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,

    };

    /**
     * The initial texture coordinates (u, v)
     */
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

    /**
     * The initial indices definition
     */
    private byte indices[] = {
            0, 1, 3, 0, 3, 2,
            4, 5, 7, 4, 7, 6,
            8, 9, 11, 8, 11, 10,
            12, 13, 15, 12, 15, 14,
            16, 17, 19, 16, 19, 18,
            20, 21, 23, 20, 23, 22,
    };

    /**
     * The Cube constructor.
     * <p>
     * Initiate the buffers.
     */
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

        byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl     - The GL Context
     * @param filter - Which texture filter to be used
     */
    public void draw(GL10 gl, int filter) {
        //Bind the texture according to the set texture filter
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[filter]);

        //Enable the vertex, texture and normal state
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        //Set the face rotation
        gl.glFrontFace(GL10.GL_CCW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);//设置纹理
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);//法线

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    /**
     * Load the textures
     *
     * @param gl      - The GL Context
     * @param context - The Activity context
     */
    public void loadGLTexture(GL10 gl, Context context) {
        //Get the texture from the Android resource directory
        InputStream is = context.getResources().openRawResource(R.raw.ic_launcher);
        Bitmap bitmap = null;
        try {
            //BitmapFactory is an Android graphics utility for images
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        //Generate there texture pointer
        gl.glGenTextures(3, textures, 0);

        //Create Nearest Filtered Texture and bind it to texture 0
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        //Create Linear Filtered Texture and bind it to texture 1
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//
//        //Create mipmapped textures and bind it to texture 2
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
//
//        if (gl instanceof GL11) {
//            gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
//            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//
//            //
//        } else {
//
//            buildMipmap(gl, bitmap);
//        }

        //Clean up
        bitmap.recycle();
    }

    /**
     * Our own MipMap generation implementation.
     * Scale the original bitmap down, always by factor two,
     * and set it as new mipmap level.
     * <p>
     * Thanks to Mike Miller (with minor changes)!
     *
     * @param gl     - The GL Context
     * @param bitmap - The bitmap to mipmap
     */
    private void buildMipmap(GL10 gl, Bitmap bitmap) {
        //
        int level = 0;
        //
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        //
        while (height >= 1 || width >= 1) {
            //First of all, generate the texture from our bitmap and set it to the according level
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
            if (height == 1 || width == 1) {
                break;
            }

            //Increase the mipmap level
            level++;
            height /= 2;
            width /= 2;
            Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);

            //Clean up
            bitmap.recycle();
            bitmap = bitmap2;
        }
    }
}
