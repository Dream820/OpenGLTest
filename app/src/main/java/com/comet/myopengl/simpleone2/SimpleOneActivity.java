package com.comet.myopengl.simpleone2;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.comet.myopengl.zero.GLImage;

public class SimpleOneActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        GLSurfaceView glView = new GLSurfaceView(this);
        glView.setRenderer(new SimpleRender2());
        setContentView(glView);
    }
}
