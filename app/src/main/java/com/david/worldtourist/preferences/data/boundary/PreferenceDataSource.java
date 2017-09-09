package com.david.worldtourist.preferences.data.boundary;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveCoordinates;
import com.david.worldtourist.preferences.domain.usecase.SaveDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveItemTypes;

import java.util.Set;

public interface PreferenceDataSource {

    void saveCoordinates(SaveCoordinates.RequestValues requestValues);

    GetCoordinates.ResponseValues getCoordinates();

    void saveDistance(SaveDistance.RequestValues requestValues);

    void getDistance(GetDistance.RequestValues requestValues,
                     UseCase.Callback<GetDistance.ResponseValues> callback);

    void saveItemTypes(SaveItemTypes.RequestValues requestValues);

    Set<String> getItemTypes();
}
