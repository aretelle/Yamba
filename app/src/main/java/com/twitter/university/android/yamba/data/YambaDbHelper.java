package com.twitter.university.android.yamba.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class YambaDbHelper extends SQLiteOpenHelper {
    private static final String DB_FILE = "yamba.db";
    private static final int VERSION = 1;

    public static final String TABLE_TIMELINE = "timeline";
    public static final String COL_ID = "id";
    public static final String COL_TIMESTAMP = "p_timestamp";
    public static final String COL_HANDLE = "p_handle";
    public static final String COL_TWEET = "p_tweet";


    public YambaDbHelper(Context ctxt) {
        super(ctxt, DB_FILE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE " + TABLE_TIMELINE + " ("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_TIMESTAMP + " INTEGER NOT NULL,"
                + COL_HANDLE + " TEXT NOT NULL,"
                + COL_TWEET + " TEXT NOT NULL)"
            );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE " + TABLE_TIMELINE);
        onCreate(db);
    }
}
