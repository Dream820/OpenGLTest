package com.comet.myopengl.two;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.comet.myopengl.R;

public class Activity01 extends Activity {
    GLSurfaceView.Renderer render = new GLRender();
    GLSurfaceView glView;
    Button start;           // 演示开始


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLImage.load(this.getResources());
        glView = new GLSurfaceView(this);


        glView.setRenderer(render);
        setContentView(R.layout.main);
        start = (Button) findViewById(R.id.button1);   // "演示开始"按钮初始化
        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub  
                setContentView(glView);
            }
        });
        //setContentView(glView);  
    }
}



  
