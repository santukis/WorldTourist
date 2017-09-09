package com.david.arlocation.aritems.manager;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.TextureView;
import android.widget.Toast;

import com.david.arlocation.R;
import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.aritems.model.GeoLocation;
import com.david.arlocation.aritems.boundary.ArManager;
import com.david.arlocation.camera.boundary.CameraController;
import com.david.arlocation.camera.controller.DefaultCameraController;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.cluster.boundary.ClusterManager;
import com.david.arlocation.cluster.controller.DefaultClusterController;
import com.david.arlocation.view.boundary.ArViewManager;
import com.david.arlocation.sensors.services.LocationService;
import com.david.arlocation.sensors.services.OrientationService;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.controller.DefaultViewController;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.arlocation.sensors.boundary.SensorController;
import com.david.arlocation.sensors.controller.DefaultSensorController;
import com.david.arlocation.sensors.services.SensorListener;
import com.david.arlocation.view.views.IconsView;
import com.david.arlocation.view.views.RadarView;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class DefaultArManager<T extends ArItem> implements ArManager<T>, SensorListener, Observer {

    private Context context;
    private CameraController cameraController;
    private SensorController orientationController;
    private SensorController locationController;
    private ClusterManager<T> clusterManager;
    private ArViewManager<T> viewManager;

    public DefaultArManager(@NonNull Context context,
                            @NonNull TextureView textureView,
                            @NonNull IconsView<T> iconsView,
                            @NonNull RadarView<T> radarView) {

        this.context = context;
        this.cameraController = new DefaultCameraController(context, this, textureView);
        this.orientationController = new DefaultSensorController(context);
        this.locationController = new DefaultSensorController(context);
        this.clusterManager = new DefaultClusterController<>();
        this.viewManager = new DefaultViewController<>(context, iconsView, radarView);
    }

    @Override
    public void setMarkerRenderer(@NonNull MarkerRenderer<T> markerRenderer) {
        viewManager.setMarkerRenderer(markerRenderer);
    }

    @Override
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
        viewManager.setOnClusterClickListener(listener);
    }

    @Override
    public void setOnArItemClickListener(OnArItemClickListener<T> listener) {
        viewManager.setOnArItemClickListener(listener);
    }

    @Override
    public void init() {
        orientationController.connectSensor(this, OrientationService.class);
        locationController.connectSensor(this, LocationService.class);
        cameraController.connectCamera();
    }

    @Override
    public void addArItems(Collection<T> arItems) {

        clusterManager.createClusters(arItems, new ClusterManager.OnClustered<T>() {
            @Override
            public void onSuccess(Set<? extends Cluster<T>> clusters) {
                viewManager.clearMarkers();
                viewManager.createMarkers(clusters);
            }
        });
    }

    @Override
    public void release() {
        cameraController.disconnectCamera();
        orientationController.disconnectSensor();
        locationController.disconnectSensor();
    }


    @Override
    public void onSensorDataChanged(int sensorId, Object data) {
        switch (sensorId) {

            case LOCATION_SENSOR:
                clusterManager.setCurrentLocation((GeoLocation) data);
                clusterManager.updateClusters(new ClusterManager.OnClustered<T>() {
                    @Override
                    public void onSuccess(Set<? extends Cluster<T>> clusters) {
                        viewManager.clearMarkers();
                        viewManager.createMarkers(clusters);
                    }
                });
                break;

            case ORIENTATION_SENSOR:
                clusterManager.setRotationMatrix((float[]) data);
                viewManager.updateMarkersPosition();
                break;
        }
    }

    @Override
    public void isSensorAvailable(int sensorId, boolean available) {
        switch (sensorId) {
            case LOCATION_SENSOR:
            case ORIENTATION_SENSOR:
                Toast.makeText(context, R.string.sensor_not_available, Toast.LENGTH_LONG).show();

                try{
                    ((Activity) context).getFragmentManager().popBackStackImmediate();

                } catch (ClassCastException exception) {

                }
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof float[]) {
            clusterManager.setCameraProjectionMatrix((float[]) arg);
        }
    }
}
