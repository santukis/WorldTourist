package com.david.worldtourist.itemsmap.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;

import javax.inject.Inject;

public class DeleteRoute extends UseCase<DeleteRoute.RequestValues, DeleteRoute.ResponseValues> {

    private final ItemsMapRepository repository;

    @Inject
    public DeleteRoute(ItemsMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.deleteRoute(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String name;
        private TravelMode travelMode;

        public RequestValues(String name, TravelMode travelMode) {
            this.name = name;
            this.travelMode = travelMode;
        }

        public String getName() {
            return name;
        }

        public TravelMode getTravelMode() {
            return travelMode;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private Route route;

        public ResponseValues(Route route) {
            this.route = route;
        }

        public Route getRoute() {
            return route;
        }
    }
}
