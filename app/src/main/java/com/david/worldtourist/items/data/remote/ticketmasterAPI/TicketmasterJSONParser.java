package com.david.worldtourist.items.data.remote.ticketmasterAPI;

import android.util.Log;

import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemDate;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.items.domain.model.Ticket;
import com.david.worldtourist.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TicketmasterJSONParser {

    private final String PAGE = "page";
    private final String TOTAL_PAGES = "totalPages";
    private final String EMBEDDED = "_embedded";
    private final String EVENTS = "events";
    private final String ID = "id";
    private final String NAME = "name";
    private final String CLASSIFICATIONS = "classifications";
    private final String SEGMENT = "segment";
    private final String GENRE = "genre";
    private final String INFO = "info";
    private final String IMAGES = "images";
    private final String URL = "url";
    private final String IMAGE_WIDTH = "width";
    private final String ATTRIBUTION = "attribution";
    private final String DATES = "dates";
    private final String START_DATE = "start";
    private final String END_DATE = "end";
    private final String LOCAL_DATE = "localDate";
    private final String LOCAL_TIME = "localTime";
    private final String PRICE_RANGES = "priceRanges";
    private final String CURRENCY = "currency";
    private final String MIN_PRICE = "min";
    private final String MAX_PRICE = "max";
    private final String VENUES = "venues";
    private final String LOCATION = "location";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    private final String CITY = "city";
    private final String ADDRESS = "address";
    private final String FIRST_ONE = "line1";

    private static TicketmasterJSONParser INSTANCE = null;
    private int totalPage = 0;
    private int currentPage = 0;

    private TicketmasterJSONParser() {

    }

    public static TicketmasterJSONParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TicketmasterJSONParser();
        }
        return INSTANCE;
    }

    int getTotalPage() {
        return totalPage;
    }

    void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    int getCurrentPage() {
        return currentPage;
    }

    void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    List<Item> getItemsFromJson(String json) {

        List<Item> items = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(json);

            if (isJsonEmpty(root)) {
                return items;
            }

            JSONObject embedded = root.optJSONObject(EMBEDDED);

            if (embedded == null) {
                return items;
            }

            JSONArray events = embedded.optJSONArray(EVENTS);

            if (events != null) {
                for (int eventPosition = 0; eventPosition < events.length(); eventPosition++) {
                    JSONObject event = events.getJSONObject(eventPosition);

                    String id = event.optString(ID);
                    String name = event.optString(NAME);
                    ItemType itemType = getItemType(event.optJSONArray(CLASSIFICATIONS));
                    String info = event.optString(URL);
                    List<Photo> photos = getPhotos(event.optJSONArray(IMAGES));
                    GeoCoordinate coordinates = getCoordinates(event.optJSONObject(EMBEDDED).optJSONArray(VENUES));
                    String city = getCity(event.optJSONObject(EMBEDDED).optJSONArray(VENUES));
                    String address = getAddress(event.optJSONObject(EMBEDDED).optJSONArray(VENUES));
                    ItemDate startDate = getDate(event.optJSONObject(DATES).optJSONObject(START_DATE));
                    ItemDate endDate = getDate(event.optJSONObject(DATES).optJSONObject(END_DATE));
                    Ticket ticket = getTicket(event.optJSONArray(PRICE_RANGES));

                    Item item = new Item.Builder(id)
                            .withName(name)
                            .withType(itemType)
                            .withInfo(info)
                            .withPhotos(photos)
                            .withCoordinate(coordinates)
                            .withCity(city)
                            .withAddress(address)
                            .withStartDate(startDate)
                            .withEndDate(endDate)
                            .withTicket(ticket)
                            .build();

                    items.add(item);
                }
            }

        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return items;
        }

        ++currentPage;

        return items;
    }

    Item getItemFromJson(String json) {

        Item item;

        try {

            JSONObject jItem = new JSONObject(json);

            String id = jItem.optString(ID);
            String name = jItem.optString(NAME);
            ItemType itemType = getItemType(jItem.optJSONArray(CLASSIFICATIONS));
            String info = jItem.optString(URL);
            String description = jItem.optString(INFO);
            List<Photo> photos = getPhotos(jItem.optJSONArray(IMAGES));
            GeoCoordinate coordinates = getCoordinates(jItem.optJSONObject(EMBEDDED).optJSONArray(VENUES));
            String city = getCity(jItem.optJSONObject(EMBEDDED).optJSONArray(VENUES));
            String address = getAddress(jItem.optJSONObject(EMBEDDED).optJSONArray(VENUES));
            ItemDate startDate = getDate(jItem.optJSONObject(DATES).optJSONObject(START_DATE));
            ItemDate endDate = getDate(jItem.optJSONObject(DATES).optJSONObject(END_DATE));
            Ticket ticket = getTicket(jItem.optJSONArray(PRICE_RANGES));

            item = new Item.Builder(id)
                    .withName(name)
                    .withType(itemType)
                    .withInfo(info)
                    .withDescription(description)
                    .withPhotos(photos)
                    .withCoordinate(coordinates)
                    .withCity(city)
                    .withAddress(address)
                    .withStartDate(startDate)
                    .withEndDate(endDate)
                    .withTicket(ticket)
                    .build();


        } catch (JSONException e) {
            Log.e("JSON", e.getLocalizedMessage());
            return Item.EMPTY_ITEM;
        }

        return item;
    }

    private boolean isJsonEmpty(JSONObject json) throws JSONException {

        if (json.length() > 0) {
            JSONObject page = json.getJSONObject(PAGE);
            int pages = page.getInt(TOTAL_PAGES);

            if (pages > 0) {
                totalPage = pages;
                return false;
            }
        }

        return true;
    }

    private ItemType getItemType(JSONArray classifications) throws JSONException {

        if (classifications != null) {
            JSONObject classification = classifications.getJSONObject(0);
            JSONObject genre = classification.optJSONObject(GENRE);
            String id = genre.optString(ID);

            ItemType type = checkIfIdMatchItemType(id);

            if (type != ItemType.NONE) {
                return type;
            }

            JSONObject segment = classification.optJSONObject(SEGMENT);
            id = segment.optString(ID);

            return checkIfIdMatchItemType(id);
        }

        return ItemType.NONE;
    }

    private ItemType checkIfIdMatchItemType(String id) {

        if (id != null) {

            if (TicketmasterPersistence.cultureTypes.contains(id)) {
                return ItemType.CULTURE;

            } else if (TicketmasterPersistence.natureTypes.contains(id)) {
                return ItemType.NATURE;

            } else if (TicketmasterPersistence.gastronomyTypes.contains(id)) {
                return ItemType.GASTRONOMY;

            } else if (TicketmasterPersistence.entertainmentTypes.contains(id)) {
                return ItemType.ENTERTAINMENT;
            }
        }

        return ItemType.NONE;
    }

    private List<Photo> getPhotos(JSONArray images) throws JSONException {

        final int MIN_WIDTH = 100;
        final int MAX_WIDTH = 400;

        List<Photo> photos = new ArrayList<>();

        if (images != null) {
            for (int photoPosition = 0; photoPosition < images.length(); photoPosition++) {
                JSONObject jPhoto = images.getJSONObject(photoPosition);
                int width = jPhoto.optInt(IMAGE_WIDTH);

                if (width > MIN_WIDTH && width < MAX_WIDTH) {
                    Photo photo = new Photo();
                    photo.setPhoto(jPhoto.optString(URL));
                    photo.setAuthor(jPhoto.optString(ATTRIBUTION));
                    photos.add(photo);
                    return photos;
                }
            }
        }

        return photos;
    }

    private GeoCoordinate getCoordinates(JSONArray venues) throws JSONException {

        if (venues != null) {
            JSONObject place = venues.getJSONObject(0);
            JSONObject location = place.optJSONObject(LOCATION);
            GeoCoordinate coordinates = new GeoCoordinate();
            coordinates.setLatitude(location.optDouble(LATITUDE));
            coordinates.setLongitude(location.optDouble(LONGITUDE));
            return coordinates;
        }

        return GeoCoordinate.EMPTY_COORDINATE;
    }

    private String getCity(JSONArray venues) throws JSONException {

        if (venues != null) {
            JSONObject place = venues.optJSONObject(0);
            JSONObject city = place.optJSONObject(CITY);
            return city.optString(NAME);
        }

        return Constants.EMPTY_STRING;
    }

    private String getAddress(JSONArray venues) throws JSONException {

        if (venues != null) {
            JSONObject place = venues.optJSONObject(0);
            JSONObject address = place.optJSONObject(ADDRESS);

            if(address != null)
                return address.optString(FIRST_ONE);
        }

        return Constants.EMPTY_STRING;
    }

    private ItemDate getDate(JSONObject jDate) throws JSONException {

        if (jDate != null) {
            ItemDate date = new ItemDate();
            date.setDate(jDate.optString(LOCAL_DATE));
            date.setTime(jDate.optString(LOCAL_TIME));
            return date;
        }

        return ItemDate.EMPTY_DATE;
    }

    private Ticket getTicket(JSONArray prices) throws JSONException {

        if (prices != null) {
            JSONObject jTicket = prices.getJSONObject(0);
            Ticket ticket = new Ticket();
            ticket.setCurrency(jTicket.optString(CURRENCY));
            ticket.setMaxPrice(jTicket.optDouble(MAX_PRICE));
            ticket.setMinPrice(jTicket.optDouble(MIN_PRICE));
            return ticket;
        }

        return Ticket.EMPTY_TICKET;
    }
}