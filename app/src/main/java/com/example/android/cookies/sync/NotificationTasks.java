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
package com.example.android.cookies.sync;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import com.example.android.cookies.utils.NotificationUtils;

public class NotificationTasks {

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