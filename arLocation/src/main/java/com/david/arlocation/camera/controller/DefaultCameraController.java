package com.david.arlocation.camera.controller;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.TextureView;

import com.david.arlocation.camera.boundary.CameraController;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class DefaultCameraController extends Observable implements CameraController, TextureView.SurfaceTextureListener {

    private Camera camera;

    private int cameraOrientation;

    private TextureView textureView;

    private float[] projectionMatrix = new float[16];

    public DefaultCameraController(@NonNull Context context,
                                   @NonNull Observer observer,
                                   @NonNull TextureView textureView) {
        addObserver(observer);
        this.textureView = textureView;
        setCameraOrientation(context);
    }

    private void setCameraOrientation(Context context) {
        final int PORTRAIT = 90;
        final int LANDSCAPE = 180;

        cameraOrientation = PORTRAIT;

        if (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            cameraOrientation = LANDSCAPE;
        }
    }

    @Override
    public void connectCamera() {
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void disconnectCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        deleteObservers();
    }

    ///////////////////////TextureView.SurfaceTextureListener implementation////////////////////////
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        if (camera == null) {
            camera = Camera.open();
        }

        try {

            if(camera != null) {

                camera.setDisplayOrientation(cameraOrientation);
                camera.setPreviewTexture(surface);
                camera.startPreview();

                createProjectionMatrix(width, height);
            }

        } catch (IOException exception) {
            camera.stopPreview();
            camera.release();

        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        if (camera != null) {

            Camera.Parameters params = camera.getParameters();
            params.setPreviewSize(width, height);
            camera.setDisplayOrientation(cameraOrientation);

            camera.setParameters(params);
            camera.startPreview();

            createProjectionMatrix(width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        disconnectCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void createProjectionMatrix(int width, int height) {
        float yFOV = 80.0f;
        float aspect = (float) width / height;
        float zNear = 0.5f;
        float zFar = 2000f;

        Matrix.perspectiveM(projectionMatrix, 0, yFOV, aspect, zNear, zFar);

        setChanged();
        notifyObservers(projectionMatrix);
    }
}
