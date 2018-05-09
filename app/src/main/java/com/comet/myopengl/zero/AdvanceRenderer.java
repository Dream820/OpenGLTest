package com.comet.myopengl.zero;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AdvanceRenderer implements GLSurfaceView.Renderer {

    private FloatBuffer vertexBuffer;
    private FloatBuffer floatBuffer;
    private ByteBuffer indexBuffer0;
    private ByteBuffer indexBuffer1;
    private ByteBuffer indexBuffer2;
    private ByteBuffer indexBuffer3;
    private ByteBuffer indexBuffer4;
    private ByteBuffer indexBuffer5;

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

            //roar
            1.0f - picBufferLenth, 0.614f, -(float) Math.sqrt(3),
            1.0f - picBufferLenth, -0.614f, -(float) Math.sqrt(3),
            -1.0f + picBufferLenth, 0.614f, -(float) Math.sqrt(3),
            -1.0f + picBufferLenth, -0.614f, -(float) Math.sqrt(3),

            //right roar
            2 - picBufferX, 0.614f, 0 - picBufferZ,
            2 - picBufferX, -0.614f, 0 - picBufferZ,
            1.0f + picBufferX, 0.614f, -(float) Math.sqrt(3) + picBufferZ,
            1.0f + picBufferX, -0.614f, -(float) Math.sqrt(3) + picBufferZ,

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

    private byte indices0[] = {
            0, 1, 3, 0, 3, 2
    };
    private byte indices1[] = {
            4, 5, 7, 4, 7, 6
    };
    private byte indices2[] = {
            8, 9, 11, 8, 11, 10
    };
    private byte indices3[] = {
            12, 13, 15, 12, 15, 14
    };
    private byte indices4[] = {
            16, 17, 19, 16, 19, 18
    };
    private byte indices5[] = {
            20, 21, 23, 20, 23, 22
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

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap2, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap3, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[3]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap4, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[4]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap5, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[5]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap6, 0);
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

        indexBuffer1 = ByteBuffer.allocateDirect(indices1.length);
        indexBuffer1.put(indices1);
        indexBuffer1.position(0);

        indexBuffer2 = ByteBuffer.allocateDirect(indices2.length);
        indexBuffer2.put(indices2);
        indexBuffer2.position(0);

        indexBuffer3 = ByteBuffer.allocateDirect(indices3.length);
        indexBuffer3.put(indices3);
        indexBuffer3.position(0);

        indexBuffer4 = ByteBuffer.allocateDirect(indices4.length);
        indexBuffer4.put(indices4);
        indexBuffer4.position(0);

        indexBuffer5 = ByteBuffer.allocateDirect(indices5.length);
        indexBuffer5.put(indices5);
        indexBuffer5.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("onSurfaceChangeddd", "111");
        if (height == 0) {
            height = 1;
        }
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public float yrot; // Y Rotation
    public int xrot; // Y Rotation
    public int perX = 5;

    public boolean isCorrecting = false;
    public int plusOrMinus = -1;
    public int time = 0;
    public float perAngle = 3;
    public boolean loadTexture = false;

    public int animatorStatus = 0;

    public float angleFirstAnimator = 90;
    public float angleFirstAnimatorPer = 3;

    public float angleSecond = 44;
    public float angleSecondPer = 2;

    @Override
    public void onDrawFrame(GL10 gl) {//20毫秒刷新一次
        Log.d("onDrawFrameee", System.currentTimeMillis() + "");
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0.0f,
                0.4f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1, 0f);
        gl.glEnable(GL10.GL_DEPTH_TEST); // 开启时时只绘制前面的一层
        gl.glScalef(0.2f, 0.2f, 0.2f);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, floatBuffer);//设置纹理

        switch (animatorStatus) {
            case 1:
                gl.glTranslatef(0, 0, (float) Math.sqrt(3));
                gl.glRotatef(angleFirstAnimator, 0.0f, 1.0f, 0.0f);
                gl.glTranslatef(0, 0, -(float) Math.sqrt(3));
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices0.length, GL10.GL_UNSIGNED_BYTE, indexBuffer0);
                angleFirstAnimator -= angleFirstAnimatorPer;
                if (angleFirstAnimator == -angleFirstAnimatorPer) {
                    animatorStatus = 2;
                }
                break;
            case 2:
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices0.length, GL10.GL_UNSIGNED_BYTE, indexBuffer0);
                gl.glRotatef(angleSecond, 0.0f, 1.0f, 0.0f);

                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices1.length, GL10.GL_UNSIGNED_BYTE, indexBuffer1);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices2.length, GL10.GL_UNSIGNED_BYTE, indexBuffer2);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[3]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices3.length, GL10.GL_UNSIGNED_BYTE, indexBuffer3);


                gl.glRotatef(-angleSecond * 2, 0.0f, 1.0f, 0.0f);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[4]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices4.length, GL10.GL_UNSIGNED_BYTE, indexBuffer4);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[5]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices5.length, GL10.GL_UNSIGNED_BYTE, indexBuffer5);

                angleSecond -= angleSecondPer;
                if (angleSecond == -angleSecondPer) {
                    animatorStatus = 3;
                }
                break;
            case 3:
                gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
                gl.glRotatef((float) xrot / 100f, 1.0f, 0.0f, 0.0f);
                xrot = xrot + perX;
                if (xrot == 200) {
                    perX = -5;
                }
                if (xrot == -200) {
                    perX = 5;
                }
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices0.length, GL10.GL_UNSIGNED_BYTE, indexBuffer0);

                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices1.length, GL10.GL_UNSIGNED_BYTE, indexBuffer1);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[2]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices2.length, GL10.GL_UNSIGNED_BYTE, indexBuffer2);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[3]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices3.length, GL10.GL_UNSIGNED_BYTE, indexBuffer3);

                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[4]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices4.length, GL10.GL_UNSIGNED_BYTE, indexBuffer4);
                gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[5]);
                gl.glDrawElements(GL10.GL_TRIANGLES, indices5.length, GL10.GL_UNSIGNED_BYTE, indexBuffer5);
                break;
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        if (isCorrecting) {
            if (time > 1) {
                yrot = yrot + plusOrMinus * perAngle;
                time -= 1;
            } else if (time == 1) {
                //最后一次直接等于 ortho
                lastCorrect();
                time -= 1;
            } else {
                isCorrecting = false;
            }
        }
        if (loadTexture) {
            loadTexture = false;
            setTexture(gl);
        }
    }

    public void lastCorrect() {
        float ramain = yrot % 60;
        if (ramain >= 0 && ramain < 30) {
            yrot = yrot - ramain;
        } else if (ramain >= 30 && ramain <= 60f) {
            yrot = yrot + 60 - 58;
        } else if (ramain <= 0 && ramain > -30) {
            yrot = yrot - ramain;
        } else if (ramain <= -30 && ramain >= -60) {
            yrot = yrot + (-60 - ramain);
        }
    }

    public void setTexture(GL10 gl) {
        // 需要转换那个一个
        Log.d("setTexture", "");
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap7, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
    }
}
