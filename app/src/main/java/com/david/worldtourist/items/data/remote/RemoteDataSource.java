package com.david.worldtourist.items.data.remote;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsDataSource;
import com.david.worldtourist.items.data.boundary.RestApi;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RemoteDataSource implements ItemsDataSource.Remote {

    private static RemoteDataSource INSTANCE = null;

    private final RestApi ticketmasterRestApi;
    private final RestApi googleRestApi;
    private final RestApi wikipediaRestApi;

    private RemoteDataSource(RestApi ticketmasterRestApi,
                             RestApi googleRestApi,
                             RestApi wikipediaRestApi) {
        this.ticketmasterRestApi = ticketmasterRestApi;
        this.googleRestApi = googleRestApi;
        this.wikipediaRestApi = wikipediaRestApi;
    }

    public static RemoteDataSource getInstance(RestApi ticketmasterRestApi,
                                               RestApi googleRestApi,
                                               RestApi wikipediaRestApi) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(ticketmasterRestApi, googleRestApi, wikipediaRestApi);
        }
        return INSTANCE;
    }

    @Override
    public void getItem(@NonNull GetItem.RequestValues requestValues,
                        @NonNull final UseCase.Callback<GetItem.ResponseValues> callback) {

        Item item = Item.EMPTY_ITEM;

        switch (requestValues.getItemCategory()) {

            case EVENT:
                item = ticketmasterRestApi.getItem(requestValues);

                if(item == Item.EMPTY_ITEM) {
                    callback.onError(Constants.EMPTY_OBJECT_ERROR);

                } else {
                    callback.onSuccess(new GetItem.ResponseValues(item));
                }
                break;

            case SITE:
                ItemType itemType = requestValues.getItemType();

                if (itemType == ItemType.CULTURE || itemType == ItemType.NATURE) {
                    item = wikipediaRestApi.getItem(requestValues);

                } else if (itemType == ItemType.GASTRONOMY || itemType == ItemType.ENTERTAINMENT) {
                    item = googleRestApi.getItem(requestValues);
                }

                if (item != Item.EMPTY_ITEM) {
                    callback.onSuccess(new GetItem.ResponseValues(item));

                } else {
                    callback.onError(Constants.EMPTY_OBJECT_ERROR);
                }
                break;
        }
    }

    @Override
    public void getItems(@NonNull GetItems.RequestValues requestValues,
                         @NonNull final UseCase.Callback<GetItems.ResponseValues> callback) {

        List<Item> items = new ArrayList<>();

        switch (requestValues.getItemCategory()) {

            case EVENT:
                items = ticketmasterRestApi.getItems(requestValues);

                if (items.isEmpty()) {
                    callback.onError(Constants.EMPTY_LIST_ERROR);

                } else {
                    orderItemsByDate(items);
                    callback.onSuccess(new GetItems.ResponseValues(items));
                }
                break;

            case SITE:
                items.addAll(wikipediaRestApi.getItems(requestValues));
                items.addAll(googleRestApi.getItems(requestValues));


                if (!items.isEmpty()) {
                    orderItemsByDistance(items, requestValues.getCurrentLocation());
                    callback.onSuccess(new GetItems.ResponseValues(items));

                } else {
                    callback.onError(Constants.EMPTY_LIST_ERROR);
                }
                break;
        }
    }

    private void orderItemsByDate(List<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item firstItem, Item secondItem) {
                String date1 = firstItem.getStartDate().getDate();
                String date2 = secondItem.getStartDate().getDate();
                return date1.compareTo(date2);
            }
        });
    }

    private void orderItemsByDistance(List<Item> items, final GeoCoordinate userLocation) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item firstItem, Item secondItem) {
                Double distance1 = GeoCoordinate.getDistance(userLocation, firstItem.getCoordinate());
                Double distance2 = GeoCoordinate.getDistance(userLocation, secondItem.getCoordinate());
                return distance1.compareTo(distance2);
            }
        });
    }
}
