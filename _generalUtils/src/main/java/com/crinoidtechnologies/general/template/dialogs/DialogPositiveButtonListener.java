package com.crinoidtechnologies.general.template.dialogs;

import android.content.DialogInterface;

/**
 * Created by ${Vivek} on 2/1/2016 for MissApp.Be careful
 */
public abstract class DialogPositiveButtonListener implements DialogButtonListener {

    abstract public void onPositiveButtonClick(DialogInterface dialog, int which);

    @Override
    public void onNegativeButtonClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onNeutralButtonClick(DialogInterface dialog, int which) {

    }

    public void onPositiveButtonClick(DialogInterface dialog, int which, String text) {
    }

}
