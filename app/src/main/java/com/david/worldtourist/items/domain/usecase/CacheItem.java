package com.david.worldtourist.items.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.Item;

import javax.inject.Inject;

public class CacheItem extends UseCase<CacheItem.RequestValues, CacheItem.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public CacheItem(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.cacheItem(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Item item;

        public RequestValues(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return this.item;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
