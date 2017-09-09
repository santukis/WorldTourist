package com.david.worldtourist.useritems.domain.usecases;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;

import java.util.List;

import javax.inject.Inject;

public class DeleteItems extends UseCase<DeleteItems.RequestValues, DeleteItems.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public DeleteItems(ItemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.deleteItems(requestValues, new Callback<ResponseValues>() {
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
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private List<String> itemIds;

        public RequestValues(List<String> itemIds) {
            this.itemIds = itemIds;
        }

        public List<String> getItemIds() {
            return itemIds;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
