package com.david.worldtourist.itemsmap.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;

public interface MapsDataSource {

    void getRoute(@NonNull GetRoute.RequestValues requestValues,
                  @NonNull UseCase.Callback<GetRoute.ResponseValues> callback);

    void getAddress(@NonNull GetAddress.RequestValues requestValues,
                    @NonNull UseCase.Callback<GetAddress.ResponseValues> callback);
}
