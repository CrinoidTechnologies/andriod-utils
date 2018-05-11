package com.crinoidtechnologies.general.template.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.crinoidtechnologies.general.R;

/**
 * Created by ${Vivek} on 8/31/2016 for MobileTrackerChat.Be careful
 */

public class SampleDialogs {

    static final CharSequence[] subjectList = new CharSequence[3];

    public static Dialog getDeleteMessageDialog(Context context, String title, final DialogButtonListener listener, boolean isFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)

                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onNegativeButtonClick(dialog, which);
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    public static Dialog getShareAppDialog(Context context, final DialogButtonListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.share_app_title)
                .setMessage(R.string.share_app_message)
                .setPositiveButton(R.string.i_am_in, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onNegativeButtonClick(dialog, which);
                        }
                    }
                });
        return builder.create();
    }

    public static Dialog getChooseSubjectDialog(Context context, final DialogPositiveButtonListener listener) {
        return getChooseSubjectDialog(context, new DialogButtonListener() {
            int index = 0;

            @Override
            public void onPositiveButtonClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onPositiveButtonClick(dialog, which);
                    listener.onPositiveButtonClick(dialog, which, subjectList[index].toString());
                }
            }

            @Override
            public void onNegativeButtonClick(DialogInterface dialog, int which) {

                if (listener != null) {
                    listener.onNegativeButtonClick(dialog, which);
                }
            }

            @Override
            public void onNeutralButtonClick(DialogInterface dialog, int which) {
                index = which;
                if (listener != null) {
                    listener.onNeutralButtonClick(dialog, which);
                }
            }
        });
    }

    public static Dialog getChooseSubjectDialog(Context context, final DialogButtonListener listener) {

        subjectList[0] = context.getString(R.string.suggestion);
        subjectList[1] = context.getString(R.string.Bug);
        subjectList[2] = context.getString(R.string.Help);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.choose_subject)
                .setSingleChoiceItems(subjectList, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onNeutralButtonClick(dialog, which);
                        }
                    }
                })
                .setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onNegativeButtonClick(dialog, which);
                        }
                    }
                });
        return builder.create();
    }

    public static Dialog getTryAgainDialog(Context context, int title, boolean isNegativeButton, final DialogButtonListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onPositiveButtonClick(dialog, which);
                        }
                    }
                });
        if (isNegativeButton) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (listener != null) {
                        listener.onNegativeButtonClick(dialog, which);
                    }
                }
            });
        }

        return builder.create();
    }

    public static Dialog getNoInternetDialog(Context context, boolean isNegativeButton, final DialogButtonListener listener) {

//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity)
//                .setTitle(mActivity.getResources().getString(R.string.no_network))
//                .setMessage(mActivity.getResources().getString(R.string.no_network_msg))
//                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mActivity.finish();
//                    }
//                })
//                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        mActivity.startActivity(intent);
//                        dialog.dismiss();
//                    }
//                });
//
//        alertDialog.show();

        Dialog noInternetDialog = SampleDialogs.getTryAgainDialog(context, R.string.no_internet_available, isNegativeButton, listener);
        if (!isNegativeButton) {
            noInternetDialog.setCanceledOnTouchOutside(false);
            noInternetDialog.setCancelable(false);
        }
        return noInternetDialog;
    }

    public static Dialog getOkCancelDialog(Context context, int title, int message, final DialogButtonListener listener) {
        return DialogBuilderUtils.getDialog(context, listener, title, message, R.string.ok, R.string.cancel);
    }

    public static Dialog getOkDialog(Context context, int title, int message, final DialogButtonListener listener) {
        return DialogBuilderUtils.getDialog(context, listener, title, message, R.string.ok, 0);
    }

    public static Dialog getOkDialog(Context context, String title, String message, final DialogButtonListener listener) {
        return DialogBuilderUtils.getDialog(context, listener, title, message, R.string.ok, 0);
    }
}
