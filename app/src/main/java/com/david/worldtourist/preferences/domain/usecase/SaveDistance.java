package com.david.worldtourist.preferences.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import javax.inject.Inject;

public class SaveDistance extends UseCase<SaveDistance.RequestValues, SaveDistance.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public SaveDistance(PreferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.saveDistance(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private double distance;
        private ItemCategory itemCategory;

        public RequestValues(double distance, ItemCategory itemCategory) {
            this.distance = distance;
            this.itemCategory = itemCategory;
        }

        public double getDistance() {
            return this.distance;
        }

        public ItemCategory getItemCategory() {
            return itemCategory;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
