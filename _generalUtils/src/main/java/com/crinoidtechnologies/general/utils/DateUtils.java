package com.crinoidtechnologies.general.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    public static SimpleDateFormat DATE_TIME_FORMAT_1 = new SimpleDateFormat("MMM-dd-yy HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat DATE_TIME_FORMAT_2 = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.ENGLISH);
    public static SimpleDateFormat DATE_TIME_FORMAT_3 = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);
    public static SimpleDateFormat DATE_TIME_FORMAT_4 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

    public static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static SimpleDateFormat TIME_FORMAT_24 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat defaultDateFormat = DATE_FORMAT_1;
    public static SimpleDateFormat defaultTimeFormat = TIME_FORMAT_24;
    public static SimpleDateFormat defaultTimeDateFormat = DATE_TIME_FORMAT_1;

    public static long MILLIS_VALUE = 1000;
    public static String STRING_TODAY = "Today";
    public static String STRING_YESTERDAY = "Yesterday";

    public static String longToMessageDate(long dateLong) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        String timeString = new SimpleDateFormat("hh:mm").format(calendar.getTime());
        return timeString;
    }

    public static String longToMessageListHeaderDate(long dateLong) {
        String timeString;

        Locale locale = new Locale("en");

        Calendar calendar = Calendar.getInstance();
        int currentDate = calendar.getTime().getDate();

        calendar.setTimeInMillis(dateLong * MILLIS_VALUE);
        int inputDate = calendar.getTime().getDate();

        if (inputDate == currentDate) {
            timeString = STRING_TODAY;
        } else if (inputDate == currentDate - 1) {
            timeString = STRING_YESTERDAY;
        } else {
            Date time = calendar.getTime();
            timeString = new SimpleDateFormat("EEEE", locale).format(time) + ", " + inputDate + " " +
                    new SimpleDateFormat("MMMM", locale).format(time);
        }

        return timeString;
    }

    public static String dateToString(Date date) {
        return dateToString(defaultDateFormat, date);
    }

    public static String dateToString(SimpleDateFormat format, Date date) {
        return format.format(date);
    }

    public static String getCurrentDateInString(SimpleDateFormat dateFormat) {
        //   Calendar c = Calendar.getInstance();
        // System.out.println("Current time => " + c.getTime());
        return dateFormat.format(new Date());
    }

    public static String getCurrentDateInString() {
        return getCurrentDateInString(defaultDateFormat);
    }

    public static String getCurrentDatePlusInString(int days) {
        return defaultDateFormat.format(getCurrentDatePlus(days));
    }

    public static Date getCurrentDatePlus(int days) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static Date stringToDate(String date) {
        return stringToDate(defaultDateFormat, date);
    }

    public static Date stringToDate(SimpleDateFormat dateFormat, String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static int dateDifferenceInDays(Date one, Date two) {
        return (int) TimeUnit.DAYS.convert(one.getTime() - two.getTime(), TimeUnit.MILLISECONDS);
    }

    public static Date getCurrentDatePlus(int days, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public static String getDateInOtherFormat(String data, SimpleDateFormat orignal, SimpleDateFormat newOne) {
        Date date = stringToDate(orignal, data);
        return dateToString(newOne, date);

    }
}