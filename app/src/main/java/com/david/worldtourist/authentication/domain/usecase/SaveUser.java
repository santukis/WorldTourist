package com.david.worldtourist.authentication.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.domain.model.User;

public class SaveUser extends UseCase<SaveUser.RequestValues, SaveUser.ResponseValues> {

    private final UserRepository repository;

    public SaveUser(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.saveUser(requestValues, new Callback<ResponseValues>() {
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
        repository.saveUser(requestValues, new Callback<ResponseValues>() {
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
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private User user;

        public RequestValues(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
