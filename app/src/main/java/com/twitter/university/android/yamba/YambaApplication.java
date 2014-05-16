package com.twitter.university.android.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;


public class YambaApplication extends Application
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = "APP";

    private static final String DEF_HANDLE = "student";
    private static final String DEF_PASSWD = "password";
    private static final String DEF_ENDPOINT = "http://yamba.marakana.com/api";

    private YambaClient client;
    private String handleKey;
    private String pwdKey;
    private String uriKey;

    @Override
    public void onCreate() {
        super.onCreate();

        handleKey = getString(R.string.prefs_key_handle);
        pwdKey = getString(R.string.prefs_key_passwd);
        uriKey = getString(R.string.prefs_key_uri);

        // Don't use an anonymous class to handle this event!
        // http://stackoverflow.com/questions/3799038/onsharedpreferencechanged-not-fired-if-change-occurs-in-separate-activity
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public synchronized void onSharedPreferenceChanged(SharedPreferences p, String s) {
        client = null;
    }

    public synchronized YambaClient getClient() {
        if (null == client) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String usr = prefs.getString(handleKey, DEF_HANDLE);
            String pwd = prefs.getString(pwdKey, DEF_PASSWD);
            String uri = prefs.getString(uriKey, DEF_ENDPOINT);
            Log.d(TAG, "new client: " + usr + "@" + uri);

            try { client = new YambaClient(usr, pwd, uri); }
            catch (Exception e) {
                Log.e(TAG, "failed creating client: " + e, e);
            }
        }

        return client;
    }
}
