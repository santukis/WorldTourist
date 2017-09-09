package com.david.arlocation.cluster.manager;

import android.os.AsyncTask;

import com.david.arlocation.aritems.model.ArItem;
import com.david.arlocation.aritems.model.GeoLocation;
import com.david.arlocation.cluster.model.CameraPosition;
import com.david.arlocation.cluster.model.Cluster;
import com.david.arlocation.cluster.model.Location;
import com.david.arlocation.cluster.boundary.ClusterManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class DefaultClusterManager<T extends ArItem> implements ClusterManager<T> {

    private float CLUSTER_SIDE = 2.5f;

    private GeoLocation currentGeoLocation = GeoLocation.EMPTY_COORDINATE;
    private float[] cameraMatrix = new float[16];
    private float[] rotationMatrix = new float[16];

    private Collection<T> arItems = Collections.EMPTY_LIST;

    public DefaultClusterManager() {

    }

    @Override
    public void setCurrentLocation(GeoLocation currentGeoLocation) {
        this.currentGeoLocation = currentGeoLocation;
    }

    @Override
    public void setCameraProjectionMatrix(float[] cameraMatrix) {
        this.cameraMatrix = cameraMatrix;
    }

    @Override
    public void setRotationMatrix(float[] rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
    }

    @Override
    public void createClusters(final Collection<T> items, OnClustered<T> callback) {
        arItems = items;

        if(currentGeoLocation != GeoLocation.EMPTY_COORDINATE) {
            new ClusterItems(callback).execute(arItems);
        }
    }

    @Override
    public void updateClusters(OnClustered<T> callback) {
        if(!arItems.isEmpty()) {
            new ClusterItems(callback).execute(arItems);
        }
    }

    private class ClusterItems extends AsyncTask<Collection<T>, Void, Set<? extends Cluster<T>>> {

        private OnClustered<T> callback;

        ClusterItems(OnClustered<T> callback) {
            this.callback = callback;
        }

        @Override
        protected Set<? extends Cluster<T>> doInBackground(Collection<T>... params) {
            Collection<T> items = params[0];

            return createClusters(items);
        }

        @Override
        protected void onPostExecute(Set<? extends Cluster<T>> clusters) {
            callback.onSuccess(clusters);
        }
    }

    private Set<Cluster<T>> createClusters(Collection<T> items) {
        Set<Cluster<T>> clusters = new HashSet<>();
        Set<T> temporalItems = new HashSet<>(items);
        Set<T> clusterItems = new HashSet<>();

        for (Iterator<T> iterator = items.iterator(); iterator.hasNext(); ) {
            T currentItem = iterator.next();
            CameraPosition currentPosition = getCameraPosition(currentItem);

            for (Iterator<T> iterator2 = temporalItems.iterator(); iterator2.hasNext(); ) {
                T nextItem = iterator2.next();
                CameraPosition nextPosition = getCameraPosition(nextItem);

                if (isItemOverlapped(currentPosition, nextPosition)) {
                    clusterItems.add(nextItem);
                    iterator2.remove();
                }
            }

            if (!clusterItems.isEmpty()) {
                clusters.add(new DefaultCluster(new HashSet<T>(clusterItems)));
                clusterItems.clear();
            }
        }

        return clusters;
    }

    private CameraPosition getCameraPosition(T item) {
        return Location.getCameraPosition(
                currentGeoLocation,
                item.getLocation(),
                cameraMatrix,
                rotationMatrix);
    }

    private boolean isItemOverlapped(CameraPosition first, CameraPosition second) {
        float x1 = first.getX() / first.getW();
        float x2 = second.getX() / second.getW();
        float y1 = first.getY() / first.getW();
        float y2 = second.getY() / second.getW();
        float z1 = first.getZ();
        float z2 = second.getZ();

        return areOverlappedInX(x1, x2) &&
                areOverlappedInY(y1, y2) &&
                (areBehindTheCamera(z1, z2) || areInFrontOfTheCamera(z1, z2));
    }

    private boolean areOverlappedInX(float first, float second) {
        return ((first <= second) && ((first + CLUSTER_SIDE) >= second))
                || ((first >= second) && (first <= (second + CLUSTER_SIDE)));
    }

    private boolean areOverlappedInY(float first, float second) {
        return (first >= second && first <= second + CLUSTER_SIDE) ||
                (first <= second && first + CLUSTER_SIDE >= second);
    }

    private boolean areInFrontOfTheCamera(float first, float second) {
        return (first >= 0 && second >= 0);
    }

    private boolean areBehindTheCamera(float first, float second) {
        return (first <= 0 && second <= 0);
    }

    private class DefaultCluster implements Cluster<T> {
        private Set<T> items = new HashSet<>();
        private GeoLocation coordinates = GeoLocation.EMPTY_COORDINATE;

        DefaultCluster(Set<T> items) {
            this.items = items;
            coordinates = getCoordinates();
        }

        @Override
        public CameraPosition getPosition() {
            return Location.getCameraPosition(
                    currentGeoLocation,
                    coordinates,
                    cameraMatrix,
                    rotationMatrix);
        }

        @Override
        public Collection<T> getItems() {
            return items;
        }

        @Override
        public int getSize() {
            return items.size();
        }

        private GeoLocation getCoordinates() {
            double latitude = 0, longitude = 0;

            for (T item : items) {
                latitude += item.getLocation().getLatitude();
                longitude += item.getLocation().getLongitude();
            }

            return new GeoLocation(latitude / items.size(), longitude / items.size());
        }
    }
}