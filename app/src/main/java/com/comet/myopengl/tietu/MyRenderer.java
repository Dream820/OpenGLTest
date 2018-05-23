package com.comet.myopengl.tietu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;

public class MyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "Test5Renderer";
    private Context mContext;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer[] mCubePositions;
    private final FloatBuffer[] mCubeColors;
    private final FloatBuffer[] mCubeTextureCoordinates;
    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;
    private int[] mTextureDataHandle = new int[6];
    private int mProgramHandle;
    private final int POSITION_DATA_SIZE = 3;
    private final int COLOR_DATA_SIZE = 4;
    private final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    //    float xAngle=0;//绕x轴旋转的角度
    float cube_0_yAngle = 90;//绕x轴旋转的角度
    float cube_4_yAngle = -120;//绕x轴旋转的角度
    float cube_3_yAngle = -120;//绕x轴旋转的角度
    float cube_5_yAngle = -120;//绕x轴旋转的角度
    float cube_2_yAngle = 120;//绕x轴旋转的角度
    float cube_1_yAngle = 120;//绕x轴旋转的角度
    float yAngle_add = 3f;//绕x轴旋转的角度
    float yAngle = 0;//绕x轴旋转的角度
    float xAngle = 0;//绕x轴旋转的角度
    float xAngle_add = 0.008f;//绕x轴旋转的角度
    static float[] mMMatrix = new float[16];//具体物体的移动旋转矩阵，旋转、平移
    public static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵
    public static float[] mProjMatrix = new float[16];//4x4矩阵 投影用

    int fps;
    FPSCounter fpsCounter;

    public MyRenderer(final Context context) {
        mContext = context;

        float picBufferLenth = 0.05f;
        float picBufferX = picBufferLenth / 2;
        float picBufferZ = (float) (Math.sin(Math.toRadians(30)) * picBufferLenth);

        float picUpY = 0f;
        float fangda=1.5f;

        final float cubePosition[][] =
                {
                        {//0
                                -1.0f + picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//left_top
                                -1.0f + picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),//left_center
                                1.0f - picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//right_top
                                -1.0f + picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),//left_bottom
                                1.0f - picBufferLenth, -0.614f + picUpY, (float) Math.sqrt(3),//right_bottom
                                1.0f - picBufferLenth, 0.614f + picUpY, (float) Math.sqrt(3),//right_center
                        },

                        {//1
                                2 - picBufferX, 0.614f + picUpY, 0 - picBufferZ,//left_top
                                2 - picBufferX, -0.614f + picUpY, 0 - picBufferZ,// x y z left_bottom
                                1.0f + picBufferX, 0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,//right_top
                                2 - picBufferX, -0.614f + picUpY, 0 - picBufferZ,// x y z left_bottom
                                1.0f + picBufferX, -0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,//right_bottom
                                1.0f + picBufferX, 0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,//right_top

                        },
                        {//2
                                1.0f + picBufferX, 0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,//left_top
                                1.0f + picBufferX, -0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,// x y z left_bottom
                                2.0f - picBufferX, 0.614f + picUpY, 0 + picBufferZ,//right_top
                                1.0f + picBufferX, -0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,// x y z left_bottom
                                2.0f - picBufferX, -0.614f + picUpY, 0 + picBufferZ,//right_bottom
                                2.0f - picBufferX, 0.614f + picUpY, 0 + picBufferZ,//right_top
                        },

                        {//3
                                //roar
                                1.0f - picBufferLenth, 0.614f + picUpY, -(float) Math.sqrt(3),//left_top
                                1.0f - picBufferLenth, -0.614f + picUpY, -(float) Math.sqrt(3),// x y z left_bottom
                                -1.0f + picBufferLenth, 0.614f + picUpY, -(float) Math.sqrt(3),//right_top
                                1.0f - picBufferLenth, -0.614f + picUpY, -(float) Math.sqrt(3),// x y z left_bottom
                                -1.0f + picBufferLenth, -0.614f + picUpY, -(float) Math.sqrt(3),//right_bottom
                                -1.0f + picBufferLenth, 0.614f + picUpY, -(float) Math.sqrt(3),//right_top
                        },

                        {//4
                                //left front
                                -2 + picBufferX, 0.614f + picUpY, 0 + picBufferZ,                       //left_top
                                -2 + picBufferX, -0.614f + picUpY, 0 + picBufferZ,                      // x y z left_bottom
                                -1.0f - picBufferX, 0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ, //right_top
                                -2 + picBufferX, -0.614f + picUpY, 0 + picBufferZ,                      // x y z left_bottom
                                -1.0f - picBufferX, -0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ,//right_bottom
                                -1.0f - picBufferX, 0.614f + picUpY, (float) Math.sqrt(3) - picBufferZ, //right_top
                        },

                        {//5
                                //left roar
                                -1.0f - picBufferX, 0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,//left_top
                                -1.0f - picBufferX, -0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,// x y z left_bottom
                                -2 + picBufferX, 0.614f + picUpY, 0 - picBufferZ,//right_top
                                -1.0f - picBufferX, -0.614f + picUpY, -(float) Math.sqrt(3) + picBufferZ,// x y z left_bottom
                                -2 + picBufferX, -0.614f + picUpY, 0 - picBufferZ,//right_bottom
                                -2 + picBufferX, 0.614f + picUpY, 0 - picBufferZ,//right_top
                        }
                };
        final float cubeColor[][] =
                {
                        // Front face (red)
                        {
                                1.0f, 0.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f, 0.0f,
                                1.0f, 0.0f, 0.0f, 0.0f,
                        },
                        // Right face (green)
                        {
                                0.0f, 1.0f, 0.0f, 1.0f,
                                0.0f, 1.0f, 0.0f, 1.0f,
                                0.0f, 1.0f, 0.0f, 1.0f,
                                0.0f, 1.0f, 0.0f, 1.0f,
                                0.0f, 1.0f, 0.0f, 1.0f,
                                0.0f, 1.0f, 0.0f, 1.0f,
                        },
                        {
                                // Back face (blue)
                                0.0f, 0.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 1.0f,
                                0.0f, 0.0f, 1.0f, 1.0f,
                        },
                        {
                                // Left face (yellow)
                                1.0f, 1.0f, 0.0f, 1.0f,
                                1.0f, 1.0f, 0.0f, 1.0f,
                                1.0f, 1.0f, 0.0f, 1.0f,
                                1.0f, 1.0f, 0.0f, 1.0f,
                                1.0f, 1.0f, 0.0f, 1.0f,
                                1.0f, 1.0f, 0.0f, 1.0f,
                        },
                        {
                                // Top face (cyan)
                                0.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 1.0f, 1.0f, 1.0f,
                                0.0f, 1.0f, 1.0f, 1.0f,
                        },
                        {
                                // Bottom face (magenta)
                                1.0f, 0.0f, 1.0f, 1.0f,
                                1.0f, 0.0f, 1.0f, 1.0f,
                                1.0f, 0.0f, 1.0f, 1.0f,
                                1.0f, 0.0f, 1.0f, 1.0f,
                                1.0f, 0.0f, 1.0f, 1.0f,
                                1.0f, 0.0f, 1.0f, 1.0f
                        }
                };
        final float cubeTextureCoordinate[][] =
                {   // Front face
                        {
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,
                        },
                        {
                                // Right face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,
                        },
                        {
                                // Back face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,
                        },
                        {
                                // Left face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,
                        },
                        {
                                // Top face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f,
                        },
                        {
                                // Bottom face
                                0.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 0.0f,
                                0.0f, 1.0f,
                                1.0f, 1.0f,
                                1.0f, 0.0f
                        }
                };

        mCubePositions = new FloatBuffer[6];
        for (int i = 0; i < cubePosition.length; i++) {
            mCubePositions[i] = ByteBuffer.allocateDirect(cubePosition[i].length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mCubePositions[i].put(cubePosition[i]).position(0);
        }


        mCubeColors = new FloatBuffer[6];
        for (int i = 0; i < cubeColor.length; i++) {
            mCubeColors[i] = ByteBuffer.allocateDirect(cubeColor[i].length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mCubeColors[i].put(cubeColor[i]).position(0);
        }

        mCubeTextureCoordinates = new FloatBuffer[6];
        for (int i = 0; i < cubeTextureCoordinate.length; i++) {
            mCubeTextureCoordinates[i] = ByteBuffer.allocateDirect(cubeTextureCoordinate[i].length * BYTES_PER_FLOAT)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mCubeTextureCoordinates[i].put(cubeTextureCoordinate[i]).position(0);
        }

        fpsCounter = new FPSCounter();

    }

    @Override
    public void onDrawFrame(GL10 gl) {  // TODO Auto-generated method stub
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        GLES20.glUseProgram(mProgramHandle);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(mModelMatrix, 0, 0, 1.0f, 0.0f, 0.0f);
        for (int i = 0; i < mCubePositions.length; i++) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[i]);
            GLES20.glUniform1i(mTextureUniformHandle, 0);
            if (cube_0_yAngle!=0||cube_1_yAngle!=0||cube_2_yAngle!=0||
                    cube_3_yAngle!=0||cube_4_yAngle!=0||cube_5_yAngle!=0) {
                if (i==0) {
                    drawCube_0(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }else if(i==4&&cube_0_yAngle<=0){
                    drawCube_4(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }else if(i==5&&cube_4_yAngle>=0){
                    drawCube_5(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }else if(i==3&&cube_5_yAngle>=0){
                    drawCube_3(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }else if(i==2&&cube_4_yAngle>-60){
                    drawCube_2(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }else if(i==1&&cube_2_yAngle<=0){
                    drawCube_1(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
                }
            } else {
                drawCube(mCubePositions[i], mCubeColors[i], mCubeTextureCoordinates[i]);
            }
        }
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle[0]);
//        GLES20.glUniform1i(mTextureUniformHandle, 0);
//        drawCube(mCubePositions[0], mCubeColors[0], mCubeTextureCoordinates[0]);
        fps = fpsCounter.getFPS();
    }

    private void drawCube(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                          final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        //设置沿Z轴正向位移1
//        Matrix.translateM(mMMatrix,0,0,0,-1);
        //设置绕x轴旋转
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);
        Matrix.rotateM(mMMatrix, 0, yAngle, 0, 1, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (xAngle > 3 || xAngle < -3) {
            xAngle_add = -xAngle_add;
        }
        xAngle += xAngle_add;
//        yAnœgle += 1f;
    }

    private void drawCube_0(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                           final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,0,0,(float) Math.sqrt(3));
        Matrix.rotateM(mMMatrix, 0, cube_0_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,0,0,-(float) Math.sqrt(3));
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_0_yAngle >0) {
            cube_0_yAngle -= 3f;
        }
    }

    private void drawCube_1(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                            final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,2,0,0);
        Matrix.rotateM(mMMatrix, 0, cube_1_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,-2,0,0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_1_yAngle >0) {
            cube_1_yAngle -= yAngle_add;
        }
    }

    private void drawCube_2(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                            final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,1,0,(float) Math.sqrt(3)-0.1f);
        Matrix.rotateM(mMMatrix, 0, cube_2_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,-1,0,-(float) Math.sqrt(3)+0.1f);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_2_yAngle >0) {
            cube_2_yAngle -= yAngle_add;
        }
    }

    private void drawCube_3(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                          final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,-1,0,-(float) Math.sqrt(3));
        Matrix.rotateM(mMMatrix, 0, cube_3_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,1,0,(float) Math.sqrt(3));
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_3_yAngle <0) {
            cube_3_yAngle += yAngle_add;
        }
    }

    private void drawCube_4(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                          final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,-1,0,(float) Math.sqrt(3)-0.1f);
        Matrix.rotateM(mMMatrix, 0, cube_4_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,1,0,-(float) Math.sqrt(3)+0.1f);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_4_yAngle <0) {
            cube_4_yAngle += yAngle_add;
        }
    }

    private void drawCube_5(final FloatBuffer cubePositionsBuffer, final FloatBuffer cubeColorsBuffer,
                          final FloatBuffer cubeTextureCoordinatesBuffer) {
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //初始化变换矩阵
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,-2,0,0);
        Matrix.rotateM(mMMatrix, 0, cube_5_yAngle, 0, 1, 0);
        Matrix.translateM(mMMatrix,0,2,0,0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, getFianlMatrix(mMMatrix), 0);

        cubePositionsBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubePositionsBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        cubeColorsBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeColorsBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);
        cubeTextureCoordinatesBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE,
                GLES20.GL_FLOAT, false, 0, cubeTextureCoordinatesBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        if (cube_5_yAngle <0) {
            cube_5_yAngle += yAngle_add;
        }
    }

    public float[] getFianlMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public int getFPS() {
        return fps;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub
        GLES20.glViewport(0, 0, width, height);
        final float ratio = (float) width / height;
        float fangda=1f;
        final float left = -ratio*fangda;
        final float right = ratio*fangda;
        final float bottom = -1.5f;
        final float top = 0.5f;
        final float near = 2f;
        final float far = 10.0f;
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
//        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glEnable(GLES20.GL_BLEND);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 1f, 8f,
                0, 0, -3f, 0, 1, 0);
        final String vertexShader = getVertexShader();
        final String fragmentShader = getFragmentShader();
        final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
//        gl.glAlphaFunc(gl.GL_GREATER, 0);
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE);
        mProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Color", "a_TexCoordinate"});
        mTextureDataHandle[0] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_0);
        mTextureDataHandle[1] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_1);
        mTextureDataHandle[2] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_2);
        mTextureDataHandle[3] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_3);
        mTextureDataHandle[4] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_4);
        mTextureDataHandle[5] = ToolsUtil.loadTexture(mContext, R.drawable.keyboard_5);
    }

    private String getVertexShader() {
        final String vertexShader =
                "uniform mat4 u_MVPMatrix; \n" // A constant representing the combined model/view/projection matrix.
                        + "attribute vec4 a_Position; \n" // Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color; \n" // Per-vertex color information we will pass in.
                        + "attribute vec2 a_TexCoordinate;\n" // Per-vertex texture coordinate information we will pass in.
                        + "varying vec4 v_Color; \n" // This will be passed into the fragment shader.
                        + "varying vec2 v_TexCoordinate; \n" // This will be passed into the fragment shader.
                        + "void main() \n" // The entry point for our vertex shader.
                        + "{ \n"
                        + " v_Color = a_Color; \n" // Pass the color through to the fragment shader.
                        // It will be interpolated across the triangle.
                        + " v_TexCoordinate = a_TexCoordinate;\n"// Pass through the texture coordinate.
                        + " gl_Position = u_MVPMatrix \n" // gl_Position is a special variable used to store the final position.
                        + " * a_Position; \n" // Multiply the vertex by the matrix to get the final point in
                        + "} \n"; // normalized screen coordinates. \n";
        return vertexShader;
    }

    private String getFragmentShader() {
        final String fragmentShader = "precision mediump float; \n"
                // Set the default precision to medium. We don't need as high of a
                // precision in the fragment shader.
                + "uniform sampler2D u_Texture; \n" // The input texture.
                + "varying vec4 v_Color; \n" // This is the color from the vertex shader interpolated across the
                // triangle per fragment.
                + "varying vec2 v_TexCoordinate; \n" // Interpolated texture coordinate per fragment.
                + "void main() \n" // The entry point for our fragment shader.
                + "{ \n"
//                + " gl_FragColor = v_Color * texture2D(u_Texture, v_TexCoordinate); \n" // Pass the color directly through the pipeline.
                + " gl_FragColor = texture2D(u_Texture, v_TexCoordinate); \n" // Pass the color directly through the pipeline.
                + "} \n";
        return fragmentShader;
    }

    private int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }
        return shaderHandle;
    }

    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }
            GLES20.glLinkProgram(programHandle);
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }
        return programHandle;
    }

    static class ToolsUtil {
        public static int loadTexture(final Context context, final int resourceId) {
            final int[] textureHandle = new int[1];
            GLES20.glGenTextures(1, textureHandle, 0);
            if (textureHandle[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                bitmap.recycle();
            }


            if (textureHandle[0] == 0) {
                throw new RuntimeException("failed to load texture");
            }
            return textureHandle[0];
        }
    }

    class FPSCounter {
        int FPS;
        int lastFPS;
        long tempFPStime;

        public FPSCounter() {
            FPS = 0;
            lastFPS = 0;
            tempFPStime = 0;
        }

        int getFPS() {
            long nowtime = SystemClock.uptimeMillis();
            FPS++;
            if (nowtime - tempFPStime >= 1000) {
                lastFPS = FPS;
                tempFPStime = nowtime;
                FPS = 0;
                //Log.d("FPSCounter","fps="+lastFPS);
            }
            return lastFPS;
        }
    }
}  