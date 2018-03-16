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

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.cookies.Entities.Event;
import com.example.android.cookies.EventDetailActivity;
import com.example.android.cookies.R;
import com.example.android.cookies.sync.EventIntentService;
import com.example.android.cookies.sync.EventTasks;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int EVENT_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int EVENT_PENDING_INTENT_ID = 3417;
    /**
     * This notification channel id is used to link notifications to this channel
     */
    private static final String EVENT_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int ACTION_CALENDAR_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void eventAddedToCalendarNotification(Context context, Event event, long calendarEventId) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    EVENT_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, EVENT_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccentBlueLagoon))
                .setSmallIcon(R.drawable.ic_concert)
                //.setLargeIcon(R.drawable.ic_concert)
                .setContentTitle(context.getString(R.string.event_notification_title))
                .setContentText(event.getPerformance())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        event.getPerformance()))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context, event))
                .addAction(goToEventInCalendarAction(context, calendarEventId))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(EVENT_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent intent = new Intent(context, EventIntentService.class);
        intent.setAction(EventTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getService(context, ACTION_IGNORE_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(android.R.drawable.ic_notification_clear_all, context.getString(R.string.action_cancel_title), pendingIntent);
    }

    private static NotificationCompat.Action goToEventInCalendarAction(Context context, long calendarEventId) {
        Intent intent = new Intent(context, EventIntentService.class);
        intent.setAction(EventTasks.ACTION_GO_TO_EVENT_IN_CALENDAR);
        intent.putExtra("calendarEventId", calendarEventId);
//        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
//        builder.appendPath("time");
//        ContentUris.appendId(builder, Calendar.getInstance().getTimeInMillis());
//        Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
//        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, calendarEventId);
//        Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
        PendingIntent pendingIntent = PendingIntent.getService(context, ACTION_CALENDAR_PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(android.R.drawable.ic_menu_my_calendar, "See calendar", pendingIntent);
    }

    private static PendingIntent contentIntent(Context context, Event event) {
        Intent startActivityIntent = new Intent(context, EventDetailActivity.class);
        startActivityIntent.putExtra("artist", event.getArtist());
        startActivityIntent.putExtra("performance", event.getPerformance());
        startActivityIntent.putExtra("venueName", event.getVenueName());
        startActivityIntent.putExtra("venueCity", event.getVenueCity());

        return PendingIntent.getActivity(
                context,
                EVENT_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
