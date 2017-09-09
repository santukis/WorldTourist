package com.david.worldtourist.preferences.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;

import java.util.Set;

import javax.inject.Inject;

public class SaveItemTypes extends UseCase<SaveItemTypes.RequestValues, SaveItemTypes.ResponseValues> {

    private final PreferenceRepository repository;

    @Inject
    public SaveItemTypes(PreferenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.saveItemTypes(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        Set<String> itemTypes;

        public RequestValues(Set<String> itemTypes) {
            this.itemTypes = itemTypes;
        }

        public Set<String> getItemTypes() {
            return this.itemTypes;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
