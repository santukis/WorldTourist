package com.david.worldtourist.itemsdetail.domain.usecase;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;

import javax.inject.Inject;

public class SaveItem extends UseCase<SaveItem.RequestValues, SaveItem.ResponseValues>{

    private final ItemsRepository repository;

    @Inject
    public SaveItem(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.saveItem(requestValues, new Callback<ResponseValues>() {
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

    public static class RequestValues implements UseCase.RequestValues{

        private Item item;
        private ItemCategory itemCategory;

        public RequestValues(@NonNull Item item, @NonNull ItemCategory itemCategory){
            this.item = item;
            this.itemCategory = itemCategory;
        }

        public Item getItem(){
            return item;
        }

        public ItemCategory getItemCategory(){
            return itemCategory;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues{}
}
