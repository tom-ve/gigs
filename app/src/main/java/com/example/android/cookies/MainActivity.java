package com.example.android.cookies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements
        EventAdapter.AdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;

    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private EditText mLocationInput;
    private final String[] eventData = {"test1", "test2", "test3", "test4", "test5", "test6",
            "test7", "test8", "test9", "test10", "test11", "test12", "test13", "test14","test15",
            "test16", "test17", "test18", "test19", "test20", "test21"};

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

    }

    /**
     * This method is for responding to clicks from our list.
     *
     * @param event String describing a particular event.
     */
    @Override
    public void onClick(String event) {
        Intent intentToStartDetailActivity = new Intent(this, EventDetailActivity.class);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, event);
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
            mEventAdapter.setEventData(eventData);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make the View for the event data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showEventDataView() {
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