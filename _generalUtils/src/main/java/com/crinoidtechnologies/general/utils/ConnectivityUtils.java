package com.crinoidtechnologies.general.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Connectivity helper provides simple API to listen for network connected / disconnected state.
 * <p/>
 * with {@link #registerDefault(android.content.Context)}.
 * <p/>
 * Requires <code>android.permission.ACCESS_NETWORK_STATE</code> permission
 */
public final class ConnectivityUtils {

    private static final List<ConnectivityListener> listeners = new ArrayList<>();
    private static boolean sIsConnected = true;
    private static ConnectivityReceiver receiver;

    private ConnectivityUtils() {
    }

    /**
     * Be sure to remove receiver at appropriate time (i.e. in Activity.onPause()).
     */
    public static synchronized void register(Context context, ConnectivityListener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)){
                return;
            }
        }
        registerIfNeeded(context);
    }

    public static synchronized void unregister(Context context, ConnectivityListener listener) {
        listeners.remove(listener);
        if (listeners.size() == 0) {
            if (receiver != null) {
                context.getApplicationContext().unregisterReceiver(receiver);
                receiver = null;
            }
        }
    }

    public static synchronized void registerDefault(Context appContext) {
        registerIfNeeded(appContext);
    }

    private static void registerIfNeeded(Context context) {
        if (receiver == null) {
            receiver = new ConnectivityReceiver(new ConnectivityListener() {
                @Override
                public void onConnectionEstablished() {
                    synchronized (listeners) {
                        for (ConnectivityListener l : listeners) {
                            l.onConnectionEstablished();
                        }
                    }
                }

                @Override
                public void onConnectionLost() {
                    synchronized (listeners) {
                        for (ConnectivityListener l : listeners) {
                            l.onConnectionLost();
                        }
                    }
                }
            });
            context.getApplicationContext().registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        }
    }

    public interface ConnectivityListener {
        /**
         * Called on the UI thread when connection established (network is available).
         */
        void onConnectionEstablished();

        /**
         * Called on the UI thread when connection lost (network is unavailable).
         */
        void onConnectionLost();
    }

    private static class ConnectivityReceiver extends BroadcastReceiver {

        private final ConnectivityListener mConnectivityListener;

        private ConnectivityReceiver(ConnectivityListener connectivityListener) {
            if (connectivityListener == null) throw new NullPointerException();
            this.mConnectivityListener = connectivityListener;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            if (!ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) return;

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

            if (isConnected != sIsConnected) {
                sIsConnected = isConnected;

                if (isConnected) {
                    mConnectivityListener.onConnectionEstablished();
                } else {
                    mConnectivityListener.onConnectionLost();
                }
            }
        }

    }

}
