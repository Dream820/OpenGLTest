package com.comet.myopengl.gl2;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.comet.myopengl.zero.GLImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ComBineView extends GLSurfaceView {

    CubeRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private float mPreviousDeg;

    public ComBineView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(mRenderer = new CubeRenderer());
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public ComBineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(mRenderer = new CubeRenderer());
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onPause() {
    } //do stuff

    @Override
    public void onResume() {
    } //do stuff

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            System.out.println();
            if (event.getPointerCount() == 1) {
                float x = event.getX();
                float y = event.getY();

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (mRenderer != null) {
                        float deltaX = (x - mPreviousX) / this.getWidth() * 360;
                        float deltaY = (y - mPreviousY) / this.getHeight() * 360;
                        mRenderer.mDeltaX += deltaY;
                        mRenderer.mDeltaY += deltaX;
                    }
                }
                mPreviousX = x;
                mPreviousY = y;
            } else if (event.getPointerCount() == 2) {
                float dx = event.getX(1) - event.getX(0);
                float dy = event.getY(1) - event.getY(0);
                float deg = (float) Math.toDegrees(Math.atan2(dy, dx));
                if (event.getAction() != MotionEvent.ACTION_MOVE) {
                    mPreviousDeg = deg;
                    mPreviousX = event.getX();
                    mPreviousY = event.getY();
                    return true;
                }
                float ddeg = deg - mPreviousDeg;
                mRenderer.mDeltaZ -= ddeg;
                mPreviousDeg = deg;
            }
            requestRender();
        }
        return true;
    }

    public void spinCube(float dx, float dy, float dz) {
        mRenderer.mDeltaX += dx;
        mRenderer.mDeltaY += dy;
        mRenderer.mDeltaZ += dz;
        requestRender();
    }

    private class CubeRenderer implements Renderer {

        volatile public float mDeltaX, mDeltaY, mDeltaZ;

        int iProgId;
        int iPosition;
        int iVPMatrix;
        int iTexLoc;
        int iTexCoords;

        float[] m_fProjMatrix = new float[16];
        float[] m_fViewMatrix = new float[16];
        float[] m_fIdentity = new float[16];
        float[] m_fVPMatrix = new float[16];

        private float[] mAccumulatedRotation = new float[16];

        private float[] mCurrentRotation = new float[16];

        private float[] mTemporaryMatrix = new float[16];

        private float picBufferLenth = 0.1f;
        private float picBufferX = picBufferLenth / 2;
        private float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);
        private float picUpY = 1f;

        private float[] VERTEX = {   // in counterclockwise order:
                //front
                -1.0f + picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//left_top
                -1.0f + picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),// x y z left_bottom
                1.0f - picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//right_top
                1.0f - picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),//right_bottom

                //left front
                -2 + picBufferX, 0.614f + picUpY, 0 + picBufferZ,
                -2 + picBufferX, -0.614f + picUpY, 0 + picBufferZ,
                -1.0f - picBufferX, 0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,
                -1.0f - picBufferX, -0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,

                //left roar
                -1.0f - picBufferX, 0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,
                -1.0f - picBufferX, -0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,
                -2 + picBufferX, 0.614f + picUpY, 0 - picBufferZ,
                -2 + picBufferX, -0.614f + picUpY, 0 - picBufferZ,

                //roar
                1.0f - picBufferLenth, 0.614f + picUpY, -(float) Math.sqrt(3),
                1.0f - picBufferLenth, -0.614f + picUpY, -(float) Math.sqrt(3),
                -1.0f + picBufferLenth, 0.614f + picUpY, -(float) Math.sqrt(3),
                -1.0f + picBufferLenth, -0.614f + picUpY, -(float) Math.sqrt(3),

                //right roar
                2 - picBufferX, 0.614f + picUpY, 0 - picBufferZ,
                2 - picBufferX, -0.614f + picUpY, 0 - picBufferZ,
                1.0f + picBufferX, 0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,
                1.0f + picBufferX, -0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,

                //right front
                1.0f + picBufferX, 0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,
                1.0f + picBufferX, -0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,
                2.0f - picBufferX, 0.614f + picUpY, 0 + picBufferZ,
                2.0f - picBufferX, -0.614f + picUpY, 0 + picBufferZ,
        };
        private final float[] TEX_VERTEX = {   // in clockwise order:
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
        private int[] mTexName;
        private ByteBuffer indexBuffer0;
        private ByteBuffer indexBuffer1;
        private ByteBuffer indexBuffer2;
        private ByteBuffer indexBuffer3;
        private ByteBuffer indexBuffer4;
        private ByteBuffer indexBuffer5;
        private FloatBuffer mVertexBuffer;
        private FloatBuffer mTexVertexBuffer;

        final String strVShader =
                "attribute vec4 a_position;" +
                        "attribute vec4 a_color;" +
                        "attribute vec3 a_normal;" +
                        "uniform mat4 u_VPMatrix;" +
                        "uniform vec3 u_LightPos;" +
                        "varying vec3 v_texCoords;" +
                        "attribute vec3 a_texCoords;" +
                        "void main()" +
                        "{" +
                        "v_texCoords = a_texCoords;" +
                        "gl_Position = u_VPMatrix * a_position;" +
                        "}";

        final String strFShader =
                "precision mediump float;" +
                        "uniform samplerCube u_texId;" +
                        "varying vec3 v_texCoords;" +
                        "void main()" +
                        "{" +
                        "gl_FragColor = textureCube(u_texId, v_texCoords);" +
                        "}";


        public CubeRenderer() {
            mVertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(VERTEX);
            mVertexBuffer.position(0);

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

            mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(TEX_VERTEX);
            mTexVertexBuffer.position(0);
        }

        public void onDrawFrame(GL10 arg0) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            GLES20.glUseProgram(iProgId);

            GLES20.glEnableVertexAttribArray(iPosition);
            GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false,
                    12, mVertexBuffer);

            GLES20.glEnableVertexAttribArray(iTexCoords);
            GLES20.glVertexAttribPointer(iTexCoords, 2, GLES20.GL_FLOAT, false, 0,
                    mTexVertexBuffer);

            Matrix.setIdentityM(m_fIdentity, 0);

            Matrix.setIdentityM(mCurrentRotation, 0);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaZ, 0.0f, 0.0f, 1.0f);
            mDeltaX = 0.0f;
            mDeltaY = 0.0f;
            mDeltaZ = 0.0f;

            Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
            System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

//            Matrix.multiplyMM(mTemporaryMatrix, 0, m_fIdentity, 0, mAccumulatedRotation, 0);
            System.arraycopy(mTemporaryMatrix, 0, m_fIdentity, 0, 16);

            Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
            Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
            GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[0]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices0.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer0);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[1]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices1.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer1);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[2]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices2.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer2);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[3]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices3.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer3);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[4]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices4.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer4);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[5]);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices5.length,
                    GLES20.GL_UNSIGNED_BYTE, indexBuffer5);


        }

        public void onSurfaceChanged(GL10 arg0, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            Matrix.frustumM(m_fProjMatrix, 0, -(float) width / height, (float) width / height, -1, 1, 1, 10);
        }

        @Override
        public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
            GLES20.glClearColor(0, 0, 0, 0);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glDepthFunc(GLES20.GL_LEQUAL);
            GLES20.glFrontFace(GLES20.GL_CCW);

            Matrix.setLookAtM(m_fViewMatrix,
                    0, 0, 0, 6, 0, 0, 0, 0, 1, 0);
            Matrix.setIdentityM(mAccumulatedRotation, 0);

            iProgId = loadProgram(strVShader, strFShader);
            iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
            iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_VPMatrix");
            iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
            iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");


            mTexName = new int[6];
            GLES20.glGenTextures(6, mTexName, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap1, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[1]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap2, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[2]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap3, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[3]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap4, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[4]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap5, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexName[5]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                    GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                    GLES20.GL_LINEAR);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLImage.mBitmap6, 0);
        }

    }

    public static int loadShader(String strSource, int iType) {
        int[] compiled = new int[1];
        int iShader = GLES20.glCreateShader(iType);
        GLES20.glShaderSource(iShader, strSource);
        GLES20.glCompileShader(iShader);
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(iShader));
            return 0;
        }
        return iShader;
    }

    public static int loadProgram(String strVSource, String strFSource) {
        int iVShader;
        int iFShader;
        int iProgId;
        int[] link = new int[1];
        iVShader = loadShader(strVSource, GLES20.GL_VERTEX_SHADER);
        if (iVShader == 0) {
            Log.d("Load Program", "Vertex Shader Failed");
            return 0;
        }
        iFShader = loadShader(strFSource, GLES20.GL_FRAGMENT_SHADER);
        if (iFShader == 0) {
            Log.d("Load Program", "Fragment Shader Failed");
            return 0;
        }

        iProgId = GLES20.glCreateProgram();

        GLES20.glAttachShader(iProgId, iVShader);
        GLES20.glAttachShader(iProgId, iFShader);

        GLES20.glLinkProgram(iProgId);

        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, link, 0);
        if (link[0] <= 0) {
            Log.d("Load Program", "Linking Failed");
            return 0;
        }
        GLES20.glDeleteShader(iVShader);
        GLES20.glDeleteShader(iFShader);
        return iProgId;
    }
}