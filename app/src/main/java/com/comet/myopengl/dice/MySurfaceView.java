package com.comet.myopengl.dice;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MySurfaceView extends GLSurfaceView {
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setRenderer(new Myrender());
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
