package com.david.worldtourist.items.data.remote.wikipediaAPI;


import android.text.Html;
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

public class WikipediaJSONParser {

    private final String QUERY = "query";
    private final String PAGES = "pages";
    private final String TITLE = "title";
    private final String LATITUDE = "lat";
    private final String LONGITUDE = "lon";
    private final String ID = "pageid";
    private final String COORDINATES = "coordinates";
    private final String THUMBNAIL = "thumbnail";
    private final String SOURCE = "source";
    private final String THUMBURL = "thumburl";
    private final String EXTRACTS = "extract";
    private final String IMAGEINFO = "imageinfo";
    private final String FLAG = "flag";
    private final String COMMONS = "commons";

    private static WikipediaJSONParser INSTANCE = null;

    private WikipediaJSONParser() {}

    public static WikipediaJSONParser getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WikipediaJSONParser();
        }
        return INSTANCE;
    }

    synchronized List<Item> getItemsFromJson(String json) {

        List<Item> items = new ArrayList<>();

        try {
            JSONObject wikiObject = new JSONObject(json);

            JSONObject pages = wikiObject.getJSONObject(QUERY).getJSONObject(PAGES);
            JSONArray pageId = pages.names();

            for (int itemPosition = 0; itemPosition < pages.length(); itemPosition++) {
                JSONObject itemJson = pages.getJSONObject(pageId.getString(itemPosition));

                ItemType itemType = getItemType(itemJson.optString(TITLE));

                if (itemType == ItemType.NONE)
                    continue;

                GeoCoordinate coordinates = getCoordinates(itemJson.optJSONArray(COORDINATES));

                if (coordinates == GeoCoordinate.EMPTY_COORDINATE)
                    continue;

                List<Photo> photoList = getPhoto(itemJson.optJSONObject(THUMBNAIL));

                Item item = new Item.Builder(itemJson.getString(ID))
                        .withName(itemJson.getString(TITLE))
                        .withCoordinate(coordinates)
                        .withType(itemType)
                        .withPhotos(photoList)
                        .build();

                items.add(item);
            }

        } catch (JSONException e) {
            Log.e("WIKIPEDIA", e.getLocalizedMessage());
            return items;
        }

        return items;
    }

    Item getItemFromJson(String json) {

        Item item = Item.EMPTY_ITEM;

        try {
            JSONObject wikiObject = new JSONObject(json);
            JSONObject pages = wikiObject.getJSONObject(QUERY).getJSONObject(PAGES);
            JSONArray pageId = pages.names();

            for (int pagePosition = 0; pagePosition < pageId.length(); pagePosition++) {
                JSONObject itemJson = pages.getJSONObject(pageId.getString(pagePosition));

                ItemType itemType = getItemType(itemJson.optString(TITLE));

                GeoCoordinate coordinates = getCoordinates(itemJson.optJSONArray(COORDINATES));

                if (coordinates == GeoCoordinate.EMPTY_COORDINATE)
                    continue;

                String summary = getSummary(itemJson.optString(EXTRACTS));

                String description = getDescription(itemJson.optString(EXTRACTS));

                String info = WikipediaPersistence.WIKIPEDIA_PAGE_URL +
                        formatItemName(itemJson.getString(TITLE));

                item = new Item.Builder(itemJson.getString(ID))
                        .withName(itemJson.getString(TITLE))
                        .withCoordinate(coordinates)
                        .withType(itemType)
                        .withSummary(summary)
                        .withDescription(description)
                        .withInfo(info)
                        .build();
            }


        } catch (JSONException e) {
            Log.e("WIKIPEDIA", e.getLocalizedMessage());
            return item;
        }

        return item;
    }

    private ItemType getItemType(String title) {

        title = title.toLowerCase();

        List<String> cultureTypes = WikipediaPersistence.getCultureTypes();

        for (String type : cultureTypes) {
            if (title.contains(type))
                return ItemType.CULTURE;
        }

        List<String> natureTypes = WikipediaPersistence.getNatureTypes();

        for (String type : natureTypes) {
            if(title.contains(type))
                return ItemType.NATURE;
        }

        return ItemType.NONE;
    }

    private GeoCoordinate getCoordinates(JSONArray jCoordinates) throws JSONException {

        if(jCoordinates != null) {
            double latitude = jCoordinates.optJSONObject(0).optDouble(LATITUDE);
            double longitude = jCoordinates.optJSONObject(0).optDouble(LONGITUDE);
            return new GeoCoordinate(latitude, longitude);
        }

        return GeoCoordinate.EMPTY_COORDINATE;
    }

    private List<Photo> getPhoto(JSONObject jPhoto) throws JSONException {

        List<Photo> photos = new ArrayList<>();

        if(jPhoto != null) {
            String photoUrl = jPhoto.optString(SOURCE);
            photos.add(new Photo(photoUrl, ""));
        }

        return photos;
    }

    private String formatItemName(String name) {
        final String SPACE = " ";
        final String UNDERSCORE = "_";

        return name.replaceAll(SPACE, UNDERSCORE);
    }

    List<Photo> getPhotosFromJson(String json) {

        List<Photo> itemPhotos = new ArrayList<>();

        try {
            JSONObject wikiObject = new JSONObject(json);
            JSONObject pages = wikiObject.getJSONObject(QUERY).getJSONObject(PAGES);
            JSONArray pageId = pages.names();

            for (int pagePosition = 0; pagePosition < pageId.length(); pagePosition++) {
                JSONObject itemJson = pages.getJSONObject(pageId.getString(pagePosition));

                if (itemJson.getString(TITLE).toLowerCase().contains(FLAG) ||
                        itemJson.getString(TITLE).toLowerCase().contains(COMMONS))
                    continue;

                if (itemJson.optJSONArray(IMAGEINFO) != null) {
                    String url = itemJson.optJSONArray(IMAGEINFO).optJSONObject(0).optString(THUMBURL);
                    Photo photo = new Photo(url, "");
                    itemPhotos.add(photo);
                }
            }

        } catch (JSONException e) {
            Log.e("WIKIPEDIA", e.getLocalizedMessage());
            return itemPhotos;
        }

        return itemPhotos;
    }

    private String getSummary(String jsonDescription) {
        String summary = Html.fromHtml(jsonDescription).toString();

        if(summary.length() > 200) {
            summary = summary.substring(0, 200);
        }

        return summary;
    }

    private String getDescription(String jsonDescription) {

        String description = Constants.EMPTY_STRING;

        if (jsonDescription != null) {

            final String DESCRIPTION_FORMAT_START = "<body style=\"font-size:small;color:gray;\"align=\"justify\">";
            final String DESCRIPTION_FORMAT_END = "</body>";

            description = DESCRIPTION_FORMAT_START + jsonDescription + DESCRIPTION_FORMAT_END + "<br> WIKIPEDIA";

            return description;
        }

        return description;
    }
}