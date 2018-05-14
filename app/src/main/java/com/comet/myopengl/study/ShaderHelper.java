package com.comet.myopengl.study;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int glVertexShader, String shaderCode) {
        final int shaderObjectid = glCreateShader(glVertexShader);//生成shader的缓存
        if (shaderObjectid == 0) {
            return 0;
        }
        //把字符串转换成shader 并绑定id
        glShaderSource(shaderObjectid, shaderCode);//shader的缓存 绑定shader规则

        glCompileShader(shaderObjectid);//把shader缓存 绑定的规则 compile
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectid, GL_COMPILE_STATUS, compileStatus, 0);
        Log.d("compileShader", shaderCode + "||" + glGetShaderInfoLog(shaderObjectid));
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectid);
        }
        return shaderObjectid;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderId);    //将
        glAttachShader(programObjectId, fragmentShaderId);
        glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        Log.d("linkProgram", "||" + glGetShaderInfoLog(programObjectId));

        if (linkStatus[0] == 0) {
            glDeleteShader(programObjectId);
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        return validateStatus[0] != 0;
    }


}
