package com.david.worldtourist.preferences.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import java.util.Set;

import javax.inject.Inject;

public class GetItemTypes extends UseCase<GetItemTypes.RequestValues, GetItemTypes.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public GetItemTypes(PreferenceRepository repository) {
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
        return new ResponseValues(repository.getItemTypes());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        Set<String> itemTypes;

        public ResponseValues(Set<String> itemTypes) {
            this.itemTypes = itemTypes;
        }

        public Set<String> getItemTypes() {
            return this.itemTypes;
        }
    }
}
