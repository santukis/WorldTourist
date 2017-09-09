package com.david.arlocation.cluster.model;

import android.opengl.Matrix;

import com.david.arlocation.aritems.model.GeoLocation;

public class Location {

    private final static double WGS84_A = 6_378_137;                // WGS 84 semi-major axis constant in meters
    private final static double WGS84_E2 = 0.006_694_379_990_14;    // square of WGS 84 eccentricity

    public static float[] WSG84toECEF(GeoLocation geoLocation) {
        double radLat = Math.toRadians(geoLocation.getLatitude());
        double radLon = Math.toRadians(geoLocation.getLongitude());

        float latCosine = (float) Math.cos(radLat);
        float latSine = (float) Math.sin(radLat);
        float lonCosine = (float) Math.cos(radLon);
        float lonSine = (float) Math.sin(radLon);

        float N = (float) (WGS84_A / Math.sqrt(1.0 - WGS84_E2 * latSine * latSine));

        float x = (float) ((N + geoLocation.getAltitude()) * latCosine * lonCosine);
        float y = (float) ((N + geoLocation.getAltitude()) * latCosine * lonSine);
        float z = (float) ((N * (1.0 - WGS84_E2) + geoLocation.getAltitude()) * latSine);

        return new float[] {x , y, z};
    }

    public static float[] ECEFtoENU(GeoLocation currentGeoLocation, float[] currentLocationECEF, float[] itemECEF) {
        double radLat = Math.toRadians(currentGeoLocation.getLatitude());
        double radLon = Math.toRadians(currentGeoLocation.getLongitude());

        float latCosine = (float)Math.cos(radLat);
        float latSine = (float)Math.sin(radLat);
        float lonCosine = (float)Math.cos(radLon);
        float lonSine = (float)Math.sin(radLon);

        float dx = currentLocationECEF[0] - itemECEF[0];
        float dy = currentLocationECEF[1] - itemECEF[1];
        float dz = currentLocationECEF[2] - itemECEF[2];

        float east = -lonSine * dx + lonCosine * dy;

        float north = -latSine * lonCosine * dx - latSine * lonSine * dy + latCosine * dz;

        float up = latCosine * lonCosine * dx + latCosine * lonSine * dy + latSine * dz;

        return new float[] {east , north, up, 1};
    }

    public static CameraPosition getCameraPosition(GeoLocation userPosition,
                                                   GeoLocation itemPosition,
                                                   float[] cameraProjectionMatrix,
                                                   float[] rotationMatrix) {

        float[] currentLocationInECEF = Location.WSG84toECEF(userPosition);
        float[] cameraPositionVector = new float[4];
        float[] pointInECEF = Location.WSG84toECEF(itemPosition);
        float[] pointInENU = Location.ECEFtoENU(userPosition, currentLocationInECEF, pointInECEF);

        Matrix.multiplyMV(
                cameraPositionVector,
                0,
                getRotatedMatrix(cameraProjectionMatrix, rotationMatrix),
                0,
                pointInENU,
                0);

        return new CameraPosition(
                cameraPositionVector[0],
                cameraPositionVector[1],
                cameraPositionVector[2],
                cameraPositionVector[3]);

    }

    private static float[] getRotatedMatrix(float[] cameraProjectionMatrix, float[] rotationMatrix) {
        float[] rotatedProjectionMatrix = new float[16];

        Matrix.multiplyMM(
                rotatedProjectionMatrix, 0, cameraProjectionMatrix, 0, rotationMatrix, 0);

        return rotatedProjectionMatrix;
    }
}
