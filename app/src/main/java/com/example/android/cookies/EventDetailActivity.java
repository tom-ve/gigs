package com.example.android.cookies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

public class EventDetailActivity extends AppCompatActivity {

    private Map<String, String> eventDetails;
    private TextView DisplayNameTextView;
    private TextView performanceDisplayNameTextView;
    private TextView dateTimeTextView;
    private TextView venueDisplayNameTextView;
    private TextView venueStreetTextView;
    private TextView venueCityTextView;
    private Button addToCalendarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        DisplayNameTextView = findViewById(R.id.tv_display_name);
        performanceDisplayNameTextView = findViewById(R.id.tv_performance_display_name);
        dateTimeTextView = findViewById(R.id.tv_datetime);
        venueDisplayNameTextView = findViewById(R.id.tv_venue_display_name);
        venueStreetTextView = findViewById(R.id.tv_venue_street);
        venueCityTextView = findViewById(R.id.tv_venue_city_display_name);

        addToCalendarButton = findViewById(R.id.button_add_to_calendar);
        addToCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EventDetailActivity.this, "ADD TO CALENDAR", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intentThatStartedThisIntent = getIntent();
        if (intentThatStartedThisIntent.hasExtra(Intent.EXTRA_TEXT)) {
            getEventDetails(intentThatStartedThisIntent.getStringExtra(Intent.EXTRA_TEXT));
            DisplayNameTextView.setText(eventDetails.get("displayName"));
            performanceDisplayNameTextView.setText(eventDetails.get("performanceDisplayName"));
            dateTimeTextView.setText(eventDetails.get("dateTime"));
            venueDisplayNameTextView.setText(eventDetails.get("venueDisplayName"));
            venueStreetTextView.setText(eventDetails.get("venueStreet"));
            venueCityTextView.setText(eventDetails.get("venueCityDisplayName"));
        }
        setTitle(eventDetails.get("displayName"));
    }

    private void getEventDetails(String eventId) {
        eventDetails = new TreeMap<>();
        eventDetails.put("displayName", "Imagine Dragons");
        eventDetails.put("performanceDisplayName", "Imagine Dragons for ever young");
        eventDetails.put("dateTime", "08/02/2018 20:00");
        eventDetails.put("venueDisplayName", "Sportpaleis");
        eventDetails.put("venueStreet", "Scheijnpoortweg 119");
        eventDetails.put("venueCityDisplayName", "Antwerp, Belgium");
    }
}
