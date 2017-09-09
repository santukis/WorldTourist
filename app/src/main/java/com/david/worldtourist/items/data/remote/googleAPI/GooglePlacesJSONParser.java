package com.david.worldtourist.items.data.remote.googleAPI;


import android.util.Log;

import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GooglePlacesJSONParser {

    private final String CONTINUE = "next_page_token";
    private final String STATUS = "status";
    private final String OK = "OK";
    private final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
    private final String RESULTS = "results";
    private final String RESULT = "result";
    private final String TYPES = "types";
    private final String GEOMETRY = "geometry";
    private final String LOCATION = "location";
    private final String PHOTOS = "photos";
    private final String PLACE_ID = "place_id";
    private final String NAME = "name";
    private final String LATITUDE = "lat";
    private final String LONGITUDE = "lng";
    private final String WEBSITE = "website";
    private final String PHONE = "international_phone_number";
    private final String PHOTO_REFERENCE = "photo_reference";
    private final String OPENNING_HOURS = "opening_hours";
    private final String ATTRIBUTIONS = "html_attributions";
    private final String WEEK_DAY = "weekday_text";
    private final String VICINITY = "vicinity";

    private static GooglePlacesJSONParser INSTANCE = null;
    private String nextToken = Constants.EMPTY_STRING;

    private GooglePlacesJSONParser() {

    }

    public static GooglePlacesJSONParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GooglePlacesJSONParser();
        }
        return INSTANCE;
    }

    String getNextToken() {
        return nextToken;
    }

    synchronized List<Item> getItemsFromJson(String json) {

        List<Item> items = new ArrayList<>();

        try {
            JSONObject googleObject = new JSONObject(json);

            if (googleObject.getString(STATUS).equals(OK)) {

                nextToken = googleObject.optString(CONTINUE);

                JSONArray results = googleObject.getJSONArray(RESULTS);

                for (int placePosition = 0; placePosition < results.length(); placePosition++) {
                    JSONObject place = results.getJSONObject(placePosition);

                    ItemType itemType = getItemType(place.optJSONArray(TYPES));

                    GeoCoordinate coordinates = getCoordinates(
                            place.optJSONObject(GEOMETRY).optJSONObject(LOCATION));

                    List<Photo> placePhotos = getPhotos(place.optJSONArray(PHOTOS));

                    Item item = new Item.Builder(place.getString(PLACE_ID))
                            .withName(place.getString(NAME))
                            .withAddress(place.optString(VICINITY))
                            .withCoordinate(coordinates)
                            .withType(itemType)
                            .withPhotos(placePhotos)
                            .build();
                    items.add(item);
                }

            } else if (googleObject.getString(STATUS).equals(OVER_QUERY_LIMIT)) {
                return items;
            }

        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return items;
        }

        return items;
    }

    Item getItemFromJson(String json) {

        Item item = Item.EMPTY_ITEM;

        try {
            JSONObject googleObject = new JSONObject(json);

            if (googleObject.getString(STATUS).equals(OK)) {
                JSONObject result = googleObject.getJSONObject(RESULT);

                ItemType itemType = getItemType(result.optJSONArray(TYPES));

                GeoCoordinate coordinates = getCoordinates(
                        result.optJSONObject(GEOMETRY).optJSONObject(LOCATION));

                List<Photo> photos = getPhotos(result.optJSONArray(PHOTOS));

                String[] timetable = getTimetable(result.optJSONObject(OPENNING_HOURS));

                item = new Item.Builder(result.getString(PLACE_ID))
                        .withName(result.getString(NAME))
                        .withAddress(result.optString(VICINITY))
                        .withCoordinate(coordinates)
                        .withTimetable(timetable)
                        .withInfo(result.optString(WEBSITE))
                        .withPhone(result.optString(PHONE))
                        .withPhotos(photos)
                        .withType(itemType)
                        .build();

            } else if (googleObject.getString(STATUS).equals(OVER_QUERY_LIMIT)) {
                return item;
            }

        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return item;
        }

        return item;
    }

    private ItemType getItemType(JSONArray types) throws JSONException{

        if(types != null) {

            for(int typePosition = 0; typePosition < types.length(); typePosition++) {
                String type = types.getString(typePosition);

                if(GooglePersistence.entertainmentTypes.contains(type)) {
                    return ItemType.ENTERTAINMENT;

                } else if(GooglePersistence.gastronomyTypes.contains(type)) {
                    return ItemType.GASTRONOMY;
                }
            }
        }

        return ItemType.NONE;
    }

    private GeoCoordinate getCoordinates(JSONObject location) throws JSONException {

        if(location != null) {
            double latitude = location.optDouble(LATITUDE);
            double longitude = location.optDouble(LONGITUDE);
            return new GeoCoordinate(latitude, longitude);
        }

        return GeoCoordinate.EMPTY_COORDINATE;
    }

    private List<Photo> getPhotos(JSONArray photos) throws JSONException {

        List<Photo> placePhotos = new ArrayList<>();

        if (photos != null) {
            for (int photoPosition = 0; photoPosition < photos.length(); photoPosition++) {
                JSONObject jsonPhoto = photos.getJSONObject(photoPosition);
                JSONArray attributions = jsonPhoto.getJSONArray(ATTRIBUTIONS);

                Photo photo = new Photo(
                        GooglePersistence.PHOTOS_URL +
                                jsonPhoto.getString(PHOTO_REFERENCE) +
                                GooglePersistence.API_KEY,
                        attributions.optString(0));
                placePhotos.add(photo);
            }
        }

        return placePhotos;
    }

    private String[] getTimetable(JSONObject openingHours) throws JSONException {
        final int WEEK_SIZE = 7;

        String[] timetable = new String[0];

        if (openingHours != null) {
            JSONArray weekdayText = openingHours.optJSONArray(WEEK_DAY);

            timetable = new String[WEEK_SIZE];

            for (int timePosition = 0; timePosition < weekdayText.length(); timePosition++) {
                timetable[timePosition] = weekdayText.getString(timePosition);
            }
            return timetable;
        }

        return timetable;
    }
}
