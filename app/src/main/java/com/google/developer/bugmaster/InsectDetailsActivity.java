package com.google.developer.bugmaster;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

public class InsectDetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String EXTRA_INSECT_ID = "EXTRA_INSECT_ID";
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Implement layout and display insect details
        mId = getIntent().getIntExtra(EXTRA_INSECT_ID, 0);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
