package com.david.worldtourist.itemsmap.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;

import javax.inject.Inject;

public class GetRoute extends UseCase<GetRoute.RequestValues, GetRoute.ResponseValues> {

    private final ItemsMapRepository repository;

    @Inject
    public GetRoute(ItemsMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.getRoute(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                getCallback().onSuccess(response);
            }

            @Override
            public void onError(String error) {
                getCallback().onError(error);
            }
        });
    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
        repository.getRoute(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String name;
        private GeoCoordinate origin;
        private GeoCoordinate destination;
        private TravelMode travelMode;

        public RequestValues(String name, GeoCoordinate origin, GeoCoordinate destination, TravelMode travelMode) {
            this.name = name;
            this.origin = origin;
            this.destination = destination;
            this.travelMode = travelMode;
        }

        public String getName() {
            return name;
        }

        public GeoCoordinate getOrigin() {
            return origin;
        }

        public GeoCoordinate getDestination() {
            return destination;
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
