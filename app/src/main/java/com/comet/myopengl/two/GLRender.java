package com.comet.myopengl.two;

        import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRender implements Renderer {
    float xrot, yrot, zrot;
    int texture = -1;
    int one = 0x10000;

    IntBuffer intBuffer = IntBuffer.allocate(2);
    int n_view_start_x = 100;
    int n_view_start_y = 20;
    int n_close_flag = 0; // 1: close_icon show. 0: close_icon hide.  
    public int textureId;

    IntBuffer vertices = BufferUtil.iBuffer(new int[]{
            -one, -one, one,
            one, -one, one,
            one, one, one,
            -one, one, one,

            -one, -one, -one,
            -one, one, -one,
            one, one, -one,
            one, -one, -one,

            -one, one, -one,
            -one, one, one,
            one, one, one,
            one, one, -one,

            -one, -one, -one,
            one, -one, -one,
            one, -one, one,
            -one, -one, one,

            one, -one, -one,
            one, one, -one,
            one, one, one,
            one, -one, one,

            -one, -one, -one,
            -one, -one, one,
            -one, one, one,
            -one, one, -one,

    });
    IntBuffer texCoords = BufferUtil.iBuffer(new int[]{
            one, 0, 0, 0, 0, one, one, one,
            0, 0, 0, one, one, one, one, 0,
            one, one, one, 0, 0, 0, 0, one,
            0, one, one, one, one, 0, 0, 0,
            0, 0, 0, one, one, one, one, 0,
            one, 0, 0, 0, 0, one, one, one,
    });

    ByteBuffer indices = BufferUtil.bBuffer(new byte[]{
            0, 1, 3, 2,
            4, 5, 7, 6,
            8, 9, 11, 10,
            12, 13, 15, 14,
            16, 17, 19, 18,
            20, 21, 23, 22,
    });

    ByteBuffer indices_0 = BufferUtil.bBuffer(new byte[]{
            0, 1, 3, 2
    });

    ByteBuffer indices_1 = BufferUtil.bBuffer(new byte[]{
            4, 5, 7, 6
    });

    ByteBuffer indices_2 = BufferUtil.bBuffer(new byte[]{
            8, 9, 11, 10
    });

    ByteBuffer indices_3 = BufferUtil.bBuffer(new byte[]{
            12, 13, 15, 14
    });

    ByteBuffer indices_4 = BufferUtil.bBuffer(new byte[]{
            16, 17, 19, 18
    });

    ByteBuffer indices_5 = BufferUtil.bBuffer(new byte[]{
            20, 21, 23, 22
    });

    //正方形的4个顶点     
    private IntBuffer quaterBuffer = BufferUtil.iBuffer(new int[]{
            one, one, 0,
            -one, one, 0,
            one, -one, 0,
            -one, -one, 0});
    IntBuffer texCoords_rect = BufferUtil.iBuffer(new int[]{
            0, 1, 1, 1, 1, 0, 0, 0,
    });

    @Override
    public void onDrawFrame(GL10 gl) {
        // 1.清除屏幕和深度缓存  
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // 2.重置当前的模型观察矩阵  
        gl.glLoadIdentity();
        // 3.移动到指定的位置  
        gl.glTranslatef(0.0f, 0.0f, -5.0f);

        // 4.设置3个方向的旋转  
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);

        texture = intBuffer.get(1);
        ;
        //  5.绑定纹理  
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);

        // 6.开启顶点和纹理功能  
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        // 7.纹理和四边形对应的顶点  
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, vertices);
        gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoords);

        // 8.绘制0  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.iBitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_0);

        //绘制1  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.jBitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_1);

        //绘制2  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.kBitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_2);

        //绘制3  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.lBitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_3);

        //绘制4  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_4);

        //绘制5  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.close_Bitmap, 0);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indices_5);

        if (0 == n_close_flag) {

            // 重置当前的模型观察矩阵     
            gl.glLoadIdentity();
            // 左移 1.5 单位，并移入屏幕 5.0     
            gl.glTranslatef(-4.5f, 3.0f, -5.0f);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            texture = intBuffer.get(0);
            // 绑定纹理  
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.close_Bitmap, 0);

            // 绘制关闭按钮  
            // 设置和绘制正方形     
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, quaterBuffer);
            gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoords_rect);

            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            //gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,  GL10.GL_UNSIGNED_BYTE, indices_5);  

            n_close_flag = 1;
        }

        // 9.关闭顶点和纹理功能  
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        xrot += 0.5f;
        yrot += 0.6f;
        zrot += 0.3f;

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float ratio = (float) (width - n_view_start_x) / (height - n_view_start_x);
        //设置OpenGL场景的大小  
        //gl.glViewport(0, 0, width, height);  
        gl.glViewport(n_view_start_x, n_view_start_y, width - n_view_start_x, height - n_view_start_y);

        //设置投影矩阵  
        gl.glMatrixMode(GL10.GL_PROJECTION);
        //重置投影矩阵  
        gl.glLoadIdentity();
        // 设置视口的大小  
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        // 选择模型观察矩阵  
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // 重置模型观察矩阵  
        gl.glLoadIdentity();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 绿色背景  
        gl.glClearColor(0, 1, 0, 0);

        gl.glEnable(GL10.GL_CULL_FACE);
        // 启用阴影平滑  
        gl.glShadeModel(GL10.GL_SMOOTH);
        // 启用深度测试  
        gl.glEnable(GL10.GL_DEPTH_TEST);

        //启用纹理映射  
        gl.glClearDepthf(1.0f);
        //深度测试的类型  
        gl.glDepthFunc(GL10.GL_LEQUAL);
        //精细的透视修正  
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  
          
/*       
        // 1.允许2D贴图,纹理 
        gl.glEnable(GL10.GL_TEXTURE_2D); 
         
        IntBuffer intBuffer = IntBuffer.allocate(1); 
        // 2.创建纹理 
        gl.glGenTextures(1, intBuffer); 
        texture = intBuffer.get(); 
        // 3.设置要使用的纹理 
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture); 
         
        // 4.生成纹理 
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.mBitmap, 0); 
         
        // 5.线形滤波 
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); 
*/

        // 1.允许2D贴图,纹理  
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // 2.创建纹理  
        gl.glGenTextures(2, intBuffer);

        texture = intBuffer.get(0);
        // 3.设置要使用的纹理  
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        // 4.生成纹理  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.close_Bitmap, 0);

        texture = intBuffer.get(1);
        // 3.设置要使用的纹理  
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
        // 4.生成纹理  
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, GLImage.nBitmap, 0);

        // 5.线形滤波  
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

    }

} 