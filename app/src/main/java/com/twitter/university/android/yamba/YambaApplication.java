package com.twitter.university.android.yamba;

import android.app.Application;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;


public class YambaApplication extends Application {
    private static final String TAG = "APP";

    private static final String DEF_HANDLE = "student";
    private static final String DEF_PASSWD = "password";
    private static final String DEF_ENDPOINT = "http://yamba.marakana.com/api";

    private YambaClient client;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public synchronized YambaClient getClient() {
        if (null == client) {
            try { client = new YambaClient(DEF_HANDLE, DEF_PASSWD, DEF_ENDPOINT); }
            catch (Exception e) {
                Log.e(TAG, "failed creating client: " + e, e);
            }
        }

        return client;
    }
}
