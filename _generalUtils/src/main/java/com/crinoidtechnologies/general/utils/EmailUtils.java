package com.crinoidtechnologies.general.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import com.crinoidtechnologies.general.R;

public class EmailUtils {

    public static String SUPPORT_EMAIL = "@GMAIL.COM";

    public static boolean sendEmail(Context context, String subject, String body, String... email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_mail_via)));
        return true;
//        }
//        Intent intentEmail = new Intent(Intent.ACTION_SEND);
//        intentEmail.putExtra(Intent.EXTRA_EMAIL, email);
//        intentEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intentEmail.putExtra(Intent.EXTRA_TEXT, body);
//        intentEmail.setType("message/rfc822");
//        intentEmail.setFlags(intentEmail.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK);
//

//        return false;
    }

    public static void sendInviteEmail(Context context, String[] selectedFriends) {
        Resources resources = context.getResources();
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.putExtra(Intent.EXTRA_EMAIL, selectedFriends);
        // intentEmail.putExtra(Intent.EXTRA_SUBJECT, resources.getText(R.string.inf_subject_of_invitation));
        // intentEmail.putExtra(Intent.EXTRA_TEXT, resources.getText(R.string.inf_body_of_invitation));
        //intentEmail.setType(ConstsCore.TYPE_OF_EMAIL);
        // context.startActivity(Intent.createChooser(intentEmail, resources.getText(R.string.inf_choose_email_provider)));
    }

    public static void sendFeedbackEmail(Context context, String feedbackType) {
        sendEmail(context, feedbackType, (DeviceInfoUtils.getDeviseInfoForFeedback(context)).toString(), SUPPORT_EMAIL);
    }

    public static void sendSupportEmail(Context context, String feedbackType, String userInfo) {
        Resources resources = context.getResources();
        Intent intentEmail = new Intent(Intent.ACTION_SEND);
        intentEmail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{SUPPORT_EMAIL});
        intentEmail.putExtra(Intent.EXTRA_SUBJECT, feedbackType);
        intentEmail.setType("message/rfc822");
        context.startActivity(Intent.createChooser(intentEmail, "Send Mail via"));
    }

//    public static List<InviteFriend> getContactsWithEmail(Context context) {
//        List<InviteFriend> friendsContactsList = new ArrayList<InviteFriend>();
//        Uri uri = null;
//
//        ContentResolver contentResolver = context.getContentResolver();
//
//        String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.PHOTO_ID, ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
//
//        String order = "CASE WHEN " + ContactsContract.Contacts.DISPLAY_NAME + " NOT LIKE '%@%' THEN 1 ELSE 2 END, " + ContactsContract.CommonDataKinds.Phone.CONTACT_ID + ", " + ContactsContract.Contacts.DISPLAY_NAME + ", " + ContactsContract.CommonDataKinds.Email.DATA + " COLLATE NOCASE";
//
//        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
//
//        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION,
//                filter, null, order);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            String id;
//            String name;
//            String email;
//            do {
//                name = cursor.getString(cursor.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                email = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                id = cursor.getString(cursor.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                if (ContactsContract.Contacts.CONTENT_URI != null) {
//                    uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(
//                            id));
//                    uri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//                }
//                friendsContactsList.add(new InviteFriend(email, name, null, InviteFriend.VIA_CONTACTS_TYPE,
//                        uri, false));
//            } while (cursor.moveToNext());
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        return friendsContactsList;
//    }
}