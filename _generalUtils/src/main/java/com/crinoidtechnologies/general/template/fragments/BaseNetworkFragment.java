package com.crinoidtechnologies.general.template.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.crinoidtechnologies.general.utils.ConnectivityUtils;
import com.crinoidtechnologies.general.utils.ProgressDialogUtils;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseNetworkFragment extends BaseFragment implements ConnectivityUtils.ConnectivityListener {

    protected BroadcastReceiver receiver;
    protected ProgressDialog dialog;
    boolean isReceiverRegistered = false;

    @Override
    public void onConnectionEstablished() {

    }

    @Override
    public void onConnectionLost() {

    }

    protected void onBroadCastReceive(Context context, Intent intent) {

    }

    protected IntentFilter getIntentFilterForReceiver() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = getIntentFilterForReceiver();
        if (filter != null) {
            if (receiver == null) {
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        onBroadCastReceive(context, intent);
                    }
                };
            }

            if (!isReceiverRegistered) {
                getActivity().registerReceiver(receiver, filter);
                isReceiverRegistered = true;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
        if (receiver != null) {
            try {
                if (isReceiverRegistered)
                    getActivity().unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isReceiverRegistered = false;
    }

    protected void showLoadingBar(String message) {
        if (dialog == null) {
            dialog = ProgressDialogUtils.getProgressDialog(getActivity(), message);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.dismiss();
        dialog.show();
    }

    private void createLoaderView() {

    }

    protected void hideLoadingBar() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
