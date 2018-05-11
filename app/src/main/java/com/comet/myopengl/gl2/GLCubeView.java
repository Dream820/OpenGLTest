package com.comet.myopengl.gl2;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLCubeView extends GLSurfaceView {

    CubeRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private float mPreviousDeg;

    public GLCubeView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(mRenderer = new CubeRenderer());
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public GLCubeView(Context context, AttributeSet attrs) {
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
        int iTexId;
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

        private float cube[] = {
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

        short[] indeces = {
                   0, 1, 3, 0, 3, 2,
                   4, 5, 7, 4, 7, 6,
                   8, 9, 11, 8, 11, 10,
                   12, 13, 15, 12, 15, 14,
                   16, 17, 19, 16, 19, 18,
                   20, 21, 23, 20, 23, 22,
        };

        float[] tex = {
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

        FloatBuffer cubeBuffer = null;
        ShortBuffer indexBuffer = null;
        FloatBuffer texBuffer = null;

        public CubeRenderer() {
            cubeBuffer = ByteBuffer.allocateDirect(cube.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            cubeBuffer.put(cube).position(0);

            indexBuffer = ByteBuffer.allocateDirect(indeces.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
            indexBuffer.put(indeces).position(0);

            texBuffer = ByteBuffer.allocateDirect(tex.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            texBuffer.put(tex).position(0);
        }

        public void onDrawFrame(GL10 arg0) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            GLES20.glUseProgram(iProgId);

            cubeBuffer.position(0);
            GLES20.glVertexAttribPointer(iPosition, 3, GLES20.GL_FLOAT, false, 0, cubeBuffer);
            GLES20.glEnableVertexAttribArray(iPosition);

            texBuffer.position(0);
            GLES20.glVertexAttribPointer(iTexCoords, 3, GLES20.GL_FLOAT, false, 0, texBuffer);
            GLES20.glEnableVertexAttribArray(iTexCoords);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, iTexId);
            GLES20.glUniform1i(iTexLoc, 0);

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

            Matrix.multiplyMM(mTemporaryMatrix, 0, m_fIdentity, 0, mAccumulatedRotation, 0);
            System.arraycopy(mTemporaryMatrix, 0, m_fIdentity, 0, 16);

            Matrix.multiplyMM(m_fVPMatrix, 0, m_fViewMatrix, 0, m_fIdentity, 0);
            Matrix.multiplyMM(m_fVPMatrix, 0, m_fProjMatrix, 0, m_fVPMatrix, 0);
            GLES20.glUniformMatrix4fv(iVPMatrix, 1, false, m_fVPMatrix, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
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

            Matrix.setLookAtM(m_fViewMatrix, 0, 0, 0, 6, 0, 0, 0, 0, 1, 0);
            Matrix.setIdentityM(mAccumulatedRotation, 0);

            iProgId = loadProgram(strVShader, strFShader);
            iPosition = GLES20.glGetAttribLocation(iProgId, "a_position");
            iVPMatrix = GLES20.glGetUniformLocation(iProgId, "u_VPMatrix");
            iTexLoc = GLES20.glGetUniformLocation(iProgId, "u_texId");
            iTexCoords = GLES20.glGetAttribLocation(iProgId, "a_texCoords");

            int[] textureId = new int[1];

            // Face 0 - Red
            byte[] cubePixels0 = {127, 0, 0};
            // Face 1 - Green
            byte[] cubePixels1 = {0, 127, 0};
            // Face 2 - Blue
            byte[] cubePixels2 = {0, 0, 127};
            // Face 3 - Yellow
            byte[] cubePixels3 = {127, 127, 0};
            // Face 4 - Purple
            byte[] cubePixels4 = {127, 0, 127};
            // Face 5 - White
            byte[] cubePixels5 = {127, 127, 127};

            ByteBuffer cubePixels = ByteBuffer.allocateDirect(3);

            // Generate a texture object
            GLES20.glGenTextures(1, textureId, 0);

            // Bind the texture object
            GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId[0]);

            // Load the cube face - Positive X
            cubePixels.put(cubePixels0).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Load the cube face - Negative X
            cubePixels.put(cubePixels1).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Load the cube face - Positive Y
            cubePixels.put(cubePixels2).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Load the cube face - Negative Y
            cubePixels.put(cubePixels3).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Load the cube face - Positive Z
            cubePixels.put(cubePixels4).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Load the cube face - Negative Z
            cubePixels.put(cubePixels5).position(0);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GLES20.GL_RGB, 1, 1, 0,
                    GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, cubePixels);

            // Set the filtering mode
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            iTexId = textureId[0];
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