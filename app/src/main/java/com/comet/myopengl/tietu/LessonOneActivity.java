package com.comet.myopengl.tietu;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.comet.myopengl.R;

public class LessonOneActivity extends AppCompatActivity {
    /**
     * Hold a reference to our GLSurfaceView
     */
    private MyGLSurfaceView mGLSurfaceView;
//    LessonOneRenderer mRender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_guide_theme);
//        mGLSurfaceView=findViewById(R.id.mzOpenGLView);

        mGLSurfaceView = new MyGLSurfaceView(this);

        LinearLayout mzLayout=findViewById(R.id.guide_theme_gl_surface_view_layout);
        mzLayout.addView(mGLSurfaceView);

        // Check if the system supports OpenGL ES 2.0.  
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.  
//            mGLSurfaceView.setEGLContextClientVersion(2);

//            mRender = new LessonOneRenderer(this);
            // Set the renderer to our demo renderer, defined below.  
//            mGLSurfaceView.setRenderer(mRender);
        } else {
            // This is where you could create an OpenGL ES 1.x compatible  
            // renderer if you wanted to support both ES 1 and ES 2.  
            return;
        }

        handler.postDelayed(runnable, 1000);


    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            // TODO Auto-generated method stub  
            //要做的事情  
            handler.postDelayed(this, 1000);
            //Log.v("timer",runTop(TOP));  
            //setTile();  
            showFPS(mGLSurfaceView.lessonOneRenderer.getFPS());
        }
    };

    private void showFPS(int fps) {
        this.setTitle("fps:" + fps);
    }

    @Override
    protected void onResume() {
        // The activity must call the GL surface view's onResume() on activity onResume().  
        super.onResume();
        try {
            mGLSurfaceView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        // The activity must call the GL surface view's onPause() on activity onPause().  
        super.onPause();
        try {
            mGLSurfaceView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  