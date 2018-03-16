package com.example.android.cookies.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class EventIntentService extends IntentService {

    public EventIntentService() {
        super("EventIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        long calendarEventId = 0;
        if (intent.hasExtra("calendarEventId")) {
            calendarEventId = intent.getLongExtra("calendarEventId", 0);
        }
        EventTasks.executeTask(this, action, calendarEventId);
    }
}