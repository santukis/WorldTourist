package com.david.worldtourist.preferences.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import javax.inject.Inject;

public class GetDistance extends UseCase<GetDistance.RequestValues, GetDistance.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public GetDistance(PreferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.getDistance(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
            }

            @Override
            public void onError(String error) {
            }
        });
    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
        repository.getDistance(requestValues, new Callback<ResponseValues>() {
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
        return new ResponseValues(repository.fetchDistance());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private ItemCategory itemCategory;

        public RequestValues(ItemCategory itemCategory) {
            this.itemCategory = itemCategory;
        }

        public ItemCategory getItemCategory() {
            return itemCategory;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private double distance;

        public ResponseValues(double distance) {
            this.distance = distance;
        }

        public double getDistance() {
            return this.distance;
        }
    }
}
