package com.david.worldtourist.itemsdetail.domain.usecase;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemType;

import javax.inject.Inject;

public class GetItem extends UseCase<GetItem.RequestValues, GetItem.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public GetItem(ItemsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.loadItem(requestValues, new Callback<ResponseValues>() {
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
        return new ResponseValues(repository.fetchItem());
    }

    public static class RequestValues implements UseCase.RequestValues {
        private ItemCategory itemCategory;
        private String itemId;
        private ItemType itemType;

        public RequestValues(ItemCategory itemCategory, String itemId, ItemType itemType) {
            this.itemCategory = itemCategory;
            this.itemId = itemId;
            this.itemType = itemType;
        }

        public ItemCategory getItemCategory() {
            return itemCategory;
        }

        public String getItemId() {
            return itemId;
        }

        public ItemType getItemType() {
            return itemType;
        }

    }

    public static class ResponseValues implements UseCase.ResponseValues {

        private Item item;

        public ResponseValues(@NonNull Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }
}
