package com.david.worldtourist.itemsmap.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;

import javax.inject.Inject;


public class CacheMapCoordinates extends UseCase<CacheMapCoordinates.RequestValues, CacheMapCoordinates.ResponseValues> {

    private final ItemsMapRepository repository;

    @Inject
    public CacheMapCoordinates(ItemsMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.cacheMapCoordinates(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        public GeoCoordinate mapLocation;

        public RequestValues(GeoCoordinate mapLocation) {
            this.mapLocation = mapLocation;
        }

        public GeoCoordinate getMapLocation() {
            return mapLocation;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
