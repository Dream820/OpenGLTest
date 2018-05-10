package com.comet.myopengl.cube1;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.comet.myopengl.zero.GLImage;

public class SimpleOpenGLActivity extends Activity {
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        GLSurfaceView   glView = new GLSurfaceView(this);
        glView.setRenderer(new GLReader());
        setContentView(glView);

    }  
} 