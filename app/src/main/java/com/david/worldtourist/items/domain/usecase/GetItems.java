package com.david.worldtourist.items.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.LoadingState;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;


public class GetItems extends UseCase<GetItems.RequestValues, GetItems.ResponseValues> {


    private final ItemsRepository repository;

    @Inject
    public GetItems(ItemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.loadItems(requestValues, new Callback<ResponseValues>() {
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
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return new ResponseValues(repository.fetchItems());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private LoadingState loadingState;
        private ItemCategory itemCategory;
        private Set<String> itemTypes;
        private GeoCoordinate currentLocation;
        private double distance;

        public RequestValues(LoadingState loadingState,
                             ItemCategory itemCategory,
                             Set<String> itemTypes,
                             GeoCoordinate currentLocation,
                             double distance) {
            this.loadingState = loadingState;
            this.itemCategory = itemCategory;
            this.itemTypes = itemTypes;
            this.currentLocation = currentLocation;
            this.distance = distance;
        }

        public LoadingState getLoadingState() {
            return loadingState;
        }

        public ItemCategory getItemCategory(){
            return itemCategory;
        }

        public Set<String> getItemTypes() {
            return itemTypes;
        }

        public GeoCoordinate getCurrentLocation() {
            return currentLocation;
        }

        public double getDistance() {
            return distance;
        }

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private List<Item> items;

        public ResponseValues(List<Item> items) {
            this.items = items;
        }

        public List<Item> getItems() {
            return items;
        }
    }
}
