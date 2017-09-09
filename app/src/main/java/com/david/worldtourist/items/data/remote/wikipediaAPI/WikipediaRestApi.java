package com.david.worldtourist.items.data.remote.wikipediaAPI;


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
import java.util.Set;

public class WikipediaRestApi implements RestApi {


    private static WikipediaRestApi INSTANCE = null;

    private NetworkClient networkClient;
    private WikipediaJSONParser jsonParser;

    private WikipediaRestApi(NetworkClient networkClient, WikipediaJSONParser jsonParser) {
        this.networkClient = networkClient;
        this.jsonParser = jsonParser;
    }

    public static WikipediaRestApi getInstance(NetworkClient networkClient,
                                               WikipediaJSONParser jsonParser) {
        if (INSTANCE == null) {
            INSTANCE = new WikipediaRestApi(networkClient, jsonParser);
        }
        return INSTANCE;
    }

    @Override
    public Item getItem(@NonNull GetItem.RequestValues requestValues) {

        String jsonItem = networkClient.makeServiceCall(
                WikipediaPersistence.WIKIPEDIA_DETAIL_URL +
                        WikipediaPersistence.ID_KEY +
                        requestValues.getItemId());

        Item item = jsonParser.getItemFromJson(jsonItem);

        jsonItem = networkClient.makeServiceCall(WikipediaPersistence.WIKIPEDIA_PHOTOS_URL +
                WikipediaPersistence.ID_KEY + requestValues.getItemId());

        item.setPhotos(jsonParser.getPhotosFromJson(jsonItem));

        return item;
    }

    @Override
    public List<Item> getItems(@NonNull GetItems.RequestValues requestValues) {

        if(requestValues.getLoadingState() == LoadingState.UPDATE) {
            return new ArrayList<>();
        }

        GeoCoordinate currentLocation = requestValues.getCurrentLocation();
        double distance = requestValues.getDistance();

        String jsonItems = networkClient.makeServiceCall(
                WikipediaPersistence.WIKIPEDIA_GEOSEARCH_URL +
                        WikipediaPersistence.COORDINATES_KEY + currentLocation.getLatitude() +
                        "%7C" + currentLocation.getLongitude() +
                        WikipediaPersistence.RADIUS + distance);

        List<Item> unfilteredItems = jsonParser.getItemsFromJson(jsonItems);

        return filterItemsByType(unfilteredItems, getWikiTypes(requestValues.getItemTypes()));

    }

    private List<String> getWikiTypes(Set<String> itemTypes) {

        List<String> wikiTypes = new ArrayList<>();

        for(String type : itemTypes) {
            if(type.equals(Constants.NATURE)) {
                wikiTypes.add(Constants.NATURE);

            } else if(type.equals(Constants.CULTURE)) {
                wikiTypes.add(Constants.CULTURE);
            }
        }
        return wikiTypes;
    }

    private List<Item> filterItemsByType(List<Item> unfilteredItems, List<String> types) {

        List<Item> filteredItems = new ArrayList<>();

        for(String type : types) {
            for(Item item : unfilteredItems) {
                if(item.getType().toString().equals(type)) {
                    filteredItems.add(item);
                }
            }
        }

        return filteredItems;
    }
}
