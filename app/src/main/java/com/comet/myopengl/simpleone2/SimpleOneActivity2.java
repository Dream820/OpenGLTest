package com.comet.myopengl.simpleone2;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;

import com.comet.myopengl.R;
import com.comet.myopengl.zero.GLImage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SimpleOneActivity2 extends Activity {


    private GLSurfaceView mGLSurfaceView;
    private MyRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        setContentView(R.layout.activity_main);

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gv_one);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new MyRenderer();
        mGLSurfaceView.setRenderer(mRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }


    private static class MyRenderer implements GLSurfaceView.Renderer {

        private static final String VERTEX_SHADER =
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "attribute vec2 a_texCoord;" +
                        "varying vec2 v_texCoord;" +
                        "void main() {" +
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "  v_texCoord = a_texCoord;" +
                        "}";
        private static final String FRAGMENT_SHADER =
                "precision mediump float;" +
                        "varying vec2 v_texCoord;" +
                        "uniform sampler2D s_texture;" +
                        "void main() {" +
                        "  gl_FragColor = texture2D(s_texture, v_texCoord);" +
                        "}";

        private float picBufferLenth = 0.1f;
        private float picBufferX = picBufferLenth / 2;
        private float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);
        private float picUpY = 1f;

        private final float[] VERTEX = {   // in counterclockwise order:
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
        private static final float[] TEX_VERTEX = {   // in clockwise order:
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

        private ByteBuffer indexBuffer0;
        private ByteBuffer indexBuffer1;
        private ByteBuffer indexBuffer2;
        private ByteBuffer indexBuffer3;
        private ByteBuffer indexBuffer4;
        private ByteBuffer indexBuffer5;

        private final FloatBuffer mVertexBuffer;
        private final FloatBuffer mTexVertexBuffer;
        private final float[] mMVPMatrix = new float[16];

        private int mProgram;
        private int mPositionHandle;
        private int mMatrixHandle;
        private int mTexCoordHandle;
        private int mTexSamplerHandle;
        private int[] mTexName;

        MyRenderer() {
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

        static int loadShader(int type, String shaderCode) {
            int shader = GLES20.glCreateShader(type);
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);
            return shader;
        }

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

            mProgram = GLES20.glCreateProgram();
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
            GLES20.glAttachShader(mProgram, vertexShader);
            GLES20.glAttachShader(mProgram, fragmentShader);
            GLES20.glLinkProgram(mProgram);

            GLES20.glUseProgram(mProgram);

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
            mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");

            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, mVertexBuffer);

            GLES20.glEnableVertexAttribArray(mTexCoordHandle);
            GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
                    mTexVertexBuffer);

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

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) width / height, 0.1f, 100f);
            Matrix.translateM(mMVPMatrix, 0, 0f, 0f, -5f);
            Matrix.scaleM(mMVPMatrix, 0, 0.4f, 0.4f, 0.4f);
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);

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
    }
}
