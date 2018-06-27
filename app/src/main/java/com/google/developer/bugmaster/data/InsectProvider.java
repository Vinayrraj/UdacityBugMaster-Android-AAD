package com.google.developer.bugmaster.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class InsectProvider extends ContentProvider {

    public static final int CODE_INSECT = 100;
    public static final int CODE_INSECT_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DatabaseManager mDatabaseManager;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = InsectContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, InsectContract.PATH_WEATHER, CODE_INSECT);
        matcher.addURI(authority, InsectContract.PATH_WEATHER + "/#", CODE_INSECT_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseManager = DatabaseManager.getInstance(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        switch (sUriMatcher.match(uri)) {

            case CODE_INSECT:
                return mDatabaseManager.bulkInsert(getContext(), uri, values);

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        /*
         * Here's the switch statement that, given a URI, will determine what kind of request is
         * being made and query the database accordingly.
         */
        switch (sUriMatcher.match(uri)) {

            case CODE_INSECT_WITH_ID: {
                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{id};
                cursor = mDatabaseManager.queryInsectsById(projection, selectionArguments);
                break;
            }

            case CODE_INSECT: {
                cursor = mDatabaseManager.queryAllInsects(
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implementing getType right now.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException("Not implementing insert right now.");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Not implementing delete right now.");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Not implementing update right now.");
    }
}
