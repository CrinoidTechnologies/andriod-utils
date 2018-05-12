package com.crinoidtechnologies.general.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ${Vivek} on 11/23/2015 for MessageSaver.Be careful
 */
public class NotificationHelper {

    private static final long TIME_UNIT = 60 * 60 * 1000; // I.E HOURS

    private final NotificationManager notificationManager;
    public int largeIconId;
    public int smallIconId;
    Context context;
    private List<Integer> notificationsTimes;// in hours
    private List<String> notificationsContents;

    public NotificationHelper(Context context, List<Integer> notificationsTimes, List<String> notificationsContents) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        setData(notificationsTimes, notificationsContents);
    }

    public static void cancelAllNotification(Context context) {
        ((NotificationManager) context.getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    }

    public static void showNotification(Context context, Class<?> clas, String title, String description) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, clas), 0);
        Resources r = context.getResources();
        Notification notification = new Notification.Builder(context)
                .setTicker(title)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static void showNotification(Context context, Intent intent, String title, String des, Bitmap largeLogo) {
        showNotification(context, intent, title, des, largeLogo, false);
    }

    public static void showNotification(Context context, Intent intent, String title, String des, Bitmap largeLogo, boolean isSound) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification.Builder notificationBuilder = new Notification.Builder(context)
                .setTicker(title)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle(title)
                .setContentText(des)
                .setContentIntent(pi)
                .setAutoCancel(true);

        if (isSound) {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder.setSound(alarmSound);
        }
        if (largeLogo != null) {
            notificationBuilder.setLargeIcon(largeLogo);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public void setData(List<Integer> notificationsTimes, List<String> notificationsContents) {
        this.notificationsTimes = notificationsTimes;
        this.notificationsContents = notificationsContents;
    }

    public void queueAllNotification(boolean isShuffle) {
        ArrayList<Integer> point = new ArrayList<>(notificationsTimes.size());
        for (int i = 0; i < notificationsTimes.size(); i++) {
            point.add(i);
        }
        ArrayList<Integer> notificationOrder = new ArrayList<>(notificationsTimes.size());
        for (int i = 0; i < notificationsTimes.size(); i++) {
            int j = (int) (Math.random() * point.size());
            notificationOrder.add(isShuffle ? point.get(j) : i);
            point.remove(j);
        }

        for (int i = 0; i < notificationsTimes.size(); i++) {
            scheduleNotification(i, getNotification("", notificationOrder.get(i), i), notificationsTimes.get(i));
        }

    }

    //// TODO: 2/9/2016 Note *************************-------register NotificationPublisher receiver in manifest
    private void scheduleNotification(int i, Notification notification, int delay) {

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // You need this if starting
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, i);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay * TIME_UNIT;
        Log.d("TAG", "scheduleNotification() called with: " + "i = [" + i + "], notification = [" + pendingIntent + "], delay = [" + delay + "]");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, delay);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(i+20,notification);

    }

    private Notification getNotification(String title, int i, int order) {

        Intent notificationIntent = new Intent();
        PendingIntent pending = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(smallIconId)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIconId))
                .setContentTitle(title)
                .setContentText(notificationsContents.get(i))
                .setWhen(System.currentTimeMillis() + TIME_UNIT * notificationsTimes.get(order))
                .setContentIntent(pending)
                .build();

//        Notification notify =
//                new NotificationCompat.Builder(context)
//                        .setSmallIcon(R.drawable.ic_close_white_24dp)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!").build();

        Log.i("TAG", "getNotification: " + notificationsContents.get(i));
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        return notify;
    }

}
