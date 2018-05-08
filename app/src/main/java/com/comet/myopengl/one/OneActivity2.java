package com.comet.myopengl.one;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.widget.Toast;

import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OneActivity2 extends Activity {

    private GLSurfaceView mGLSurfaceView;
    private MyRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.supportGlEs20(this)) {
            Toast.makeText(this, "GLES 2.0 not supported!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gv_one);

        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new MyRenderer(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRenderer.destroy();
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
        private  float picBufferLenth = 0.1f;
        private  final float[] VERTEX = {   // in counterclockwise order:
                //front
//                1.0f - picBufferLenth, 0.614f, (float) Math.sqrt(3),//right_top
//                -1.0f + picBufferLenth, 0.614f, (float) Math.sqrt(3),//left_top
//                -1.0f + picBufferLenth, -0.614f, (float) Math.sqrt(3),// x y z left_bottom
//                1.0f - picBufferLenth, -0.614f, (float) Math.sqrt(3),//right_bottom
                -1.0f + picBufferLenth, 0.614f, (float) Math.sqrt(3),//left_top
                -1.0f + picBufferLenth, -0.614f, (float) Math.sqrt(3),// x y z left_bottom
                1.0f - picBufferLenth, 0.614f, (float) Math.sqrt(3),//right_top
                1.0f - picBufferLenth, -0.614f, (float) Math.sqrt(3),//right_bottom
        };
        private  final short[] VERTEX_INDEX = {
                0, 1, 3, 0, 3, 2,
        };
        private  final float[] TEX_VERTEX = {   // in clockwise order:
//                0.5f, 0,  // bottom right
//                0, 0,  // bottom left
//                0, 0.5f,  // top left
//                0.5f, 0.5f,  // top right

                1.0f, 0.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
        };
        private  float picBufferX = picBufferLenth / 2;
        private  float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);

        private  final float[] VERTEX2 = {   // in counterclockwise order:
                //right roar
//                1.0f + picBufferX, 0.614f, -(float) Math.sqrt(3) + picBufferZ,
//                2 - picBufferX, 0.614f, 0 - picBufferZ,
//                2 - picBufferX, -0.614f, 0 - picBufferZ,
//                1.0f + picBufferX, -0.614f, -(float) Math.sqrt(3) + picBufferZ,
                2 - picBufferX, 0.614f, 0 - picBufferZ,
                2 - picBufferX, -0.614f, 0 - picBufferZ,
                1.0f + picBufferX, 0.614f, -(float) Math.sqrt(3) + picBufferZ,
                1.0f + picBufferX, -0.614f, -(float) Math.sqrt(3) + picBufferZ,

        };
        private  final short[] VERTEX_INDEX2 = {
                4, 5, 7, 4, 7, 6,
        };
        private  final float[] TEX_VERTEX2 = {   // in clockwise order:
                1.0f, 0.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 1.0f,
        };


        private final Context mContext;
        private final FloatBuffer mVertexBuffer;
        private final FloatBuffer mTexVertexBuffer;
        private final ShortBuffer mVertexIndexBuffer;
        private final float[] mMVPMatrix = new float[16];

        private final FloatBuffer mVertexBuffer2;
        private final FloatBuffer mTexVertexBuffer2;
        private final ShortBuffer mVertexIndexBuffer2;
        private final float[] mMVPMatrix2 = new float[16];

        private int mProgram;
        private int mPositionHandle;
        private int mMatrixHandle;
        private int mTexCoordHandle;
        private int mTexSamplerHandle;
        private int mTexName;

        MyRenderer(final Context context) {
            mContext = context;
            mVertexBuffer = ByteBuffer.allocateDirect(VERTEX.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(VERTEX);
            mVertexBuffer.position(0);

            mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .put(VERTEX_INDEX);
            mVertexIndexBuffer.position(0);

            mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(TEX_VERTEX);
            mTexVertexBuffer.position(0);

            mVertexBuffer2 = ByteBuffer.allocateDirect(VERTEX2.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(VERTEX2);
            mVertexBuffer2.position(0);

            mVertexIndexBuffer2 = ByteBuffer.allocateDirect(VERTEX_INDEX2.length * 2)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .put(VERTEX_INDEX2);
            mVertexIndexBuffer2.position(0);

            mTexVertexBuffer2 = ByteBuffer.allocateDirect(TEX_VERTEX2.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(TEX_VERTEX2);
            mTexVertexBuffer2.position(0);
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

            GLES20.glEnableVertexAttribArray(mPositionHandle);
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, mVertexBuffer2);

            GLES20.glEnableVertexAttribArray(mTexCoordHandle);
            GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
                    mTexVertexBuffer2);

            int[] texNames = new int[1];
            GLES20.glGenTextures(1, texNames, 0);
            mTexName = texNames[0];
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
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

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            Matrix.perspectiveM(mMVPMatrix, 0, 45, (float) width / height, 0.1f, 100f);
            Matrix.translateM(mMVPMatrix, 0, 0f, 0f, -5f);
            Matrix.scaleM(mMVPMatrix, 0, 0.5f, 0.5f, 0.5f);


            Matrix.perspectiveM(mMVPMatrix2, 0, 45, (float) width / height, 0.1f, 100f);
            Matrix.translateM(mMVPMatrix2, 0, 0f, 0f, -5f);
            Matrix.scaleM(mMVPMatrix2, 0, 0.5f, 0.5f, 0.5f);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glUniform1i(mTexSamplerHandle, 0);

            GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length,
                    GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

            GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix2, 0);
            GLES20.glUniform1i(mTexSamplerHandle, 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX2.length,
                    GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer2);
        }

        void destroy() {
            GLES20.glDeleteTextures(1, new int[]{mTexName}, 0);
        }
    }
}