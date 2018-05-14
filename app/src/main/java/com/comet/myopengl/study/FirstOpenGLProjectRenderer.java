package com.comet.myopengl.study;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.comet.myopengl.MyApplication;
import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glVertexAttribPointer;

public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;//数量 3 xyz 4xyzc
    private float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,

            0f, 0f,
            9f, 0f,
            9f, 14f
    };
    private FloatBuffer vertexData;
    private static final String A_POSITION = "a_Position";
    private int aPostionLocation;
    private int uColorLocation;

    private static final String U_COLOR = "u_Color";
    public FirstOpenGLProjectRenderer() {
        vertexData = ByteBuffer.allocate(tableVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_vertex_shader);//读取顶点着色器
        String fragmentShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_fragment_shader);//读取片段着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//返回shader位置
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//解析着色器
        int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);//生成一个工程 将着色器传入

        GLES20.glUseProgram(program);//使用工程

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);//返回着色器参数 在缓冲区中的位置
        aPostionLocation = GLES20.glGetUniformLocation(program, A_POSITION);//返回着色器参数 在缓冲区中的位置

        vertexData.position(0);

        glVertexAttribPointer(aPostionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                false
                , 0, vertexData);//将位置参数绑定 着色器
        glEnableVertexAttribArray(aPostionLocation);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GL_COLOR_BUFFER_BIT);
        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f,
                1.0f);//为 u_Color 这个 Uniform 设置颜色值 RGB 为 0 1 0 1 绿色
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 10, 6);//画三角形

        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

    }
}
