package com.david.worldtourist.itemsmap.data.repository;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;
import com.david.worldtourist.itemsmap.data.boundary.MapsDataSource;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;
import com.david.worldtourist.itemsmap.domain.usecase.DeleteRoute;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemsMapRepositoryImp implements ItemsMapRepository{

    private static ItemsMapRepositoryImp INSTANCE = null;

    private final MapsDataSource dataSource;

    private List<Route> cachedRoutes = new ArrayList<>();
    private GeoCoordinate cachedMapCoordinates = GeoCoordinate.EMPTY_COORDINATE;

    private ItemsMapRepositoryImp(MapsDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ItemsMapRepositoryImp getInstance(MapsDataSource dataSource) {
        if(INSTANCE == null) {
            INSTANCE = new ItemsMapRepositoryImp(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getRoute(@NonNull final GetRoute.RequestValues requestValues,
                         @NonNull final UseCase.Callback<GetRoute.ResponseValues> callback) {

        for(Route route : cachedRoutes) {
            if(route.getName().equals(requestValues.getName())
                    && route.getTravelMode() == requestValues.getTravelMode()) {
                callback.onSuccess(new GetRoute.ResponseValues(Route.EMPTY_ROUTE));
                return;
            }
        }

        dataSource.getRoute(requestValues, new UseCase.Callback<GetRoute.ResponseValues>() {
            @Override
            public void onSuccess(GetRoute.ResponseValues response) {
                cachedRoutes.add(response.getRoute());
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public synchronized void deleteRoute(DeleteRoute.RequestValues requestValues) {
        for(Iterator<Route> iterator = cachedRoutes.iterator(); iterator.hasNext();) {
            Route route = iterator.next();
            if(route.getName().equals(requestValues.getName())
                    && route.getTravelMode() == requestValues.getTravelMode()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void getAddress(@NonNull GetAddress.RequestValues requestValues,
                           @NonNull final UseCase.Callback<GetAddress.ResponseValues> callback) {

        dataSource.getAddress(requestValues, callback);
    }

    @Override
    public void cacheMapCoordinates(@NonNull CacheMapCoordinates.RequestValues requestValues) {
        cachedMapCoordinates = requestValues.getMapLocation();
    }

    @Override
    public GeoCoordinate fetchMapCoordinates() {
        return cachedMapCoordinates;
    }
}