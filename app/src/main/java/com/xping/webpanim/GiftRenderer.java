package com.xping.webpanim;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xpmbp on 2016/10/30.
 */

public class GiftRenderer implements GLSurfaceView.Renderer{

    Context context; // Application's context
    private int width = 0;
    private int height = 0;

    private GLBitmap glBitmap;
    private Bitmap bitmap;

    private long frameSeq = 0;
    private int viewportOffset = 0;
    private int maxOffset = 10;

    private boolean isClearScreen = false;

    public GiftRenderer(Context context) {
        this.context = context;
        glBitmap = new GLBitmap();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
        gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Background
        gl.glClearDepthf(1.0f); // Depth Buffer Setup
        gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
        gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) {
            height = 1;
        }
        this.width = width;
        this.height = height;

        gl.glViewport(0, 0, width, height); // Reset The Current Viewport

        gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
        gl.glLoadIdentity(); // Reset The Projection Matrix

        // Calculate The Aspect Ratio Of The Window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 1000f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        if(bitmap==null || bitmap.isRecycled() || isClearScreen==true){
            return;
        }

        // Reset the Modelview Matrix
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f);
        gl.glScalef(1.2f, 2.1f, 1.0f);

        glBitmap.draw(gl, bitmap);

//        changeGLViewport(gl);
    }

    /**
     * 通过改变gl的视角获取
     *
     * @param gl
     */
    private void changeGLViewport(GL10 gl) {
        System.out.println("time=" + System.currentTimeMillis());
        frameSeq++;
        viewportOffset++;
        if (frameSeq % 100 == 0) {// 每隔100帧，重置
            gl.glViewport(0, 0, width, height);
            viewportOffset = 0;
        } else {
            int k = 8;
            gl.glViewport(-maxOffset - viewportOffset * k,
                    -maxOffset - viewportOffset * k,
                    this.width + viewportOffset * 2 * k + maxOffset * 2,
                    this.height + viewportOffset * 2 * k + maxOffset * 2);
        }
    }

    public void updateImage(Bitmap bitmap){
        if(bitmap==null || bitmap.isRecycled()){
            return;
        }

        this.bitmap = bitmap;
        isClearScreen = false;
    }

    public void clearScreen(){
        isClearScreen = true;
    }

}
