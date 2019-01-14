package com.example.boo.lab2android.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.boo.lab2android.repositories.ContactRepository;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String CLASSNAME = "RECEIVER";
    @Override
    public void onReceive(final Context context,final Intent intent) {
        boolean isConnected = NetworkConnectivityStatus.getConnectivityStatus(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) || "android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction()) ) {
            if (isConnected) {
                Log.d(CLASSNAME, "connection on");
                ContactRepository.updateContacts();
            } else {
                Log.d(CLASSNAME, "connection off");
            }
        }
    }
}
