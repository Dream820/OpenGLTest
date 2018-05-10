package com.comet.myopengl.simpleone;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class SimpleOneActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new GLSurfaceView(this);

        glView.setRenderer(new SimpleRender());
        setContentView(glView);
    }
}
