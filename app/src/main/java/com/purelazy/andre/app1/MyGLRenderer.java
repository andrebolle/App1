/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.purelazy.andre.app1;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private Triangle mTriangle;
    private Square   mSquare;
    private Line     mLine;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] P = new float[16];
    private final float[] V = new float[16];
    private final float[] PV = new float[16];
    private final float[] PVM = new float[16];

    float[] T = new float[16]; // Translation
    float[] R = new float[16]; // Rotation
    float[] S = new float[16]; // Scale

    float[] TR = new float[16]; // Scale
    float[] M = new float[16]; // Model (Scale, Rotate, Translate)

    private final float[] mRotationMatrix = new float[16];

    float aspectRatio;

    private float mAngle;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle();
        mSquare   = new Square();
        mLine     = new Line();
    }

    public static void gluPerspective(float[] m, float fovy, float aspect,
                                      float zNear, float zFar) {
        float top = zNear * (float) Math.tan(fovy * (Math.PI / 360.0));
        float bottom = -top;
        float left = bottom * aspect;
        float right = top * aspect;
        Matrix.frustumM(m, 0, left, right, bottom, top, zNear, zFar);
    }



    @Override
    public void onDrawFrame(GL10 gl) {


        //Matrix.frustumM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0.2f, 7f);

        // frustumM and gluPerspective


        //Log.e(TAG, ": near " + near);


        float[] scratch = new float[16];
        float[] rotationMatrix = new float[16];
        float[] modelMatrix = new float[16];
        float[] VM = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        /*
         * Defines a viewing transformation in terms of an eye point, a center of
         * view, and an up vector.
         *
         *  rm returns the result
         *  rmOffset index into rm where the result matrix starts
         *  eyeX, eyeY, eyeZ, atX, atY, atZ, upX, upY, upZ
         */

        // FOV, aspect, near & far clipping planes
        gluPerspective(P, 120f, aspectRatio, 1f, 70f);
        // Stand at the +Z, looking at origin
        Matrix.setLookAtM(V, 0,
                0f, 0f, 7f,          // eye
                0f, 0f, 0f,         // loot at point
                0f, 1f, 0f);        // Up vector


        Matrix.setIdentityM(M, 0);

/*

        for (int y=1; y < 11; y++) {
            for (int x = 1; x < 21; x++) {

                // Move your object to the right position (model transformation)
                Matrix.translateM(M, 0, 1.1f, 0f, 0f);
                Matrix.multiplyMM(VM, 0, V, 0, M, 0);
                Matrix.multiplyMM(PVM, 0, P, 0, VM, 0);
                mSquare.draw(PVM);

            }
            Matrix.translateM(M, 0, -22f, -1.1f, 0f);
        }

*/
        // Draw a litte arrow!
        Matrix.multiplyMM(PV, 0, P, 0, V, 0);

        mLine.draw(PV);

        Matrix.setIdentityM(S, 0);
        Matrix.scaleM(S, 0, 0.5f, 0.5f, 0.5f);

        Matrix.setIdentityM(R, 0);
        Matrix.rotateM(R, 0, 135f, 0f, 0f, 1f);

        Matrix.setIdentityM(T, 0);
        Matrix.translateM(T, 0, 1f, 0f, 0f);

        Matrix.multiplyMM(TR, 0, T, 0, R, 0);
        Matrix.multiplyMM(M, 0, TR, 0, S, 0);
        Matrix.multiplyMM(PVM, 0, PV, 0, M, 0);

        mLine.draw(PVM);

        Matrix.setIdentityM(S, 0);
        Matrix.scaleM(S, 0, 0.5f, 0.5f, 0.5f);

        Matrix.setIdentityM(R, 0);
        Matrix.rotateM(R, 0, -135f, 0f, 0f, 1f);

        Matrix.setIdentityM(T, 0);
        Matrix.translateM(T, 0, 1f, 0f, 0f);

        Matrix.multiplyMM(TR, 0, T, 0, R, 0);
        Matrix.multiplyMM(M, 0, TR, 0, S, 0);
        Matrix.multiplyMM(PVM, 0, PV, 0, M, 0);

        mLine.draw(PVM);


        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        //Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.

        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        // Draw triangle

        //mTriangle.draw(scratch);
    }

    /*
    The BIG Picture & Normalized device coordinates

    OpenGL 2.0 does not know anything about your coordinate space or
    about the matrices that you’re using. OpenGL only requires that, when all of your
    transformations are done, coordinates are in the range -1 to +1.
    Normalized Device Coordinates (NDC).

    These coordinates range from -1 to +1 on each axis, regardless
    of the shape or size of the actual screen. The bottom left
    corner will be at (-1, -1), and the top right corner will be at (1, 1).

    OpenGL will then map these coordinates onto the viewport that was
    configured with glViewport. The underlying operating system’s
    window manager will then map that viewport to the appropriate place on the screen.

     */

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        aspectRatio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        /*
         frustumM(float[] matrix, int offsetIntroMatrix,
                  float left,   float right,
                  float bottom, float top,
                  float near,   float far)

         Defines a projection matrix in terms of six clip planes.
         */
        Matrix.frustumM(P, 0, -aspectRatio, aspectRatio, -1, 1, 2, 7);
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        if ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}