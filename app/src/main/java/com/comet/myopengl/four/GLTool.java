package com.comet.myopengl.four;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GLTool {
/*
 *返回字节化的intbuffer
 */
    public static IntBuffer getIntBu(int[] source){
        ByteBuffer bb = ByteBuffer.allocateDirect(source.length*4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer vertices = bb.asIntBuffer();
        vertices.put(source);
        vertices.position(0);
        return vertices;
    }
     
}