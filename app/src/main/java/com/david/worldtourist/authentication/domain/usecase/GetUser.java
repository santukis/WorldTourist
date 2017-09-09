package com.david.worldtourist.authentication.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.domain.model.User;

import javax.inject.Inject;

public class GetUser extends UseCase<GetUser.RequestValues, GetUser.ResponseValues>{

    private final UserRepository repository;

    @Inject
    public GetUser(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.getUser(new Callback<ResponseValues>() {
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
    public void execute(RequestValues requestValues, final Callback<ResponseValues> callback) {
        repository.getUser(new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public ResponseValues executeSync() {
        return repository.fetchUser();
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private User user;

        public ResponseValues(User user){
            this.user = user;
        }

        public User getUser(){
            return user;
        }
    }
}
