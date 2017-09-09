package com.david.worldtourist.useritems.domain.usecases;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.ItemUserFilter;

import javax.inject.Inject;

public class CacheItemFilter extends UseCase<CacheItemFilter.RequestValues, CacheItemFilter.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public CacheItemFilter(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.cacheUserFilter(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private ItemUserFilter itemUserFilter;

        public RequestValues(ItemUserFilter itemUserFilter) {
            this.itemUserFilter = itemUserFilter;
        }

        public ItemUserFilter getUserFilter() {
            return this.itemUserFilter;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
