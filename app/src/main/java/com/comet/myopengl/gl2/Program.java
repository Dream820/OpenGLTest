package com.comet.myopengl.gl2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Program {

    public int program;
    public int colorPosition;
    public final int matrixPosition;
    private int VertexShader;
    private int FragmentShader;

    public Program(float[] vertexLit){
        //type1
        make1();
        //this matrix is useless...why?
        matrixPosition = GLES20.glGetUniformLocation(program, "u_Matrix");
        System.out.print("matrix Position= ");
        System.out.println(matrixPosition);
        int vertPosition = GLES20.glGetAttribLocation(program, "vPosition");
        makeVertexBuffer(vertexLit, vertPosition);
    }

    public Program(float[] vertexLit, float[] colorList){
        //type2
        make2();
        matrixPosition = GLES20.glGetUniformLocation(program, "u_Matrix");
        System.out.print("matrix Position= ");
        System.out.println(matrixPosition);
        int vertPosition = GLES20.glGetAttribLocation(program, "aPosition");
        makeVertexBuffer(vertexLit, vertPosition);
        makeColorBuffer(colorList, colorPosition);
    }

    private int makeShader(int type, String source){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        System.out.print("shader id= ");
        System.out.println(shader);
        return shader;
    }

    private int makeProgram(int vShader, int fShader){
        int prgrm = GLES20.glCreateProgram();
        GLES20.glAttachShader(prgrm, vShader);
        GLES20.glAttachShader(prgrm, fShader);
        GLES20.glLinkProgram(prgrm);
        GLES20.glUseProgram(prgrm);
        System.out.print("program id= ");
        System.out.println(prgrm);
        return prgrm;
    }

    private void makeVertexBuffer(float[] vertexList, int vp){
        final int bytes_per_float = 4;
        final int Variable_Count = 3;
        final int VertexStride = 0;
        FloatBuffer vertexData = ByteBuffer
                .allocateDirect(vertexList.length * bytes_per_float)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexList);
        vertexData.position(0);
        GLES20.glEnableVertexAttribArray(vp);
        GLES20.glVertexAttribPointer(vp, Variable_Count,
                GLES20.GL_FLOAT, false, VertexStride, vertexData);
    }

    private void makeColorBuffer(float[] colorList, int cp){
        final int bytes_per_float = 4;
        final int Variable_Count = 4;
        final int stride = 0;
        FloatBuffer colorData = ByteBuffer
                .allocateDirect(colorList.length * bytes_per_float)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(colorList);
        colorData.position(0);
        GLES20.glEnableVertexAttribArray(cp);
        GLES20.glVertexAttribPointer(cp, Variable_Count,
                GLES20.GL_FLOAT, false, stride, colorData);
    }

    private void make1(){

        final String VertexSource =
                "uniform mat4 u_Matrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "gl_Position = u_Matrix * vPosition;" +
                "}";

        final String FragmentSource =
                "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";

        VertexShader = makeShader(GLES20.GL_VERTEX_SHADER, VertexSource);
        FragmentShader = makeShader(GLES20.GL_FRAGMENT_SHADER, FragmentSource);
        program = makeProgram(VertexShader, FragmentShader);
        colorPosition = GLES20.glGetUniformLocation(program, "vColor");
    }

    private void make2(){
        final String VertexSource =
                "uniform mat4 u_Matrix;" +
                "attribute vec4 aPosition;" +
                "attribute vec4 aColor;" +
                "varying vec4 v_Color;" +
                "void main() {" +
                "gl_Position = u_Matrix * aPosition;" +
                "v_Color = aColor;" +
                "}";

        final String FragmentSource =
                "precision mediump float;" +
                "varying vec4 v_Color;" +
                "void main() {" +
                "gl_FragColor = v_Color;" +
                "}";

        VertexShader = makeShader(GLES20.GL_VERTEX_SHADER, VertexSource);
        FragmentShader = makeShader(GLES20.GL_FRAGMENT_SHADER, FragmentSource);
        program = makeProgram(VertexShader, FragmentShader);
        colorPosition = GLES20.glGetAttribLocation(program, "aColor");
    }
}