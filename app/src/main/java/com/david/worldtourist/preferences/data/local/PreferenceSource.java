package com.david.worldtourist.preferences.data.local;


import android.content.Context;
import android.content.SharedPreferences;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.preferences.data.boundary.PreferenceDataSource;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.preferences.domain.usecase.SaveCoordinates;
import com.david.worldtourist.preferences.domain.usecase.SaveDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveItemTypes;
import com.david.worldtourist.utils.Constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PreferenceSource implements PreferenceDataSource {

    private static PreferenceSource INSTANCE = null;

    private static final String WORLDTOURIST_PREFERENCES = "com.david.worldtourist_internal_PREFERENCES";

    private static final String LATITUDE_KEY = "latitude";
    private static final String LONGITUDE_KEY = "longitude";
    private static final String SITE_DISTANCE_KEY = "site_distance";
    private static final String EVENT_DISTANCE_KEY = "event_distance";
    private static final String ITEM_TYPE_KEY = "item_type";

    private static final GeoCoordinate DEFAULT_COORDINATE = GeoCoordinate.EMPTY_COORDINATE;
    private static final float DEFAULT_SITE_DISTANCE = 50F;
    private static final float DEFAULT_EVENT_DISTANCE = 10000F;
    private static final Set<String> DEFAULT_ITEM_TYPES = new HashSet<>(Arrays.asList(Constants.CULTURE,
            Constants.ENTERTAINMENT, Constants.GASTRONOMY, Constants.NATURE));

    private final SharedPreferences preferences;

    private PreferenceSource(Context context) {
        preferences = context.getSharedPreferences(WORLDTOURIST_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static PreferenceSource getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = new PreferenceSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void saveCoordinates(SaveCoordinates.RequestValues requestValues) {
        GeoCoordinate currentLocation = requestValues.getCurrentLocation();
        preferences.edit().putFloat(LATITUDE_KEY, (float) currentLocation.getLatitude()).apply();
        preferences.edit().putFloat(LONGITUDE_KEY, (float) currentLocation.getLongitude()).apply();
    }

    @Override
    public GetCoordinates.ResponseValues getCoordinates() {
        double latitude = preferences.getFloat(LATITUDE_KEY, (float) DEFAULT_COORDINATE.getLatitude());
        double longitude = preferences.getFloat(LONGITUDE_KEY, (float) DEFAULT_COORDINATE.getLongitude());
        GeoCoordinate currentLocation = new GeoCoordinate(latitude, longitude);

        return new GetCoordinates.ResponseValues(currentLocation);
    }

    @Override
    public void saveDistance(SaveDistance.RequestValues requestValues) {
        float distance = (float) requestValues.getDistance();

        switch (requestValues.getItemCategory()) {
            case EVENT:
                preferences.edit().putFloat(EVENT_DISTANCE_KEY, distance).apply();
                break;

            case SITE:
                preferences.edit().putFloat(SITE_DISTANCE_KEY, distance).apply();
        }
    }

    @Override
    public void getDistance(GetDistance.RequestValues requestValues,
                            UseCase.Callback<GetDistance.ResponseValues> callback) {
        float distance = DEFAULT_SITE_DISTANCE;

        switch (requestValues.getItemCategory()) {
            case EVENT:
                distance = preferences.getFloat(EVENT_DISTANCE_KEY, DEFAULT_EVENT_DISTANCE);
                break;
            case SITE:
                distance = preferences.getFloat(SITE_DISTANCE_KEY, DEFAULT_SITE_DISTANCE);
                break;
        }

        callback.onSuccess(new GetDistance.ResponseValues(distance));
    }

    @Override
    public void saveItemTypes(SaveItemTypes.RequestValues requestValues) {
        Set<String> itemTypes = requestValues.getItemTypes();
        preferences.edit().putStringSet(ITEM_TYPE_KEY, itemTypes).apply();
    }

    @Override
    public Set<String> getItemTypes() {
        return preferences.getStringSet(ITEM_TYPE_KEY, DEFAULT_ITEM_TYPES);
    }
}
