package com.david.worldtourist.itemsdetail.domain.usecase;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.ItemUserFilter;

import javax.inject.Inject;


public class GetItemFilter extends UseCase<GetItemFilter.RequestValues, GetItemFilter.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public GetItemFilter(ItemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(GetItemFilter.RequestValues requestValues) {

        repository.loadUserFilter(requestValues, new Callback<ResponseValues>() {
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
    public void execute(GetItemFilter.RequestValues requestValues,
                        final Callback<GetItemFilter.ResponseValues> callback) {
        repository.loadUserFilter(requestValues, new Callback<ResponseValues>() {
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
        return new ResponseValues(repository.fetchUserFilter());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String itemId;

        public RequestValues(@NonNull String itemId) {
            this.itemId = itemId;
        }

        public String getItemId() {
            return itemId;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

        private ItemUserFilter itemFilter;

        public ResponseValues(@NonNull ItemUserFilter itemFilter) {
            this.itemFilter = itemFilter;
        }

        public ItemUserFilter getItemFilter() {
            return itemFilter;
        }
    }
}
