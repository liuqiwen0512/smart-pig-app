package com.wuzi.pig.utils.tools;

import android.os.SystemClock;

import com.wuzi.pig.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class TimeUtils {

    private static long sEventLastTime = 0;

    public static String stringForTime(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }

    }

    public static boolean havePast200msec() {
        boolean past = SystemClock.uptimeMillis() - sEventLastTime > 200;
        sEventLastTime = SystemClock.uptimeMillis();
        return past;
    }

    public static long getTimeInMillis(String time, long defaut) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timestamp = defaut;
        try {
            Date date = dateFormat.parse(time);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static String getFormatTime(String dateStr, String resFormat, String destFormat) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        try {
            final DateFormat resDateFormat = new SimpleDateFormat(resFormat);
            final DateFormat destDateFormat = new SimpleDateFormat(destFormat);
            Date date = resDateFormat.parse(dateStr);
            dateStr = destDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String getFormatTime(String dateStr, String format) {
        try {
            final DateFormat dateFormat = new SimpleDateFormat(format);
            Date date = dateFormat.parse(dateStr);
            dateStr = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String getFormatTime(long msec, String format) {
        final DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(msec));
    }

    public static String getFormatTime(String dateStr) {
        return getFormatTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getFormatTime(long msec) {
        return getFormatTime(msec, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimeInMillis(String time) {
        return getTimeInMillis(time, Calendar.getInstance().getTimeInMillis());
    }

    public static Calendar getCalendar(String formatDate, String resFormat) {
        try {
            final DateFormat resDateFormat = new SimpleDateFormat(resFormat);
            Date date = resDateFormat.parse(formatDate);
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCalendar(String formatDate) {
        return getCalendar(formatDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimeInMillisOfDate(long millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        return getTimeInMillisOfDate(calendar);
    }

    public static long getTimeInMillisOfDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static long getTimeInMillisOfDate() {
        Calendar calendar = Calendar.getInstance();
        return getTimeInMillisOfDate(calendar);
    }
}
