package com.twitter.university.android.yamba.svc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;
import com.twitter.university.android.yamba.BuildConfig;
import com.twitter.university.android.yamba.R;
import com.twitter.university.android.yamba.YambaApplication;
import com.twitter.university.android.yamba.YambaContract;

import java.util.ArrayList;
import java.util.List;


class YambaLogic {
    private static final String TAG = "LOGIC";

    private static final int OP_TOAST = -3;

    private final YambaApplication app;
    private final int maxPolls;

    // Must be called from the UI thread
    public YambaLogic(final YambaApplication app, int maxPolls) {
        this.app = app;
        this.maxPolls = maxPolls;
    }

    public void doPost(String tweet) {
        ContentValues cv = new ContentValues();
        cv.put(YambaContract.Posts.Columns.TWEET, tweet);
        cv.put(YambaContract.Posts.Columns.TIMESTAMP, System.currentTimeMillis());
        app.getContentResolver().insert(YambaContract.Posts.URI, cv);
    }

    public void doPoll() {
        Log.d(TAG, "poll");
        try {
            parseTimeline(app.getClient().getTimeline(maxPolls));
        }
        catch (Exception e) {
            Log.e(TAG, "Poll failed: " + e, e);
        }
    }

    private int parseTimeline(List<Status> timeline) {
        long latest = getMaxTimestamp();

        List<ContentValues> vals = new ArrayList<ContentValues>();
        for (Status tweet: timeline) {
            long t = tweet.getCreatedAt().getTime();
            if (t <= latest) { continue; }

            ContentValues row = new ContentValues();
            row.put(YambaContract.Timeline.Columns.ID, Long.valueOf(tweet.getId()));
            row.put(YambaContract.Timeline.Columns.TIMESTAMP, Long.valueOf(t));
            row.put(YambaContract.Timeline.Columns.HANDLE, tweet.getUser());
            row.put(YambaContract.Timeline.Columns.TWEET, tweet.getMessage());
            vals.add(row);
        }

        int n = vals.size();
        if (0 >= n) { return 0; }
        n = app.getContentResolver().bulkInsert(
            YambaContract.Timeline.URI,
            vals.toArray(new ContentValues[n]));

        if (BuildConfig.DEBUG) { Log.d(TAG, "inserted: " + n); }
        return n;
    }

    private long getMaxTimestamp() {
        Cursor c = null;
        try {
            c = app.getContentResolver().query(
                YambaContract.MaxTimeline.URI,
                null,
                null,
                null,
                null);
            return ((null == c) || (!c.moveToNext()))
                ? Long.MIN_VALUE
                : c.getLong(0);
        }
        finally {
            if (null != c) { c.close(); }
        }
    }
}
