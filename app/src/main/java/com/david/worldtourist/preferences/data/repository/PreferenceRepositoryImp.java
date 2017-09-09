package com.david.worldtourist.preferences.data.repository;



import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.preferences.data.boundary.PreferenceDataSource;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveCoordinates;
import com.david.worldtourist.preferences.domain.usecase.SaveDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveItemTypes;

import java.util.Set;

public class PreferenceRepositoryImp implements PreferenceRepository {

    private static PreferenceRepositoryImp INSTANCE = null;

    private final PreferenceDataSource dataSource;

    private GeoCoordinate cachedUserCoordinates = GeoCoordinate.EMPTY_COORDINATE;
    private double cachedDistance = 0;

    private PreferenceRepositoryImp(PreferenceDataSource dataSource){
        this.dataSource = dataSource;
    }

    public static PreferenceRepositoryImp getInstance(PreferenceDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PreferenceRepositoryImp(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public void saveCoordinates(@NonNull SaveCoordinates.RequestValues requestValues) {
        cachedUserCoordinates = requestValues.getCurrentLocation();
        dataSource.saveCoordinates(requestValues);
    }

    @Override
    public GetCoordinates.ResponseValues getCoordinates() {
        if(cachedUserCoordinates != GeoCoordinate.EMPTY_COORDINATE) {
            return new GetCoordinates.ResponseValues(cachedUserCoordinates);
        }

        cachedUserCoordinates = dataSource.getCoordinates().getCurrentLocation();
        return new GetCoordinates.ResponseValues(cachedUserCoordinates);
    }

    @Override
    public void saveDistance(SaveDistance.RequestValues requestValues) {
        dataSource.saveDistance(requestValues);
    }

    @Override
    public void getDistance(GetDistance.RequestValues requestValues,
                            final UseCase.Callback<GetDistance.ResponseValues> callback) {

        dataSource.getDistance(requestValues, new UseCase.Callback<GetDistance.ResponseValues>() {
            @Override
            public void onSuccess(GetDistance.ResponseValues response) {
                cachedDistance = response.getDistance();
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void saveItemTypes(SaveItemTypes.RequestValues requestValues) {
        dataSource.saveItemTypes(requestValues);
    }

    @Override
    public Set<String> getItemTypes() {
        return dataSource.getItemTypes();
    }


    @Override
    public double fetchDistance() {
        return cachedDistance;
    }

}


