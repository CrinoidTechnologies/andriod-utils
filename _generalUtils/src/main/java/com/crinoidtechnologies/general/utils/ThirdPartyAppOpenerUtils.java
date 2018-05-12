package com.crinoidtechnologies.general.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.crinoidtechnologies.general.template.BaseApplication;
import com.crinoidtechnologies.general.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ${Vivek} on 2/20/2016 for MissApp.Be careful
 */
public class ThirdPartyAppOpenerUtils {

    public static void openRateUsOnPlayStore(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void openLink(Context context, String url) {
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            // context.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
        }
    }

    public static Intent getImageFromGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        return intent;
    }

    public static String getImagePathFromResult(Activity context, Uri uri) {
        // just some safety built in
        if (uri == null) {
            return "";
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public static String getImagePathFromResult(Activity context, Intent intent) {
        // just some safety built in
        if (intent == null) {
            return "";
        }
        return getImagePathFromResult(context, intent.getData());
    }

    public static void shareApp(Context context, String line) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String sAux = line;
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, "Share app via"));
        } catch (Exception e) {
            e.toString();
            Toast.makeText(context, R.string.share_app_exception, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shareReferral(Context context, String subject, String line) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            String sAux = line;
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            context.startActivity(Intent.createChooser(i, context.getString(R.string.invite_with)));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.share_app_exception, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openDialPadWithNumber(Context context, String customerCareNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + customerCareNumber));
        context.startActivity(intent);
    }

    public static Intent getSmsToContactIntent(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", message);
        return intent;
    }

    public static long pushAppointmentsToCalender(Activity curActivity, String title, String description, String place, int status, long startDate, boolean needReminder, boolean needMailService) {
        /***************** Event: note(without alert) *******************/

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put("calendar_id", 1); // id, We need to choose from
        // our mobile for primary
        // its 1
        eventValues.put("title", title);
        eventValues.put("description", description);
        eventValues.put("eventLocation", place);

        long endDate = startDate + 1000 * 60 * 60; // For next 1hr

        eventValues.put("dtstart", startDate);
        eventValues.put("dtend", endDate);

        // values.put("allDay", 1); //If it is bithday alarm or such
        // kind (which should remind me for whole day) 0 for false, 1
        // for true
        eventValues.put("eventStatus", status); // This information is
        // sufficient for most
        // entries tentative (0),
        // confirmed (1) or canceled
        // (2):
        eventValues.put("eventTimezone", "UTC/GMT +2:00");
   /*Comment below visibility and transparency  column to avoid java.lang.IllegalArgumentException column visibility is invalid error */

    /*eventValues.put("visibility", 3); // visibility to default (0),
                                        // confidential (1), private
                                        // (2), or public (3):
    eventValues.put("transparency", 0); // You can control whether
                                        // an event consumes time
                                        // opaque (0) or transparent
                                        // (1).
      */
        eventValues.put("hasAlarm", 1); // 0 for false, 1 for true

        Uri eventUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        if (needReminder) {
            /***************** Event: Reminder(with alert) Adding reminder to event *******************/

            String reminderUriString = "content://com.android.calendar/reminders";

            ContentValues reminderValues = new ContentValues();

            reminderValues.put("event_id", eventID);
            reminderValues.put("minutes", 5); // Default value of the
            // system. Minutes is a
            // integer
            reminderValues.put("method", 1); // Alert Methods: Default(0),
            // Alert(1), Email(2),
            // SMS(3)

            Uri reminderUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
        }

        /***************** Event: Meeting(without alert) Adding Attendies to the meeting *******************/

        if (needMailService) {
            String attendeuesesUriString = "content://com.android.calendar/attendees";

            /********
             * To add multiple attendees need to insert ContentValues multiple
             * times
             ***********/
            ContentValues attendeesValues = new ContentValues();

            attendeesValues.put("event_id", eventID);
            attendeesValues.put("attendeeName", "xxxxx"); // Attendees name
            attendeesValues.put("attendeeEmail", "yyyy@gmail.com");// Attendee
            // E
            // mail
            // id
            attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),
            // Relationship_None(0),
            // Organizer(2),
            // Performer(3),
            // Speaker(4)
            attendeesValues.put("attendeeType", 0); // None(0), Optional(1),
            // Required(2), Resource(3)
            attendeesValues.put("attendeeStatus", 0); // NOne(0), Accepted(1),
            // Decline(2),
            // Invited(3),
            // Tentative(4)

            Uri attendeuesesUri = curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeuesesUriString), attendeesValues);
        }

        return eventID;

    }

    public static Intent getLocationOnMapIntent(String latitude, String longitude, String label) {
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        return Intent.createChooser(intent, "Open via");
    }

    public static Intent getPathBetweenLocationOnMapIntent(String sLatitude, String sLongitude, String dLatitude, String dLongitude) {
        String string = "http://maps.google.com/maps?saddr=" + sLatitude + "," + sLongitude + "&daddr=" + dLatitude + "," + dLongitude;
        Uri uri = Uri.parse(string);
        //  Uri uri = Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        return Intent.createChooser(intent, "Open via");
    }

    public static Intent getPathBetweenLocationOnMapIntent(double sLatitude, double sLongitude, double dLatitude, double dLongitude) {
        String string = "http://maps.google.com/maps?saddr=" + sLatitude + "," + sLongitude + "&daddr=" + dLatitude + "," + dLongitude;
        Uri uri = Uri.parse(string);
        //  Uri uri = Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        return Intent.createChooser(intent, "Open via");
    }

    public static void shareImageWithText(Context context, ImageView view, String text, String subject) {
        File root = Environment.getExternalStorageDirectory();
        String fileDirectory = root.getAbsolutePath() + File.separator + BaseApplication.getAppContext().getString(R.string.app_name) + File.separator + "share";
        String filePath = root.getAbsolutePath() + File.separator + BaseApplication.getAppContext().getString(R.string.app_name) + File.separator + "share" + File.separator + "image.jpg";

        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        File cachePath = new File(filePath);
        File cacheDirectory = new File(fileDirectory);
        try {
            if (!cacheDirectory.exists()) {
                cacheDirectory.mkdirs();
            }
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        share.putExtra(Intent.EXTRA_TEXT, text);
        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(share, "Share via"));

    }

    public static void showDialPad(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }
}
