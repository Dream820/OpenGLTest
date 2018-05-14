package com.comet.myopengl.study;

import android.opengl.GLSurfaceView;

import com.comet.myopengl.MyApplication;
import com.comet.myopengl.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;

public class FirstOpenGLProjectRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2;
    float[] tableVertices = {
            0f, 0f,
            0f, 14f,
            9f, 14f,

            0f, 0f,
            9f, 0f,
            9f, 14f
    };
    FloatBuffer vertexData;

    public FirstOpenGLProjectRenderer() {
        vertexData = ByteBuffer.allocate(tableVertices.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVertices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1f, 0f, 0f, 0f);

        String vertexShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_vertex_shader);//读取顶点着色器
        String fragmentShaderSource = TextResourceReader.
                readTextFileFromResource(MyApplication.myApplication,
                        R.raw.simple_fragment_shader);//读取片段着色器
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT);
    }
}
