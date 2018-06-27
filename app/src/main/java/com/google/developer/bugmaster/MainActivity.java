package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.developer.bugmaster.data.Insect;
import com.google.developer.bugmaster.data.InsectContract;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;
import com.google.developer.bugmaster.reminders.ReminderService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.google.developer.bugmaster.QuizActivity.EXTRA_ANSWER;
import static com.google.developer.bugmaster.QuizActivity.EXTRA_INSECTS;
import static com.google.developer.bugmaster.data.InsectContract.MAIN_INSECTS_PROJECTION;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        InsectRecyclerAdapter.InsectRecyclerAdapterOnClickHandler {

    public static final String SORT_QUERY = "sort";
    private String mSortOrder;
    private static final int ID_INSECTS_LOADER = 23;

    private SharedPreferences mSharedPreferences;
    private InsectRecyclerAdapter mForecastAdapter;
    private ArrayList<Insect> mRandomInsectList = new ArrayList<>();

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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSortOrder = mSharedPreferences.getString(SORT_QUERY, InsectContract.InsectEntry.COLUMN_FRIENDLY_NAME);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_QUERY)) {
                mSortOrder = savedInstanceState.getString(SORT_QUERY);
            }
        }

        getSupportLoaderManager().initLoader(ID_INSECTS_LOADER, null, this);

        Intent i = new Intent(this, ReminderService.class);
        startService(i);
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
                if (mSortOrder.equals(InsectContract.InsectEntry.COLUMN_FRIENDLY_NAME)) {
                    mSortOrder = InsectContract.InsectEntry.COLUMN_DANGER_LEVEL;
                } else {
                    mSortOrder = InsectContract.InsectEntry.COLUMN_FRIENDLY_NAME;
                }
                mSharedPreferences.edit().putString(SORT_QUERY, mSortOrder).commit();
                getSupportLoaderManager().restartLoader(ID_INSECTS_LOADER, null, this);

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
                Intent quizIntent = new Intent(this, QuizActivity.class);
                quizIntent.putParcelableArrayListExtra(EXTRA_INSECTS, mRandomInsectList);
                quizIntent.putExtra(EXTRA_ANSWER, mRandomInsectList.get(new Random().nextInt(mRandomInsectList.size())));
                startActivity(quizIntent);
                break;


            default:

        }
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle bundle) {
        switch (loaderId) {

            case ID_INSECTS_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri forecastQueryUri = InsectContract.InsectEntry.CONTENT_URI;
                String sortOrder = null;

                if (mSortOrder.equals(InsectContract.InsectEntry.COLUMN_FRIENDLY_NAME)) {
                    sortOrder = InsectContract.InsectEntry.COLUMN_FRIENDLY_NAME + " ASC";
                } else {
                    sortOrder = InsectContract.InsectEntry.COLUMN_DANGER_LEVEL + " DESC";
                }

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_INSECTS_PROJECTION,
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
        fillList(cursor);
    }

    private void fillList(Cursor cursor) {
        ArrayList<Insect> tempList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Insect insect = new Insect(cursor);
                tempList.add(insect);
            } while (cursor.moveToNext());
            Collections.shuffle(tempList);
            mRandomInsectList = tempList;
        } else {
            mRandomInsectList.clear();
        }

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
