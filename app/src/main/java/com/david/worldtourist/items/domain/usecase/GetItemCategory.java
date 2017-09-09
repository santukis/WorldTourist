package com.david.worldtourist.items.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.ItemCategory;

import javax.inject.Inject;

public class GetItemCategory extends UseCase<GetItemCategory.RequestValues, GetItemCategory.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public GetItemCategory(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return new ResponseValues(repository.fetchItemCategory());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private ItemCategory itemCategory;

        public ResponseValues(ItemCategory itemCategory) {
            this.itemCategory = itemCategory;
        }

        public ItemCategory getItemCategory() {
            return  this.itemCategory;
        }

    }
}
