package com.david.worldtourist.items.data.remote.googleAPI;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class GooglePersistence {

    public static final String API_KEY = "&key=AIzaSyAbxQ_AqXmUtxxnc2t9Bo2At7ummq8Uq9w";

    private static final String URL_ROOT = "https://maps.googleapis.com/maps/api";

    //Places
    static final String PLACES_URL = URL_ROOT + "/place/nearbysearch/json?";
    static final String PLACE_DETAILS_URL = URL_ROOT + "/place/details/json?";
    static final String PHOTOS_URL = URL_ROOT + "/place/photo?maxwidth=400&photoreference=";

    static final String LOCATION_KEY = "location=";
    static final String RADIUS_KEY = "&radius=";
    static final String TYPES_KEY= "&types=";
    public static final String LANGUAGE_KEY = "&language=";
    static final String PLACE_ID_KEY = "placeid=";
    static final String PAGE_TOKEN_KEY = "pagetoken=";

    //Directions
    public static final String DIRECTIONS_URL = URL_ROOT + "/directions/json?";

    public static final String ORIGIN_KEY = "origin=";
    public static final String DESTINATION_KEY = "&destination=";
    public static final String MODE_KEY = "&mode=";

    //Geocoding
    public static final String GEOCODING_URL = URL_ROOT + "/geocode/json?";

    public static final String ADDRESS_KEY = "address=";


    ////////////////////////////////////////////////////////////////////////////////////////

    static final List<String> gastronomyTypes = Collections.singletonList("restaurant");
    static final List<String> entertainmentTypes = Arrays.asList("amusement_park",
            "aquarium", "zoo", "night_club");
}