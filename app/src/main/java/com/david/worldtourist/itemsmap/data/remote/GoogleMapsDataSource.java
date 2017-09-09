package com.david.worldtourist.itemsmap.data.remote;


import android.support.annotation.NonNull;
import android.util.Log;

import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.remote.googleAPI.GooglePersistence;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.data.boundary.MapsDataSource;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;
import com.david.worldtourist.itemsmap.domain.usecase.model.Address;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class GoogleMapsDataSource implements MapsDataSource {

    private static GoogleMapsDataSource INSTANCE = null;

    private static final String COMMA = ",";

    private NetworkClient networkClient;

    private GoogleMapsDataSource(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }

    public static GoogleMapsDataSource getInstance(NetworkClient networkClient) {
        if (INSTANCE == null) {
            INSTANCE = new GoogleMapsDataSource(networkClient);
        }
        return INSTANCE;
    }


    @Override
    public void getRoute(@NonNull GetRoute.RequestValues requestValues,
                         @NonNull UseCase.Callback<GetRoute.ResponseValues> callback) {

        Route route;

        GeoCoordinate origin = requestValues.getOrigin();
        GeoCoordinate destination = requestValues.getDestination();
        String mode = requestValues.getTravelMode().getName();

        String jsonItem = networkClient.makeServiceCall(
                GooglePersistence.DIRECTIONS_URL +
                        GooglePersistence.ORIGIN_KEY + origin.getLatitude() + COMMA + origin.getLongitude() +
                        GooglePersistence.DESTINATION_KEY + destination.getLatitude() + COMMA + destination.getLongitude() +
                        GooglePersistence.MODE_KEY + mode +
                        GooglePersistence.LANGUAGE_KEY + Locale.getDefault().getLanguage() +
                        GooglePersistence.API_KEY);

        GoogleDirectionsJSONParser googleParser = new GoogleDirectionsJSONParser();

        route = googleParser.getRouteFromJson(jsonItem);

        route.setName(requestValues.getName());
        route.setTravelMode(requestValues.getTravelMode());

        if(route == Route.EMPTY_ROUTE) {
            callback.onError(Constants.EMPTY_OBJECT_ERROR);

        } else {
            callback.onSuccess(new GetRoute.ResponseValues(route));
        }

    }

    @Override
    public void getAddress(@NonNull GetAddress.RequestValues requestValues,
                           @NonNull UseCase.Callback<GetAddress.ResponseValues> callback) {

        Address address;

        for(String name : requestValues.getAddresses()) {

            try{
                name = URLEncoder.encode(name, "UTF-8");

            } catch (UnsupportedEncodingException exception) {
                Log.d("ENCODER", exception.getLocalizedMessage());
            }

            String jsonItem = networkClient.makeServiceCall(
                    GooglePersistence.GEOCODING_URL +
                            GooglePersistence.ADDRESS_KEY + name +
                            GooglePersistence.LANGUAGE_KEY + Locale.getDefault().getLanguage() +
                            GooglePersistence.API_KEY);

            GoogleGeocodingJSONParser googleParser = new GoogleGeocodingJSONParser();

            address = googleParser.getAddressFromJson(jsonItem);

            if (address != Address.EMPTY_ADDRESS) {
                callback.onSuccess(new GetAddress.ResponseValues(address));
                return;
            }
        }

        callback.onError(Constants.EMPTY_OBJECT_ERROR);
    }
}
