package com.david.worldtourist.items.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.ItemCategory;

import javax.inject.Inject;

public class CacheItemCategory extends UseCase<CacheItemCategory.RequestValues, CacheItemCategory.ResponseValues>{

    private final ItemsRepository repository;

    @Inject
    public CacheItemCategory(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.cacheItemCategory(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private ItemCategory itemCategory;

        public RequestValues(ItemCategory itemCategory) {
            this.itemCategory = itemCategory;
        }

        public ItemCategory getItemCategory() {
            return this.itemCategory;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
