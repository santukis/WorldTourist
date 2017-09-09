package com.david.worldtourist.preferences.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import javax.inject.Inject;

public class GetCoordinates extends UseCase<GetCoordinates.RequestValues, GetCoordinates.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public GetCoordinates(PreferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return repository.getCoordinates();
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        public GeoCoordinate currentLocation;

        public ResponseValues(GeoCoordinate currentLocation) {
            this.currentLocation = currentLocation;
        }

        public GeoCoordinate getCurrentLocation() {
            return currentLocation;
        }
    }
}
