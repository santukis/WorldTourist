package com.david.worldtourist.useritems.domain.usecases;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;

import javax.inject.Inject;

public class UpdateSelectedItemIds extends UseCase<UpdateSelectedItemIds.RequestValues, UpdateSelectedItemIds.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public UpdateSelectedItemIds(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
        repository.updateItemId(requestValues, new Callback<ResponseValues>() {
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
        private String itemId;

        public RequestValues(String itemId) {
            this.itemId = itemId;
        }

        public String getItemId() {
            return this.itemId;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
