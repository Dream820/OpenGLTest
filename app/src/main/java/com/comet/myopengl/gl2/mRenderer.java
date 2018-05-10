package com.comet.myopengl.gl2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class mRenderer implements GLSurfaceView.Renderer {

    private Cube cube1;
    private Cube cube2;
    private Cube cube3;
    //private final float[] projectionMatrix1 = new float[16];
    private final float[] projectionMatrix2 = new float[16];
    //private final float[] projectionMatrix3 = new float[16];
    private float aspectRatio;
    int z = 0;
    int a = 1;
    int b = 1;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1f);
        // enable face culling feature
        gl.glEnable(GL10.GL_CULL_FACE);
        // specify which faces to not draw
        gl.glCullFace(GL10.GL_BACK);
        //cube1 = new Cube(1);
        cube2 = new Cube(2);
        //cube3 = new Cube(3);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        aspectRatio = (float) width / height;
        System.out.print("aspectRatio= ");
        System.out.println(aspectRatio);

        //Matrix.orthoM(projectionMatrix1, 0, -aspectRatio * 1, aspectRatio * 1, -1f, 1f, -1f, 1f);
        Matrix.orthoM(projectionMatrix2, 0, -aspectRatio * 1, aspectRatio * 1, -1f, 1f, -1f, 1f);
        //Matrix.orthoM(projectionMatrix3, 0, -aspectRatio * 5, aspectRatio * 5, -5f, 5f, -5f, 5f);
        //prespectiveM(projectionMatrix, 90, aspectRatio, 1f, -1f);
        //Matrix.translateM(projectionMatrix1, 0, 0.0f, 3.0f, 0.0f);
        //Matrix.rotateM(projectionMatrix1, 0, 1, 10.0f, 1.0f, 0.0f);
        //GLES20.glFrontFace(GLES20.GL_CW);
        printMatrix(projectionMatrix2);
        //printMatrix(projectionMatrix3);
        //GLES20.glUniformMatrix4fv(cube1.maker.matrixPosition, 1, false, projectionMatrix1, 0);
        GLES20.glUniformMatrix4fv(cube2.maker.matrixPosition, 1, false, projectionMatrix2, 0);
        //GLES20.glUniformMatrix4fv(cube3.maker.matrixPosition, 1, false, projectionMatrix3, 0);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        spin();
        //cube1.draw();
        cube2.draw();
        //cube3.draw();
    }

    private void printMatrix(float[] matrix) {
        System.out.println("***matrix***");
        int x = 0;
        for (float i : matrix) {
            System.out.print(i);
            System.out.print("  ");
            if (x == 3) {
                System.out.println("");
                x = 0;
            } else x++;
        }
        System.out.println(".................");
    }

    private void spin() {
        if (z == 500) {
            b *= -1;
        }
        if (1000 == z) {
            z = 0;
            a *= -1;
        }
        z++;
        /*Matrix.rotateM(projectionMatrix1, 0, 1, 0.0f, a * 0.001f, b * 0.001f);
        //prespectiveM(projectionMatrix, 45, aspectRatio, 5f, -5f);
        GLES20.glUniformMatrix4fv(cube1.maker.matrixPosition, 1, false, projectionMatrix1, 0);*/


        Matrix.rotateM(projectionMatrix2, 0, 1, 0.0f, a * 0.001f, b * 0.001f);
        //prespectiveM(projectionMatrix, 45, aspectRatio, 5f, -5f);
        GLES20.glUniformMatrix4fv(cube2.maker.matrixPosition, 1, false, projectionMatrix2, 0);

        //Matrix.rotateM(projectionMatrix3, 0, 1, 0.0f, -a * 0.001f, b * 0.001f);
        //prespectiveM(projectionMatrix, 45, aspectRatio, 5f, -5f);
        //GLES20.glUniformMatrix4fv(cube3.maker.matrixPosition, 1, false, projectionMatrix3, 0);

    }

    private void prespectiveM(float[] m, float yFovInDegrees, float aspect,
                              float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;
    }
}