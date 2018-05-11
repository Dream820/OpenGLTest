package com.comet.myopengl.cubeok;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.comet.myopengl.zero.GLImage;

public class GLActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView surfaceView = new GLSurfaceView(this);
        surfaceView.setRenderer(new GLRendererEx(this));
        setContentView(surfaceView);
    }
} 