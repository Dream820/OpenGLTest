package com.comet.myopengl.gl2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.comet.myopengl.zero.GLImage;

public class CuneActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        GLSurfaceView glView = new GLCubeView(this);
        setContentView(glView);
    }
} 