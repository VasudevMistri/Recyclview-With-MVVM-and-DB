package com.app.wamatask.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityTools {

    public static boolean internetConnected = true;
    public static CheckInternetConnectivity checkInternetConnectivity;
    public int TYPE_WIFI = 1;
    public int TYPE_MOBILE = 2;
    public int TYPE_NOT_CONNECTED = 0;
    public String internetConnectedMsg = "Internet Connected";
    public String internetNotConnectedMsg = "Lost Internet Connection";
    public String internetStatus = "";

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getConnectivityStatusString(context);
            String status = getConnectivityStatusString(context);
            checkInternetConnection(status);
        }
    };
    Context context;

    public ConnectivityTools(Context context, CheckInternetConnectivity checkInternetConnectivity) {
        this.context = context;
        checkInternetConnectivity = checkInternetConnectivity;
    }

    public static boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    public int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    //to check internet connection with broadcast receiver
    public void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(broadcastReceiver, internetFilter);
    }

    public void unRegisterInternetCheckReceiver() {
        context.unregisterReceiver(broadcastReceiver);
    }

    public String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    private void checkInternetConnection(String status) {
        if (status.equalsIgnoreCase("Wifi enabled") || status.equalsIgnoreCase("Mobile data enabled")) {
            internetStatus = internetConnectedMsg;
            if (!internetConnected) {
                internetConnected = true;
                try {
                    checkInternetConnectivity.checkInternet(true, "");
                    Log.e("onRes Connection File", "Connected");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            internetStatus = internetNotConnectedMsg;
            if (internetConnected) {
                internetConnected = false;
                try {
                    checkInternetConnectivity.checkInternet(false, "");
                    Log.e("onRes Connection File", "Not Connected");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface CheckInternetConnectivity {
        void checkInternet(boolean isConnected, String msg);
    }


}
