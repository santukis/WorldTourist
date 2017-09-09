package com.david.worldtourist.itemsmap.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;

import javax.inject.Inject;


public class GetMapCoordinates extends UseCase<GetMapCoordinates.RequestValues, GetMapCoordinates.ResponseValues> {

    private final ItemsMapRepository repository;

    @Inject
    public GetMapCoordinates(ItemsMapRepository repository) {
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
        return new ResponseValues(repository.fetchMapCoordinates());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        public GeoCoordinate mapLocation;

        public ResponseValues(GeoCoordinate mapLocation) {
            this.mapLocation = mapLocation;
        }

        public GeoCoordinate getMapLocation() {
            return mapLocation;
        }
    }
}
