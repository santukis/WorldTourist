package com.david.arlocation.camera.controller;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.OrientationEventListener;
import android.view.TextureView;

import com.david.arlocation.camera.boundary.CameraController;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class DefaultCameraController extends Observable implements CameraController, TextureView.SurfaceTextureListener {

    private Camera camera;

    private int cameraOrientation;
    private OrientationEventListener orientationListener;

    private TextureView cameraView;

    private float[] projectionMatrix = new float[16];

    public DefaultCameraController(@NonNull Context context,
                                   @NonNull Observer observer,
                                   @NonNull TextureView textureView) {
        addObserver(observer);
        cameraView = textureView;
        initializeOrientationListener(context);
    }

    private void initializeOrientationListener(@NonNull final Context context) {
        orientationListener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                if(orientation == ORIENTATION_UNKNOWN) {
                    return;
                }

                int degrees = 270;
                if (orientation < 45 || orientation > 315)
                    degrees = 0;
                else if (orientation < 135)
                    degrees = 90;
                else if (orientation < 225)
                    degrees = 180;

                setCameraOrientation(degrees);
            }
        };
    }

    private void setCameraOrientation(int degrees) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);

        if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            this.cameraOrientation = (info.orientation + degrees) % 360;
            this.cameraOrientation =  (360 - this.cameraOrientation) % 360;

        } else {
            this.cameraOrientation = (info.orientation - degrees + 360) % 360;
        }
    }

    @Override
    public void connectCamera() {
        cameraView.setSurfaceTextureListener(this);

        if(orientationListener != null) {
            orientationListener.enable();
        }
    }

    @Override
    public void disconnectCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        if(orientationListener != null) {
            orientationListener.disable();
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
