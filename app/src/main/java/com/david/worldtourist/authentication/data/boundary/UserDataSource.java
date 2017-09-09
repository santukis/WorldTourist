package com.david.worldtourist.authentication.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;

public interface UserDataSource {

    void saveUser(@NonNull SaveUser.RequestValues requestValues,
                  @NonNull UseCase.Callback<SaveUser.ResponseValues> callback);

    void getUser(@NonNull UseCase.Callback<GetUser.ResponseValues> callback);

    void removeUser(@NonNull RemoveUser.RequestValues requestValues,
                    @NonNull UseCase.Callback<RemoveUser.ResponseValues> callback);
}