package com.david.worldtourist.preferences.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import javax.inject.Inject;

public class SaveCoordinates extends UseCase<SaveCoordinates.RequestValues, SaveCoordinates.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public SaveCoordinates(PreferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.saveCoordinates(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        public GeoCoordinate currentLocation;

        public RequestValues(GeoCoordinate currentLocation) {
            this.currentLocation = currentLocation;
        }

        public GeoCoordinate getCurrentLocation() {
            return currentLocation;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
