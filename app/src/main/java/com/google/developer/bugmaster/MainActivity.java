package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.InsectContract;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;

import static com.google.developer.bugmaster.data.InsectContract.MAIN_FORECAST_PROJECTION;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        InsectRecyclerAdapter.InsectRecyclerAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String SORT_QUERY = "sort";
    private static final int ID_INSECTS_LOADER = 23;

    private SharedPreferences mSharedPreferences;
    private InsectRecyclerAdapter mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mForecastAdapter = new InsectRecyclerAdapter(this, this);
        mRecyclerView.setAdapter(mForecastAdapter);
        getSupportLoaderManager().initLoader(ID_INSECTS_LOADER, null, this);

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
//
//        String sortOrder = mSharedPreferences.getString(SORT_QUERY, InsectContract.WeatherEntry.COLUMN_FRIENDLY_NAME);
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(SORT_QUERY)) {
//                sortOrder = savedInstanceState.getString(SORT_QUERY);
//            }
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //TODO: Implement the sort action
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        //TODO: Launch the quiz activity
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(this, QuizActivity.class));
                break;


            default:

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {

            case ID_INSECTS_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = InsectContract.WeatherEntry.CONTENT_URI;
                String sortOrder = InsectContract.WeatherEntry.COLUMN_FRIENDLY_NAME + " ASC";


                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mForecastAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(this, InsectDetailsActivity.class);
        intent.putExtra(InsectDetailsActivity.EXTRA_INSECT_ID, id);
        startActivity(intent);
    }
}
