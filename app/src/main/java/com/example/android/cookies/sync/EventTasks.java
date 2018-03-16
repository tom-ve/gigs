package com.example.android.cookies.sync;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import com.example.android.cookies.utils.NotificationUtils;

public class EventTasks {

    public static final String ACTION_GO_TO_EVENT_IN_CALENDAR = "go-to-event-in-calendar";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";

    public static void executeTask(Context context, String action, long calenderEventId) {
        if (ACTION_GO_TO_EVENT_IN_CALENDAR.equals(action)) {
            goToEventInCalendar(context, calenderEventId);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        }
    }

    private static void goToEventInCalendar(Context context, long calendarEventId) {
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calendarEventId);
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
        context.startActivity(intent);
        NotificationUtils.clearAllNotifications(context);
    }
}