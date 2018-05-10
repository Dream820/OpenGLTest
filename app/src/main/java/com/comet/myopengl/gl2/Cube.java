package com.comet.myopengl.gl2;

import android.opengl.GLES20;

public class Cube {

    private final float[] points = {
            -0.5f,  0.5f, -0.5f,//a-0
             0.5f,  0.5f, -0.5f,//b-1
             0.5f,  0.5f,  0.5f,//c-2
            -0.5f,  0.5f,  0.5f,//d-3
            -0.5f, -0.5f, -0.5f,//e-4
             0.5f, -0.5f, -0.5f,//f-5
             0.5f, -0.5f,  0.5f,//g-6
            -0.5f, -0.5f,  0.5f,//h-7
    };

    private final float[] ColorList = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f,
            0.8f, 0.3f, 0.4f, 1.0f,
            0.5f, 0.0f, 0.3f, 1.0f,
            0.0f, 5.0f, 5.0f, 1.0f,
            0.3f, 1.0f, 0.3f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
    };

    private final float[] VertexCoordinates = new float[12 * 9];

    public Program maker;
    private int type;

    public Cube(int type){
        this.type = type;
        makeList();
        if (type == 1 || type == 3) maker = new Program(VertexCoordinates);
        else if (type == 2) maker = new Program(VertexCoordinates, ColorList);
    }

    public void draw(){

        if (type == 1){
            GLES20.glUniform4f(maker.colorPosition, 1.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            GLES20.glUniform4f(maker.colorPosition, 0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 6, 6);

            GLES20.glUniform4f(maker.colorPosition, 0.0f, 0.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 12, 6);

            GLES20.glUniform4f(maker.colorPosition, 1.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 18, 6);

            GLES20.glUniform4f(maker.colorPosition, 0.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 24, 6);

            GLES20.glUniform4f(maker.colorPosition, 1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 30, 6);
        }

        else if (type == 2){
            //GLES20.glUniform4f(maker.colorPosition, 1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        }

        else if (type == 3){
            //GLES20.glUniform4f(maker.colorPosition, 1.0f, 1.0f, 1.0f, 1.0f);
            GLES20.glDrawArrays(GLES20.GL_LINES, 0, 36);
        }
        //GLES20.glDisableVertexAttribArray(maker.vertPosition);
    }

    private void makeList(){
        int[] order = {
                0, 4, 1,
                4, 5, 1,
                1, 5, 2,
                5, 6, 2,
                2, 6, 3,
                6, 7, 3,
                5, 4, 6,
                4, 7, 6,
                4, 0, 7,
                0, 3, 7,
                0, 1, 3,
                1, 2, 3,};
        int VCIndex=0;
        for (int number : order) {
            for (int variable = 0; variable < 3; variable++){
                VertexCoordinates[VCIndex] = points[3 * number + variable];
                VCIndex++;
            }
        }
    }
}