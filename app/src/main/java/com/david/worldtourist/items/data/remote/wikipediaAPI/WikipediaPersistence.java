package com.david.worldtourist.items.data.remote.wikipediaAPI;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class WikipediaPersistence {

    private static final String[] WIKIPEDIA_LANGUAGES = {"en", "es"};
    private static final String DEFAULT_WIKIPEDIA_LANGUAGE = "en";

    private static String URL_ROOT = "https://" + getLanguage() + ".wikipedia.org/w/api.php?";

    private static final String FORMAT_KEY = "format=json";
    private static final String ACTION_KEY = "&action=query";

    private static final String GET_DETAILS_PROP_KEY = "&prop=coordinates%7Cextracts";
    private static final String GET_DETAILS_SPEC_KEY = "&exintro=";

    private static final String GET_IMAGES_PROP_KEY = "&generator=images&prop=imageinfo";
    private static final String GET_IMAGES_SPEC_KEY = "&iiprop=url&gimlimit=10&iiurlwidth=400";

    private static final String GET_DATA_PROP_KEY = "&generator=geosearch&prop=coordinates%7Cpageimages";
    private static final String GET_DATA_SPEC_KEY = "&colimit=50&ggslimit=50&pilimit=50&pithumbsize=400";

    static final String ID_KEY = "&pageids=";
    static final String COORDINATES_KEY = "&ggscoord=";
    static final String RADIUS = "&ggsradius=";

    static final String WIKIPEDIA_GEOSEARCH_URL = URL_ROOT + FORMAT_KEY + ACTION_KEY +
            GET_DATA_PROP_KEY + GET_DATA_SPEC_KEY;

    static final String WIKIPEDIA_DETAIL_URL = URL_ROOT + FORMAT_KEY + ACTION_KEY +
            GET_DETAILS_PROP_KEY + GET_DETAILS_SPEC_KEY;

    static final String WIKIPEDIA_PHOTOS_URL = URL_ROOT + FORMAT_KEY + ACTION_KEY +
            GET_IMAGES_PROP_KEY + GET_IMAGES_SPEC_KEY;

    static final String WIKIPEDIA_PAGE_URL = "https://" + getLanguage() + ".wikipedia.org/wiki/";


    private static final String[] CULTURE_TYPES_ES = {"iglesia", "museo", "capilla", "torre", "cripta",
            "catedral", "monasterio", "escultura", "estatua", "palacio", "archivo", "teatro", "monumento",
            "memorial", "castillo", "mezquita"};

    private static final String[] CULTURE_TYPES_DEFAULT = {"church", "museum", "chapel", "tower", "crypt",
            "cathedral", "monastery", "sculpture", "statue", "palace", "archive", "theater", "monument",
            "memorial", "castle", "mosque"};

    private static final String[] NATURE_TYPES_ES = {"bosque", "natural", "pico", "montaña"};

    private static final String[] NATURE_TYPES_DEFAULT = {"wood", "natural", "peak", "mountain"};


    public static List<String> getCultureTypes() {

        if(getLanguage().equals("es")) {
            return Arrays.asList(CULTURE_TYPES_ES);
        }
        return Arrays.asList(CULTURE_TYPES_DEFAULT);
    }

    public static List<String> getNatureTypes() {

        if(getLanguage().equals("es")) {
            return Arrays.asList(NATURE_TYPES_ES);
        }
        return Arrays.asList(NATURE_TYPES_DEFAULT);
    }

    private static String getLanguage() {

        String language = Locale.getDefault().getLanguage();

        if (Arrays.asList(WIKIPEDIA_LANGUAGES).contains(language)) {
            return language;
        }
        return DEFAULT_WIKIPEDIA_LANGUAGE;
    }
}