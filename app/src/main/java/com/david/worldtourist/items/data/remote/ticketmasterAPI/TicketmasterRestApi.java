package com.david.worldtourist.items.data.remote.ticketmasterAPI;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.data.remote.NetworkClient;;
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

public class TicketmasterRestApi implements RestApi {

    private static TicketmasterRestApi INSTANCE = null;

    private NetworkClient networkClient;
    private TicketmasterJSONParser jsonParser;

    private TicketmasterRestApi(NetworkClient networkClient, TicketmasterJSONParser jsonParser) {
        this.networkClient = networkClient;
        this.jsonParser = jsonParser;
    }

    public static TicketmasterRestApi getInstance(NetworkClient networkClient,
                                                  TicketmasterJSONParser jsonParser) {
        if (INSTANCE == null) {
            INSTANCE = new TicketmasterRestApi(networkClient, jsonParser);
        }
        return INSTANCE;
    }

    @Override
    public Item getItem(@NonNull GetItem.RequestValues requestValues) {

        String jsonItem = networkClient.makeServiceCall(TicketmasterPersistence.EVENT_DETAIL_URL +
                "/" + requestValues.getItemId() + TicketmasterPersistence.FORMAT_KEY +
                TicketmasterPersistence.LOCALE_KEY + Locale.getDefault().getLanguage() +
                TicketmasterPersistence.API_KEY);

        return jsonParser.getItemFromJson(jsonItem);

    }

    @Override
    public List<Item> getItems(@NonNull GetItems.RequestValues requestValues) {

        String jsonItem = getJsonItems(requestValues.getLoadingState(), requestValues.getCurrentLocation(),
                requestValues.getDistance(), requestValues.getItemTypes());

        return jsonParser.getItemsFromJson(jsonItem);
    }

    private String getJsonItems(LoadingState state, GeoCoordinate currentLocation, double distance,
                                Set<String> itemTypes) {

        List<String> types = new ArrayList<>(itemTypes);

        String call = TicketmasterPersistence.EVENTS_URL +
                TicketmasterPersistence.LOCALE_KEY + Locale.getDefault().getLanguage() +
                TicketmasterPersistence.COORDINATES_KEY + currentLocation.getLatitude() +
                "%2C" + currentLocation.getLongitude() +
                TicketmasterPersistence.RADIUS_KEY + (int) (distance / 1000) +
                TicketmasterPersistence.SORT_KEY +
                TicketmasterPersistence.CLASSIFICATION_KEY + getTypes(types) +
                TicketmasterPersistence.API_KEY;

        if(state == LoadingState.FIRST_LOAD || state == LoadingState.RELOAD) {
            jsonParser.setTotalPage(0);
            jsonParser.setCurrentPage(0);

            return networkClient.makeServiceCall(call);

        } else if (jsonParser.getCurrentPage() < jsonParser.getTotalPage()) {

            return networkClient.makeServiceCall (call +
                TicketmasterPersistence.PAGE_KEY + String.valueOf(jsonParser.getCurrentPage()));

        }

        return Constants.EMPTY_STRING;
    }


    private String getTypes(List<String> types) {
        final String COMMA = ",";
        final int LAST_ONE = types.size() - 1;

        String type = Constants.EMPTY_STRING;

        for(int typePosition = 0; typePosition < types.size(); typePosition++) {

            switch (types.get(typePosition)) {

                case Constants.CULTURE:
                    type += TicketmasterPersistence.cultureTypes;
                    break;
                case Constants.NATURE:
                    type += TicketmasterPersistence.natureTypes;
                    break;
                case Constants.ENTERTAINMENT:
                    type += TicketmasterPersistence.entertainmentTypes;
                    break;
                case Constants.GASTRONOMY:
                    type += TicketmasterPersistence.gastronomyTypes;
                    break;
            }

            if(typePosition < LAST_ONE) {
                type += COMMA;
            }
        }

        return type;
    }
}
