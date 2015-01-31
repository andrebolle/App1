package com.purelazy.andre.app1;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by andre on 31/01/15.
 */
class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(new MyGLRenderer());
    }
}
