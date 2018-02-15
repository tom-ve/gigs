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
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, event.getArtist());
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
                        //TODO: get metro area id from JSON response
                        try {
                            JSONObject metroAreaResponse = new JSONObject(response).getJSONObject("resultsPage");
                            String metroAreaId = metroAreaResponse.getString("status");
                            Log.i(TAG, "This is the metroAreaID:  " + metroAreaId);
                            getEventsForMetroArea("59028");
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

    private void getEventsForMetroArea(String metroAreaId) {

        // Request a string response from the provided URL.
        String metroAreaCalendarUrlString = NetworkUtils.getMetroAreaCalendarUrlString(metroAreaId);
        StringRequest metroAreaCalendarRequest = new StringRequest(Request.Method.GET, metroAreaCalendarUrlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO: get events from JSON response
                        Log.i(TAG, response);

                        List<Event> metroAreaEvents = new ArrayList<>();
                        metroAreaEvents.add(new Event("Clouseau", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("Eefje de Visser", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("Ed Sheeran", "test", EventType.FESTIVAL));
                        metroAreaEvents.add(new Event("Justin Timberlake", "test", EventType.FESTIVAL));
                        metroAreaEvents.add(new Event("U2", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("ACDC", "test", EventType.FESTIVAL));
                        metroAreaEvents.add(new Event("PINK FLOYD", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("W817", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("Plop", "test", EventType.CONCERT));
                        metroAreaEvents.add(new Event("Tupac", "test", EventType.CONCERT));

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