package com.david.worldtourist.useritems.domain.usecases;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;

import java.util.List;

import javax.inject.Inject;

public class GetSelectedItemIds extends UseCase<GetSelectedItemIds.RequestValues, GetSelectedItemIds.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public GetSelectedItemIds(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return new ResponseValues(repository.fetchSelectedItemIds());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private List<String> itemIds;

        public ResponseValues(List<String> itemIds) {
            this.itemIds = itemIds;
        }

        public List<String> getItemIds() {
            return this.itemIds;
        }
    }
}
