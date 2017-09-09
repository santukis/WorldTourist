package com.david.worldtourist.authentication.data.repository;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.UserDataSource;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;

public class UserRepositoryImp implements UserRepository{

    private static UserRepositoryImp INSTANCE = null;

    private final UserDataSource localDataStorage;
    private final UserDataSource remoteDataStorage;

    private User cachedUser = User.EMPTY_USER;

    private UserRepositoryImp(UserDataSource localDataStorage, UserDataSource remoteDataStorage) {
        this.localDataStorage = localDataStorage;
        this.remoteDataStorage = remoteDataStorage;
    }

    public static UserRepositoryImp getInstance(UserDataSource localDataStorage,
                                                UserDataSource remoteDataStorage) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepositoryImp(localDataStorage, remoteDataStorage);
        }
        return INSTANCE;
    }

    @Override
    public void getUser(@NonNull final UseCase.Callback<GetUser.ResponseValues> callback) {

        localDataStorage.getUser(new UseCase.Callback<GetUser.ResponseValues>() {
            @Override
            public void onSuccess(GetUser.ResponseValues response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                remoteDataStorage.getUser(callback);
            }
        });
    }

    @Override
    public void saveUser(@NonNull final SaveUser.RequestValues requestValues,
                         @NonNull final UseCase.Callback<SaveUser.ResponseValues> callback) {
        localDataStorage.saveUser(requestValues, new UseCase.Callback<SaveUser.ResponseValues>() {
            @Override
            public void onSuccess(SaveUser.ResponseValues response) {
                remoteDataStorage.saveUser(requestValues,
                        new UseCase.Callback<SaveUser.ResponseValues>() {
                            @Override
                            public void onSuccess(SaveUser.ResponseValues response) {
                                callback.onSuccess(response);
                                cachedUser = requestValues.getUser();
                            }

                            @Override
                            public void onError(String error) {
                                callback.onError(error);
                            }
                        });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void removeUser(@NonNull final RemoveUser.RequestValues requestValues,
                           @NonNull final UseCase.Callback<RemoveUser.ResponseValues> callback) {
        localDataStorage.removeUser(requestValues, new UseCase.Callback<RemoveUser.ResponseValues>() {
            @Override
            public void onSuccess(RemoveUser.ResponseValues response) {
                remoteDataStorage.removeUser(requestValues,
                        new UseCase.Callback<RemoveUser.ResponseValues>() {
                            @Override
                            public void onSuccess(RemoveUser.ResponseValues response) {
                                callback.onSuccess(response);
                                cachedUser = User.EMPTY_USER;
                            }

                            @Override
                            public void onError(String error) {
                                callback.onError(error);
                            }
                        });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public GetUser.ResponseValues fetchUser() {
        return new GetUser.ResponseValues(cachedUser);
    }
}
