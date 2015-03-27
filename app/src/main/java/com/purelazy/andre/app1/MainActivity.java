
package com.purelazy.andre.app1;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

     static class MyToast {
        static void toast(Context context, String string) {

            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, string, duration);
            toast.show();
        }
    }

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // What does this do?
        super.onCreate(savedInstanceState);

        // Check for OpenGL ES 2.0
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        if (info.reqGlEsVersion >= 0x20000) {
            MyToast.toast(this, "OpenGL ES 2.0 is supported on this device. Cool.");
        } else {
            MyToast.toast(this, "Sorry, OpenGL ES 2.0 is not supported on this device.");
        }

        // Create a GLSurfaceview
        mGLView = new MyGLSurfaceView(this);

        // Set the surface view.
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}