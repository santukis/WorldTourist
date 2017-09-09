package com.david.worldtourist.items.data.remote.ticketmasterAPI;


class TicketmasterPersistence {

    static final String API_KEY = "&apikey=XGNNrjc4Khvvc5rZSDeEtnPC181Vzq2Y";

    private final static String URL_ROOT = "https://app.ticketmaster.com/discovery/v2";

    private final static String EVENTS_KEY = "/events";

    final static String FORMAT_KEY = ".json?";
    private final static String DISTANCE_UNIT_KEY = "unit=km";
    private final static String SIZE_KEY = "&size=50";
    final static String CLASSIFICATION_KEY = "&classificationId=";
    final static String LOCALE_KEY = "&locale=";
    final static String SORT_KEY = "&sort=distance,asc";
    final static String COORDINATES_KEY = "&geoPoint=";
    final static String RADIUS_KEY = "&radius=";
    final static String PAGE_KEY = "&page=";


    final static String EVENTS_URL = URL_ROOT + EVENTS_KEY + FORMAT_KEY +
            DISTANCE_UNIT_KEY + SIZE_KEY;

    final static String EVENT_DETAIL_URL = URL_ROOT + EVENTS_KEY;


    ////////////////////////////////////////////////////////////////////////////////////

    // CULTURE = Classic Music (KnvZfZ7vAeJ), Film Documentary (KnvZfZ7vAkk), Theater classical (KnvZfZ7v7nJ),
    // Theater cultural (KnvZfZ7v7nE), Theater Fine Art (KnvZfZ7v7nl), Opera (KnvZfZ7v7lk), Performance (KnvZfZ7v7l6),
    // theater (KnvZfZ7v7l1), Miscellaneous cultural (KnvZfZ7v7lE), Dance (KnvZfZ7v7nI)

    // NATURE = Sports Extreme (KnvZfZ7vAdJ)

    // GASTRONOMY = Food and Drink (KnvZfZ7vAAI)

    // ENTERTAINMENT = Music (KZFzniwnSyZfZ7v7nJ), Film (KZFzniwnSyZfZ7v7nn), Sport(KZFzniwnSyZfZ7v7nE),
    // Circus (KnvZfZ7v7n1), Comedy (KnvZfZ7vAe1), Magic (KnvZfZ7v7lv), Puppetry (KnvZfZ7v7lF),
    // Children Theater (KnvZfZ7v7na), Hobby (KnvZfZ7v7lt)

    public static final String cultureTypes = "KnvZfZ7vAeJ,KnvZfZ7vAkk,KnvZfZ7v7nJ,KnvZfZ7v7nE,KnvZfZ7v7nl," +
            "KnvZfZ7v7lk,KnvZfZ7v7l6,KnvZfZ7v7l1,KnvZfZ7v7lE,KnvZfZ7v7nI";
    public static final String natureTypes = "KnvZfZ7vAdJ";
    public static final String gastronomyTypes = "KnvZfZ7vAAI";
    public static final String entertainmentTypes = "KZFzniwnSyZfZ7v7nJ,KZFzniwnSyZfZ7v7nn,KZFzniwnSyZfZ7v7nE," +
            "KnvZfZ7v7n1,KnvZfZ7vAe1,KnvZfZ7v7lv,KnvZfZ7v7lF,KnvZfZ7v7na,KnvZfZ7v7lt";

}
