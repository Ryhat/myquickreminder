package com.chrisfeb.myquicktask;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class CalendarUtils {
    public static final int REQUEST_CALENDAR = 123;

    private static String CALENDAR_URL = "content://com.android.calendar/calendars";
    private static String CALENDAR_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDAR_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "QuickTasks";
    private static String CALENDARS_ACCOUNT_NAME = "qt@qt.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.chrisfeb.qs";
    private static String CALENDARS_DISPLAY_NAME = "QuickTasks";

    static void fetchPermission(int requestCode, Context context) {
        int checkSelfPermission;
        try {
            checkSelfPermission = ActivityCompat.checkSelfPermission((Activity) context, Manifest.permission.WRITE_CALENDAR);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return;
        }

        // 如果有授权，走正常插入日历逻辑
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "获取权限成功，可以正常操作噢~", Toast.LENGTH_SHORT).show(); // 该方法的实现在文章的后面
        } else {
            // 如果没有授权，就请求用户授权
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_CALENDAR,
                    Manifest.permission.READ_CALENDAR}, requestCode);
        }
    }

    private static int checkAndAddCalendarAccount(Context context) {
        try (Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDAR_URL),
                null, null, null, null)) {
            if (userCursor == null) { // 查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { // 存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        }
    }

    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDAR_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                        CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        return result == null ? -1 : ContentUris.parseId(result);
    }

    public static boolean insertCalendarEvent(Context context,
                                              String title,
                                              String description,
                                              String location,
                                              int advanceTime,
                                              long beginTimeMillis,
                                              long endTimeMillis) {

        if (context == null || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            return false;
        }

        int calId = checkAndAddCalendarAccount(context); // 获取日历账户的id
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            return false;
        }

        // 如果起始时间为零，使用当前时间
        if (beginTimeMillis == 0) {
            Calendar beginCalendar = Calendar.getInstance();
            beginTimeMillis = beginCalendar.getTimeInMillis();
        }
        // 如果结束时间为零，使用起始时间+30分钟
        if (endTimeMillis == 0) {
            endTimeMillis = beginTimeMillis + 30 * 60 * 1000;
        }

        try {
            /* 插入日程 */
            ContentValues eventValues = new ContentValues();
            eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis);
            eventValues.put(CalendarContract.Events.DTEND, endTimeMillis);
            eventValues.put(CalendarContract.Events.TITLE, title);
            eventValues.put(CalendarContract.Events.DESCRIPTION, description);
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            if (location.equals("")) {
                eventValues.put(CalendarContract.Events.EVENT_LOCATION, "");
            } else {
                eventValues.put(CalendarContract.Events.EVENT_LOCATION, location);
            }

            TimeZone tz = TimeZone.getDefault(); // 获取默认时区
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

            Uri eUri = context.getContentResolver().insert(Uri.parse(CALENDAR_EVENT_URL), eventValues);
            assert eUri != null;
            long eventId = ContentUris.parseId(eUri);
            if (eventId == 0) { // 插入失败
                return false;
            }

            /* 插入提醒 - 依赖插入日程成功 */
            ContentValues reminderValues = new ContentValues();
            // uri.getLastPathSegment();
            reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminderValues.put(CalendarContract.Reminders.MINUTES, advanceTime); // 提前10分钟提醒
            reminderValues.put(CalendarContract.Reminders.METHOD,
                    CalendarContract.Reminders.METHOD_ALERT);
            Uri rUri = context.getContentResolver().insert(Uri.parse(CALENDAR_REMINDER_URL),
                    reminderValues);
            if (rUri == null || ContentUris.parseId(rUri) == 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void deleteCalendarEvent(Context context, String title) {
        if (context == null) {
            return;
        }
        try (Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDAR_EVENT_URL),
                null, null, null, null)) {
            if (eventCursor == null) { // 查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0) {
                // 遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor
                                .getColumnIndex(CalendarContract.Calendars._ID)); // 取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) { // 事件删除失败
                            return;
                        }
                    }
                }
            }
        }
    }

}
