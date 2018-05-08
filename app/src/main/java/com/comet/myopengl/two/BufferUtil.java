package com.comet.myopengl.two;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {


    public static FloatBuffer fBuffer(float[] a) {
        // 先初始化buffer,数组的长度*4,因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
        mbb.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = mbb.asFloatBuffer();
        floatBuffer.put(a);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static IntBuffer iBuffer(int[] a) {
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length * 4);
        mbb.order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = mbb.asIntBuffer();
        intBuffer.put(a);
        intBuffer.position(0);
        return intBuffer;
    }


    public static ByteBuffer bBuffer(byte[] a) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(a.length);
        byteBuffer.put(a);
        byteBuffer.position(0);
        return byteBuffer;
    }
}