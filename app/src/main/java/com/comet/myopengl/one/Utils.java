package com.comet.myopengl.one;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Utils {

    public static final int BYTES_PER_FLOAT = 4;

    private static final String TAG = "Utils";

    private Utils() {
        // util
    }

    public static String loadShader(Context context, int resId) {
        StringBuilder builder = new StringBuilder();

        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line)
                        .append('\n');
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static int loadTexture(Context context, int resId) {
        int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            Log.e(TAG, "Could not generate a new OpenGL texture object.");
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            Log.e(TAG, "Resource ID " + resId + " could not be decoded.");
            GLES20.glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }

        // bind
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        // unbind
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    public static boolean supportGlEs20(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(
                Context.ACTIVITY_SERVICE);
        return activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;
    }
}