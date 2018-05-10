package com.comet.myopengl.zero;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.comet.myopengl.R;

public class OpenGLDemoActivity extends Activity {
    MyGLSurfaceView gv_one;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        String s = info.reqGlEsVersion + "";
        String ver = Integer.toHexString(Integer.parseInt(s));
        Log.d("reqGlEsVersion = ", ver);
        GLImage.load(getResources());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);
        gv_one = findViewById(R.id.gv_one);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gv_one.changeAnimatorStatus(1);
            }
        }, 1000);
    }
}
