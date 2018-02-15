package com.example.android.cookies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.cookies.Entities.Event;
import com.example.android.cookies.Entities.EventType;
import com.example.android.cookies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements
        EventAdapter.AdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private RequestQueue mRequestQueue;

    private TextView mWelcomeText;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private EditText mLocationInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Setting up the recyclerView
        mRecyclerView = findViewById(R.id.recyclerview_event);
        mEventAdapter = new EventAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mEventAdapter);

        //Setting up the elements
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLocationInput = findViewById(R.id.et_location_input);
        mWelcomeText = findViewById(R.id.tv_welcome_text);

        //Setting up Volley
        mRequestQueue = Volley.newRequestQueue(this);

        //Loading preferences
        setupSharedPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param event String describing a particular event.
     */
    @Override
    public void onClick(Event event) {
        Intent intentToStartDetailActivity = new Intent(this, EventDetailActivity.class);
        intentToStartDetailActivity.putExtra("artist", event.getArtist());
        intentToStartDetailActivity.putExtra("performance", event.getPerformance());
        intentToStartDetailActivity.putExtra("venueName", event.getVenueName());
        intentToStartDetailActivity.putExtra("venueCity", event.getVenueCity());
        startActivity(intentToStartDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        if (selectedId == R.id.action_search) {
            String location = mLocationInput.getText().toString();
            showEventDataView();
            if (!location.isEmpty()) {
                getMetroAreaId(location);
                mLocationInput.clearFocus();
            } else {
                mErrorMessageDisplay.setText(R.string.error_empty_location);
                showErrorMessage();
            }
        } else if (selectedId == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_name_key))) {
            setWelcomeText(sharedPreferences);
        }
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setWelcomeText(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setWelcomeText(SharedPreferences sharedPreferences) {
        String name = sharedPreferences.getString(
                getString(R.string.pref_name_key),
                getString(R.string.pref_name_default));
        mWelcomeText.setText(getString(R.string.welcome_message, name));
    }

    private void getMetroAreaId(String location) {
        showLoading();

        // Request a string response from the provided URL.
        String locationsQueryUrlString = NetworkUtils.getLocationsQueryUrlString(location);
        StringRequest locationsQueryRequest = new StringRequest(Request.Method.GET, locationsQueryUrlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject resultPage = new JSONObject(response).getJSONObject("resultsPage");
                            String status = resultPage.getString("status");
                            Log.i(TAG, "Status getMetroAreaId:  " + status);
                            if (resultPage.has("results") && !resultPage.isNull("results")) {
                                JSONObject metroArea = resultPage.getJSONObject("results")
                                        .getJSONArray("location")
                                        .getJSONObject(0)
                                        .getJSONObject("metroArea");
                                String metroAreaName = metroArea.getString("displayName");
                                mLocationInput.setText(metroAreaName);
                                mLocationInput.clearFocus();
                                Log.i(TAG, "Metro area name:  " + metroAreaName);
                                int metroAreaId = metroArea.getInt("id");
                                Log.i(TAG, "Metro area id:  " + metroAreaId);
                                getEventsForMetroArea(metroAreaId);
                            } else {
                                mErrorMessageDisplay.setText(R.string.error_no_metro_area);
                            }
                        } catch (JSONException e) {
                            mErrorMessageDisplay.setText(R.string.error_no_metro_area);
                            showErrorMessage();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mErrorMessageDisplay.setText(R.string.error_no_metro_area);
                        showErrorMessage();
                    }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(locationsQueryRequest);
    }

    private void getEventsForMetroArea(int metroAreaId) {

        // Request a string response from the provided URL.
        String metroAreaCalendarUrlString = NetworkUtils.getMetroAreaCalendarUrlString(metroAreaId);
        StringRequest metroAreaCalendarRequest = new StringRequest(Request.Method.GET, metroAreaCalendarUrlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Response getEventsForMetroArea: " + response);
                        List<Event> metroAreaEvents = new ArrayList<>();
                        try {
                            JSONObject resultPage = new JSONObject(response).getJSONObject("resultsPage");
                            String status = resultPage.getString("status");
                            Log.i(TAG, "Status getEventsForMetroAreaId:  " + status);
                            if ("ok".equals(status) && resultPage.has("results") && !resultPage.isNull("results")) {
                                JSONArray events = resultPage.getJSONObject("results")
                                        .getJSONArray("event");
                                metroAreaEvents = getEventsFromJsonArray(events);
                            } else {
                                mErrorMessageDisplay.setText(R.string.error_no_events_founds);
                                showErrorMessage();
                            }
                        } catch (JSONException e) {
                            mErrorMessageDisplay.setText(R.string.error_no_events_founds);
                            showErrorMessage();
                        }

                        showEventDataView();
                        mEventAdapter.setEventData(metroAreaEvents);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mErrorMessageDisplay.setText(R.string.error_no_events_founds);
                        showErrorMessage();
                    }
        });
        // Add the request to the RequestQueue.
        mRequestQueue.add(metroAreaCalendarRequest);
    }

    private List<Event> getEventsFromJsonArray(JSONArray jsonArray) {
        List<Event> metroAreaEvents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            long id = 0;
            String artist = "Not known";
            String performance = "Not known";
            EventType type = EventType.CONCERT;
            String venueName = "Not known";
            String venueCity = "Not known";
            try {
                JSONObject event = jsonArray.getJSONObject(i);
                id = event.getLong("id");
                JSONArray performances = event.getJSONArray("performance");
                for (int iPerformance = 0; iPerformance < performances.length(); iPerformance++) {
                    if ("headline".equals(performances.getJSONObject(iPerformance).getString("billing"))) {
                        artist = performances.getJSONObject(iPerformance).getString("displayName");
                        break;
                    }
                }
                performance = event.getString("displayName");
                if ("Concert".equals(event.getString("type"))) {
                    type = EventType.CONCERT;
                } else {
                    type = EventType.FESTIVAL;
                }
                JSONObject venue = event.getJSONObject("venue");
                venueName = venue.getString("displayName");
                venueCity = venue.getJSONObject("metroArea").getString("displayName");
                metroAreaEvents.add(new Event(id, artist, performance, type, venueName, venueCity));
            } catch (JSONException e) {
                e.printStackTrace();
                mErrorMessageDisplay.setText(R.string.error_no_events_founds);
                showErrorMessage();
            }
        }
        return metroAreaEvents;
    }

    /**
     * This method will make the View for the event data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showEventDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the event
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the event View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}