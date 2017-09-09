package com.david.worldtourist.itemsmap.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;
import com.david.worldtourist.itemsmap.domain.usecase.model.Address;

import java.util.List;

import javax.inject.Inject;

public class GetAddress extends UseCase<GetAddress.RequestValues, GetAddress.ResponseValues> {

    private final ItemsMapRepository repository;

    @Inject
    public GetAddress(ItemsMapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.getAddress(requestValues, new Callback<ResponseValues>() {
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

    public static class RequestValues implements UseCase.RequestValues {
        private List<String> addresses;

        public RequestValues(List<String> addresses) {
            this.addresses = addresses;
        }

        public List<String> getAddresses() {
            return addresses;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private Address address;

        public ResponseValues(Address address) {
            this.address = address;
        }

        public Address getAddress() {
            return address;
        }
    }
}
