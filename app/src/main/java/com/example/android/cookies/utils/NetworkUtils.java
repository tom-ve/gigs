/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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