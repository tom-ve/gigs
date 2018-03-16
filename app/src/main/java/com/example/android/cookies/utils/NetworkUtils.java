package com.example.android.cookies.utils;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String EVENTS_BASE_URL = "http://api.songkick.com/api/3.0/";
    private static final String METRO_AREAS = "metro_areas/";
    private static final String SEARCH = "search/";
    private static final String LOCATIONS_JSON = "locations.json";
    private static final String CALENDAR_JSON = "/calendar.json";

    private static final String QUERY = "query";
    private static final String API_KEY = "apikey";
    private static final String API_KEY_VALUE = "GaG14A2N8GoWxHbs";


    public static String getLocationsQueryUrlString(String location) {
        return EVENTS_BASE_URL + SEARCH + LOCATIONS_JSON + "?" + API_KEY + "=" + API_KEY_VALUE +
                "&" + QUERY + "=" + location;
    }

    public static String getMetroAreaCalendarUrlString(int metroAreaId) {
        return EVENTS_BASE_URL + METRO_AREAS + metroAreaId + CALENDAR_JSON +
                "?" + API_KEY + "=" + API_KEY_VALUE;
    }
}