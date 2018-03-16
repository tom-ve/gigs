package com.example.android.cookies.data;

import android.provider.BaseColumns;

public class EventContract {

    public static final class EventEntry implements BaseColumns {

        public static final String TABLE_NAME = "event";

        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_PERFORMANCE = "performance";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_VENUE_NAME = "venueName";
        public static final String COLUMN_VENUE_CITY = "venueCity";
    }
}