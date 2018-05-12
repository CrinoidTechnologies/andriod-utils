package com.crinoidtechnologies.general.template.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.crinoidtechnologies.general.R;

/**
 * Created by ${Vivek} on 11/2/2015 for MessageSaver.Be careful
 */
public class DialogBuilderUtils {

    public static final int MultipleChoice_Multiplier = 10000;

    /***
     * @param context
     * @param listener
     * @param title
     * @param message  0 if no message
     * @param posTitle positive Button Title
     * @param negTitle negative button Title
     * @return dialog with two button
     */
    public static Dialog getDialog(Context context, final DialogButtonListener listener, int title, int message, int posTitle, int negTitle) {

        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, posTitle, negTitle);
        return builder.create();
    }

    public static AlertDialog.Builder getDialogBuilder(Context context, final DialogButtonListener listener, int title, int message, int posTitle, int negTitle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(posTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });

        if (title > 0) {
            builder.setTitle(title);
        }

        if (negTitle > 0) {
            builder.setNegativeButton(negTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onNegativeButtonClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }
        if (message > 0) {
            builder.setMessage(message);
        }
        return builder;
    }

    public static Dialog getDialog(Context context, final DialogButtonListener listener, String title, String message, int posTitle, int negTitle) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(posTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });

        if (negTitle > 0) {
            builder.setNegativeButton(negTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onNegativeButtonClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }
        if (!message.isEmpty()) {
            builder.setMessage(message);
        }
        return builder.create();
    }

    public static Dialog getCustomViewDialog(Context context, View view, int title, int message, int posTitle, int negTitle, final DialogButtonListener listener) {

        AlertDialog.Builder builder = getCustomViewDialogBuilder(context, view, title, message, posTitle, negTitle, listener);
        return builder.create();

    }

    public static AlertDialog.Builder getCustomViewDialogBuilder(Context context, View view, int title, int message, int posTitle, int negTitle, final DialogButtonListener listener) {

        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, posTitle, negTitle);

        if (view != null) {
            builder.setView(view);
        }

        return builder;

    }

    public static Dialog getSingleItemListDialog(Context context, int title, int message, String[] items, final DialogButtonListener listener) {
        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, R.string.ok, R.string.cancel);
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNeutralButtonClick(dialog, which);
            }
        });
        return builder.create();
    }

    public static Dialog getSingleItemListDialog(Context context, int title, int message, String[] items, int index, final DialogButtonListener listener) {
        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, R.string.ok, R.string.cancel);
        builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNeutralButtonClick(dialog, which);
            }
        });
        return builder.create();
    }

    public static Dialog getMultipleItemListDialog(Context context, int title, int message, String[] items, boolean[] checkedItems, boolean isCancel, final DialogButtonListener listener) {
        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, R.string.ok, isCancel ? R.string.cancel : 0);
        builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                listener.onNeutralButtonClick(dialog, (isChecked ? MultipleChoice_Multiplier : 0) + which);
            }
        });
        return builder.create();
    }

    public static Dialog getMultipleItemListDialog(Context context, int title, int message, String[] items, boolean[] checkedItems, boolean isCancel, final DialogButtonListener listener, int okString, int cancelString) {
        AlertDialog.Builder builder = getDialogBuilder(context, listener, title, message, okString, isCancel ? cancelString : 0);
        builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                listener.onNeutralButtonClick(dialog, (isChecked ? MultipleChoice_Multiplier : 0) + which);
            }
        });
        return builder.create();
    }

}


