package com.david.worldtourist.itemsmap.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.DeleteRoute;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;

public interface ItemsMapRepository {

    void getRoute(@NonNull final GetRoute.RequestValues requestValues,
                  @NonNull final UseCase.Callback<GetRoute.ResponseValues> callback);

    void deleteRoute(DeleteRoute.RequestValues requestValues);

    void getAddress(@NonNull GetAddress.RequestValues requestValues,
                    @NonNull final UseCase.Callback<GetAddress.ResponseValues> callback);

    void cacheMapCoordinates(@NonNull CacheMapCoordinates.RequestValues requestValues);

    GeoCoordinate fetchMapCoordinates();
}
