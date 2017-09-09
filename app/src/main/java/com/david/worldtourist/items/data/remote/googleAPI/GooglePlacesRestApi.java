package com.david.worldtourist.items.data.remote.googleAPI;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.items.data.boundary.RestApi;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class GooglePlacesRestApi implements RestApi {

    private static GooglePlacesRestApi INSTANCE = null;

    private NetworkClient networkClient;
    private GooglePlacesJSONParser jsonParser;


    private GooglePlacesRestApi(NetworkClient networkClient, GooglePlacesJSONParser jsonParser) {
        this.networkClient = networkClient;
        this.jsonParser = jsonParser;
    }

    public static GooglePlacesRestApi getInstance(NetworkClient networkClient, GooglePlacesJSONParser jsonParser) {
        if (INSTANCE == null) {
            INSTANCE = new GooglePlacesRestApi(networkClient, jsonParser);
        }
        return INSTANCE;
    }

    @Override
    public Item getItem(@NonNull GetItem.RequestValues requestValues) {

        String jsonItem = networkClient.makeServiceCall(
                GooglePersistence.PLACE_DETAILS_URL +
                        GooglePersistence.PLACE_ID_KEY + requestValues.getItemId() +
                        GooglePersistence.LANGUAGE_KEY + Locale.getDefault().getLanguage() +
                        GooglePersistence.API_KEY);

        return jsonParser.getItemFromJson(jsonItem);
    }

    @Override
    public List<Item> getItems(@NonNull GetItems.RequestValues requestValues) {

        List<Item> items = new ArrayList<>();
        List<String> googleTypes = getGoogleTypes(requestValues.getItemTypes());

        for (String type : googleTypes) {

            String jsonItems = getJsonItems(requestValues.getLoadingState(),
                    requestValues.getCurrentLocation(), requestValues.getDistance(), type);

            items.addAll(jsonParser.getItemsFromJson(jsonItems));
        }

        return items;
    }

    private List<String> getGoogleTypes(Set<String> itemTypes) {

        List<String> googleTypes = new ArrayList<>();

        for (String type : itemTypes) {

            if(type.equals(Constants.GASTRONOMY)) {
                googleTypes.addAll(GooglePersistence.gastronomyTypes);

            } else if(type.equals(Constants.ENTERTAINMENT)) {
                googleTypes.addAll(GooglePersistence.entertainmentTypes);
            }
        }

        return googleTypes;
    }

    private String getJsonItems(LoadingState state, GeoCoordinate currentLocation,
                                double distance, String type) {

        String nextToken = jsonParser.getNextToken();

        if(state == LoadingState.FIRST_LOAD || state == LoadingState.RELOAD) {
            return networkClient.makeServiceCall(GooglePersistence.PLACES_URL +
                            GooglePersistence.LOCATION_KEY +
                            currentLocation.getLatitude() + "," + currentLocation.getLongitude() +
                            GooglePersistence.RADIUS_KEY + distance +
                            GooglePersistence.LANGUAGE_KEY + Locale.getDefault().getLanguage() +
                            GooglePersistence.TYPES_KEY + type +
                            GooglePersistence.API_KEY);

        } else if (!nextToken.isEmpty()){
            return networkClient.makeServiceCall(GooglePersistence.PLACES_URL +
                            GooglePersistence.PAGE_TOKEN_KEY + jsonParser.getNextToken() +
                            GooglePersistence.API_KEY);
        }

        return Constants.EMPTY_STRING;
    }
}