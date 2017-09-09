package com.david.worldtourist.useritems.domain.usecases;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsRepository;

import javax.inject.Inject;


public class DeleteSelectedItemIds extends UseCase<DeleteSelectedItemIds.RequestValues, DeleteSelectedItemIds.ResponseValues> {

    private final ItemsRepository repository;

    @Inject
    public DeleteSelectedItemIds(ItemsRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.deleteSelectedItemIds();
    }

    @Override
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
