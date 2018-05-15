package com.comet.myopengl.study;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.comet.myopengl.MyApplication;
import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COMOPNENT_COUNT = 2;//数量 3 xyz 4xyzc
    private static final int BYTES_PER_FLOAT = 4;
    private float[] tableVertices = {
            -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f,

            -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,

            -0.5f, 0f, 0.5f, 0f,

            0f, -0.25f, 0f, 0.25f,

            -0.6f, -0.6f, 0.6f, 0.6f, -0.6f, 0.6f,

            -0.6f, -0.6f, 0.6f, -0.6f, 0.6f, 0.6f,
    };
    private FloatBuffer vertexData;
    private static final String A_POSITION = "a_Position";
    private int aPostionLocation;
    private int uColorLocation;

    private static final String U_COLOR = "u_Color";
    public FirstOpenGLProjectRenderer() {
        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_vertex_shader);//读取顶点着色器
        String fragmentShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_fragment_shader);//读取片段着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);//返回shader位置 定点着色器写入 编译
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);//解析着色器
        int program = ShaderHelper.linkProgram(vertexShader, fragmentShader);//生成一个工程 将着色器传入

        GLES20.glUseProgram(program);//使用工程

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);//返回着色器参数 在缓冲区中的位置
        aPostionLocation = GLES20.glGetAttribLocation(program, A_POSITION);//返回着色器参数 在缓冲区中的位置

        vertexData.position(0);

        GLES20.glVertexAttribPointer(aPostionLocation, POSITION_COMOPNENT_COUNT,
                GLES20.GL_FLOAT, false,
                0, vertexData);// 指定了渲染时索引值为 aPostionLocation 的顶点属性数组的数据格式和位置
        GLES20.glEnableVertexAttribArray(
                aPostionLocation);// Enable or disable a generic vertex attribute array

        Log.d("GLES20", "11111");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f,
                1.0f);//为 u_Color 这个 Uniform 设置颜色值 RGB 为 0 1 0 1 绿色
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 10, 6);//画三角形

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
