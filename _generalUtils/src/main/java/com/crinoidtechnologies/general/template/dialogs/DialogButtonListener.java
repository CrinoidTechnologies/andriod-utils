package com.crinoidtechnologies.general.template.dialogs;

import android.content.DialogInterface;

/**
 * Created by ${Vivek} on 11/4/2015 for MessageSaver.Be careful
 */

public interface DialogButtonListener {
    void onPositiveButtonClick(DialogInterface dialog, int which);

    void onNegativeButtonClick(DialogInterface dialog, int which);

    void onNeutralButtonClick(DialogInterface dialog, int which);
}


