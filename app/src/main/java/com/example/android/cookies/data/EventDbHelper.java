package com.example.android.cookies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.cookies.data.EventContract.*;

public class EventDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "event.db";

    private static final int DATABASE_VERSION = 2;

    public EventDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_EVENT_TABLE =
                "CREATE TABLE "                 + EventEntry.TABLE_NAME + " ("                +
                    EventEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                    EventEntry.COLUMN_ARTIST        + " TEXT NOT NULL, "                      +
                    EventEntry.COLUMN_PERFORMANCE   + " TEXT NOT NULL, "                      +
                    EventEntry.COLUMN_TYPE          + " TEXT NOT NULL, "                      +
                    EventEntry.COLUMN_VENUE_NAME   + " TEXT NOT NULL, "                       +
                    EventEntry.COLUMN_VENUE_CITY   + " TEXT NOT NULL);";


        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}