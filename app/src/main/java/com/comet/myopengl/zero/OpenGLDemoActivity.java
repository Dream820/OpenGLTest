package com.comet.myopengl.zero;

import android.app.Activity;
import android.os.Bundle;

import com.comet.myopengl.R;

public class OpenGLDemoActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(getResources());
        setContentView(R.layout.activity_main2);

    }
}
