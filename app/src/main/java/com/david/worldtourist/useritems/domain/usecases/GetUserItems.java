package com.david.worldtourist.useritems.domain.usecases;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.useritems.domain.usecases.filter.ItemFilter;

import java.util.List;

import javax.inject.Inject;


public class GetUserItems extends UseCase<GetUserItems.RequestValues,
        GetUserItems.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public GetUserItems(ItemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final RequestValues requestValues) {

        repository.loadUserItems(requestValues, new Callback<ResponseValues>() {
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
        private ItemFilter itemFilter;
        private ItemCategory itemCategory;

        public RequestValues(@NonNull ItemFilter itemFilter, @NonNull ItemCategory itemCategory) {
            this.itemFilter = itemFilter;
            this.itemCategory = itemCategory;
        }

        public ItemFilter getItemFilter() {
            return itemFilter;
        }

        public ItemCategory getItemClass() {
            return itemCategory;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

        private List<Item> userItems;

        public ResponseValues(@NonNull List<Item> items) {
            userItems = items;
        }

        public List<Item> getUserItems() {
            return userItems;
        }

    }
}