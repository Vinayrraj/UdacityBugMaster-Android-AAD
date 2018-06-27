package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private BugsDbHelper mBugsDbHelper;

    private DatabaseManager(Context context) {
        mBugsDbHelper = new BugsDbHelper(context);
    }

    public int bulkInsert(Context context, @NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mBugsDbHelper.getWritableDatabase();
        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(InsectContract.WeatherEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }

    public Cursor queryAllInsects(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mBugsDbHelper.getReadableDatabase().query(
                InsectContract.WeatherEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }


    public Cursor queryInsectsById(String[] projection, String[] selectionArgs) {
        return mBugsDbHelper.getReadableDatabase().query(
                InsectContract.WeatherEntry.TABLE_NAME,
                projection,
                InsectContract.WeatherEntry._ID + " = ? ",
                selectionArgs,
                null,
                null,
                null);
    }
}
