package com.comet.myopengl.cube1;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class SimpleOpenGLActivity extends Activity {
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GLSurfaceView   glView = new GLSurfaceView(this);
        glView.setRenderer(new GLReader());
        setContentView(glView);

    }  
} 