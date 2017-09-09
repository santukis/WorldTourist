package com.david.worldtourist.authentication.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;

public interface UserRepository {

    void getUser(@NonNull final UseCase.Callback<GetUser.ResponseValues> callback);

    void saveUser(@NonNull final SaveUser.RequestValues requestValues,
                  @NonNull final UseCase.Callback<SaveUser.ResponseValues> callback);

    void removeUser(@NonNull final RemoveUser.RequestValues requestValues,
                    @NonNull final UseCase.Callback<RemoveUser.ResponseValues> callback);

    GetUser.ResponseValues fetchUser();
}
