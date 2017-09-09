package com.david.worldtourist.itemsmap.data.remote;


import android.util.Log;

import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.domain.usecase.model.Address;
import com.david.worldtourist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class GoogleGeocodingJSONParser {

    private final String STATUS = "status";
    private final String OK = "OK";
    private final String RESULTS = "results";
    private final String ADDRESS = "address_components";
    private final String NAME = "long_name";
    private final String GEOMETRY = "geometry";
    private final String LOCATION = "location";
    private final String LATITUDE = "lat";
    private final String LONGITUDE  ="lng";

    Address getAddressFromJson(String json) {

        Address address = new Address();

        try {
            JSONObject googleObject = new JSONObject(json);

            if (googleObject.getString(STATUS).equals(OK)) {
                JSONArray results = googleObject.getJSONArray(RESULTS);

                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);

                    JSONArray addresses = result.optJSONArray(ADDRESS);

                    address.setName(getAddressName(addresses));

                    JSONObject geometry = result.optJSONObject(GEOMETRY);
                    JSONObject location = geometry.optJSONObject(LOCATION);

                    address.setCoordinates(getAddressCoordinates(location));
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return Address.EMPTY_ADDRESS;
        }

        return address;
    }

    private String getAddressName(JSONArray addresses) throws JSONException {
        String name = Constants.EMPTY_STRING;

        if(addresses != null) {
            name = addresses.getJSONObject(0).getString(NAME);
        }

        return name;
    }

    private GeoCoordinate getAddressCoordinates(JSONObject location) throws JSONException {
        GeoCoordinate coordinates = new GeoCoordinate();

        if(location != null) {
            coordinates.setLatitude(location.getDouble(LATITUDE));
            coordinates.setLongitude(location.getDouble(LONGITUDE));
        }

        return coordinates;
    }
}
