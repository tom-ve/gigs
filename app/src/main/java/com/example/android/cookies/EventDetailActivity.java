package com.example.android.cookies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cookies.Entities.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.TreeMap;

import static android.provider.CalendarContract.*;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = EventDetailActivity.class.getSimpleName();
    private static final int MY_PERMISSION_REQUEST_READ_CALENDAR = 5432;
    private static final String ACCOUNT_NAME = "com.example.android.cookies";
    private static final String OWNER_NAME = "vaneyndetom@gmail.com";
    private static final String CALENDAR_NAME = "vaneyndetom@gmail.com";
    private static final String CALENDAR_DISPLAY_NAME = "Gig events";

    private Event event;
    private TextView DisplayNameTextView;
    private TextView performanceDisplayNameTextView;
    private TextView dateTimeTextView;
    private TextView venueDisplayNameTextView;
    private TextView venueStreetTextView;
    private TextView venueCityTextView;
    private Button addToCalendarButton;

    private View.OnClickListener addToCalendarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addToCalendarButtonClicked();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //Setting up the elements
        DisplayNameTextView = findViewById(R.id.tv_display_name);
        performanceDisplayNameTextView = findViewById(R.id.tv_performance_display_name);
        dateTimeTextView = findViewById(R.id.tv_datetime);
        venueDisplayNameTextView = findViewById(R.id.tv_venue_display_name);
        venueStreetTextView = findViewById(R.id.tv_venue_street);
        venueCityTextView = findViewById(R.id.tv_venue_city_display_name);

        addToCalendarButton = findViewById(R.id.button_add_to_calendar);
        addToCalendarButton.setOnClickListener(addToCalendarOnClickListener);

        //Fill elements
        Intent intentThatStartedThisIntent = getIntent();
        getEventDetails(intentThatStartedThisIntent);
        DisplayNameTextView.setText(event.getArtist());
        performanceDisplayNameTextView.setText(event.getPerformance());
        dateTimeTextView.setText(event.getStartTime().getTime().toString());
        venueDisplayNameTextView.setText(event.getVenueName());
        venueStreetTextView.setText(event.getVenueStreet());
        venueCityTextView.setText(event.getVenueCity());
    }

    private void addToCalendarButtonClicked() {
        if (isPermissionReadCalendarEnabled() && isPermissionWriteCalendarEnabled()) {
            toastMessage("Adding to calendar ... Loading");
            new AddToCalendarTask().execute();
        } else {
            ActivityCompat.requestPermissions(
                    EventDetailActivity.this,
                    new String[]{"android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR"},
                    MY_PERMISSION_REQUEST_READ_CALENDAR);
        }
    }

    private boolean isPermissionReadCalendarEnabled() {
        return ActivityCompat.checkSelfPermission(EventDetailActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isPermissionWriteCalendarEnabled() {
        return ActivityCompat.checkSelfPermission(EventDetailActivity.this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED;
    }

    private long getCalendarId() {
        if (!doesLocalCalendarExists()) createCalendar();

        String[] projection = new String[]{Calendars._ID};
        String selection = Calendars.ACCOUNT_NAME + " = ? AND " + Calendars.ACCOUNT_TYPE + " = ? ";
        String[] selArgs = new String[]{ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL};
        @SuppressLint("MissingPermission")
        Cursor cursor = getContentResolver().query(
                Calendars.CONTENT_URI,
                projection,
                selection,
                selArgs,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    private boolean doesLocalCalendarExists() {
        String[] projection = new String[]{Calendars._ID};
        String selection = Calendars.ACCOUNT_NAME + " = ? AND " + Calendars.ACCOUNT_TYPE + " = ? ";
        String[] selArgs = new String[]{ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL};
        @SuppressLint("MissingPermission")
        Cursor cursor = getContentResolver().query(
                Calendars.CONTENT_URI,
                projection,
                selection,
                selArgs,
                null);

        return cursor != null && cursor.moveToFirst();
    }

    private void createCalendar() {
        //Create local calendar
        ContentValues values = new ContentValues();
        values.put(Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(Calendars.NAME, CALENDAR_NAME);
        values.put(Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_DISPLAY_NAME);
        values.put(Calendars.CALENDAR_COLOR, 0xffff0000);
        values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        values.put(Calendars.OWNER_ACCOUNT, OWNER_NAME);
        values.put(Calendars.CALENDAR_TIME_ZONE, "Europe/Berlin");
        values.put(Calendars.SYNC_EVENTS, 1);

        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        builder.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
        getContentResolver().insert(builder.build(), values);
    }

    private void createCalendarEvent() {
        long calendarId = getCalendarId();
        if (calendarId == -1) {
            // no calendar account; react meaningfully
            Log.w(TAG, "No calendar account");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, event.getStartTime().getTimeInMillis());
        Calendar endDateTime = event.getStartTime();
        endDateTime.set(Calendar.HOUR_OF_DAY, 22);
        values.put(Events.DTEND, endDateTime.getTimeInMillis());
        values.put(Events.TITLE, event.getPerformance());
        values.put(Events.EVENT_LOCATION, event.getVenueName());
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.EVENT_TIMEZONE, "Europe/Berlin");
        values.put(Events.DESCRIPTION, event.getArtist() + ": " + event.getPerformance());

        @SuppressLint("MissingPermission") Uri uri = getContentResolver().insert(Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toastMessage("Try again");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        if (selectedId == R.id.action_share) {
            Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText("I'm going to " + event.getPerformance())
                    .getIntent();
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            startActivity(shareIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getEventDetails(Intent intent) {
        event = new Event();
        event.setArtist(intent.getStringExtra("artist"));
        event.setPerformance(intent.getStringExtra("performance"));
        event.setVenueName(intent.getStringExtra("venueName"));
        event.setVenueStreet("Scheijnpoortweg 119");
        event.setVenueCity(intent.getStringExtra("venueCity"));

        Calendar date = Calendar.getInstance();
        date.set(
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                20,
                0,
                0);
        event.setStartTime(date);
    }

    private void toastMessage(String message) {
        Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public class AddToCalendarTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            long calendarId = getCalendarId();
            if (calendarId > -1) {
                createCalendarEvent();
                return "Event added!";
            } else {
                return "Something went wrong";
            }
        }

        @Override
        protected void onPostExecute(String message) {
            toastMessage(message);
            super.onPostExecute(message);
        }

    }
}
