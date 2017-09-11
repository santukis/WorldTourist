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
import com.david.arlocation.cluster.boundary.ClusterController;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.cluster.controller.DefaultClusterController;
import com.david.arlocation.view.boundary.ArViewController;
import com.david.arlocation.sensors.services.LocationService;
import com.david.arlocation.sensors.services.OrientationService;
import com.david.arlocation.view.components.OnArItemClickListener;
import com.david.arlocation.view.components.OnClusterClickListener;
import com.david.arlocation.view.controller.DefaultViewController;
import com.david.arlocation.view.model.MarkerRenderer;
import com.david.arlocation.sensors.boundary.SensorController;
import com.david.arlocation.sensors.controller.DefaultSensorController;
import com.david.arlocation.sensors.services.SensorListener;
import com.david.arlocation.view.views.MarkersView;
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
    private ClusterController<T> clusterController;
    private ArViewController<T> viewController;

    public DefaultArManager(@NonNull Context context,
                            @NonNull TextureView textureView,
                            @NonNull MarkersView<T> markersView,
                            @NonNull RadarView<T> radarView) {

        this.context = context;
        this.cameraController = new DefaultCameraController(context, this, textureView);
        this.orientationController = new DefaultSensorController(context);
        this.locationController = new DefaultSensorController(context);
        this.clusterController = new DefaultClusterController<>();
        this.viewController = new DefaultViewController<>(context, markersView, radarView);
    }

    @Override
    public void setMarkerRenderer(@NonNull MarkerRenderer<T> markerRenderer) {
        viewController.setMarkerRenderer(markerRenderer);
    }

    @Override
    public void setOnClusterClickListener(OnClusterClickListener<T> listener) {
        viewController.setOnClusterClickListener(listener);
    }

    @Override
    public void setOnArItemClickListener(OnArItemClickListener<T> listener) {
        viewController.setOnArItemClickListener(listener);
    }

    @Override
    public void init() {
        orientationController.connectSensor(this, OrientationService.class);
        locationController.connectSensor(this, LocationService.class);
        cameraController.connectCamera();
    }

    @Override
    public void addArItems(Collection<T> arItems) {

        clusterController.createClusters(arItems, new ClusterController.OnClustered<T>() {
            @Override
            public void onSuccess(Set<? extends Cluster<T>> clusters) {
                viewController.clearMarkers();
                viewController.createMarkers(clusters);
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
                clusterController.setCurrentLocation((GeoLocation) data);
                clusterController.updateClusters(new ClusterController.OnClustered<T>() {
                    @Override
                    public void onSuccess(Set<? extends Cluster<T>> clusters) {
                        viewController.clearMarkers();
                        viewController.createMarkers(clusters);
                    }
                });
                break;

            case ORIENTATION_SENSOR:
                clusterController.setRotationMatrix((float[]) data);
                viewController.updateMarkersPosition();
                break;
        }
    }

    @Override
    public void isSensorAvailable(int sensorId, boolean available) {
        switch (sensorId) {
            case LOCATION_SENSOR:
            case ORIENTATION_SENSOR:
                Toast.makeText(
                        context,
                        R.string.sensor_not_available,
                        Toast.LENGTH_LONG)
                        .show();

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
            clusterController.setCameraProjectionMatrix((float[]) arg);
        }
    }
}
