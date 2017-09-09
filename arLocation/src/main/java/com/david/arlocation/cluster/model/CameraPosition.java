package com.david.arlocation.cluster.model;


public class CameraPosition {

    private float x = 0f;
    private float y = 0f;
    private float z = 0f;
    private float w = 0f;

    public static final CameraPosition EMPTY_CAMERA_POSITION = new CameraPosition(0, 0, 0, 0);

    public CameraPosition(){

    }

    public CameraPosition(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW(){
        return w;
    }

    public void setW(float w){
        this.w = w;
    }
}
