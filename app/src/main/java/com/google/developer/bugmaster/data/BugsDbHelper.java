package com.google.developer.bugmaster.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.developer.bugmaster.R;
import com.google.developer.bugmaster.data.InsectContract.InsectEntry;
import com.google.developer.bugmaster.util.AppExecutors;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;

    //Used to read data from res/ and assets/
    private Resources mResources;
    private Context mContext;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mResources = context.getResources();
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + InsectEntry.TABLE_NAME + " (" +

                        InsectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        InsectEntry.COLUMN_FRIENDLY_NAME + " TEXT NOT NULL, " +
                        InsectEntry.COLUMN_SCIENTIFIC_NAME + " TEXT NOT NULL," +
                        InsectEntry.COLUMN_CLASSIFICATION + " TEXT NOT NULL, " +
                        InsectEntry.COLUMN_IMAGE_ASSET + " TEXT NOT NULL, " +
                        InsectEntry.COLUMN_DANGER_LEVEL + " INTEGER NOT NULL, " +
                        " UNIQUE (" + InsectEntry.COLUMN_SCIENTIFIC_NAME + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);

        AppExecutors appExecutors = new AppExecutors();

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    readInsectsFromResources(sqLiteDatabase);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.insects);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        final String rawJson = builder.toString();
        InsectsCollection collection = new Gson().fromJson(rawJson, InsectsCollection.class);

        if (collection != null && collection.insects != null && !collection.insects.isEmpty()) {
            ContentValues[] weatherContentValues = new ContentValues[collection.insects.size()];


            for (int index = 0; index < collection.insects.size(); index++) {
                Insect data = collection.insects.get(index);
                ContentValues weatherValues = new ContentValues();
                weatherValues.put(InsectEntry.COLUMN_FRIENDLY_NAME, data.name);
                weatherValues.put(InsectEntry.COLUMN_SCIENTIFIC_NAME, data.scientificName);
                weatherValues.put(InsectEntry.COLUMN_CLASSIFICATION, data.classification);
                weatherValues.put(InsectEntry.COLUMN_IMAGE_ASSET, data.imageAsset);
                weatherValues.put(InsectEntry.COLUMN_DANGER_LEVEL, data.dangerLevel);

                weatherContentValues[index] = weatherValues;
            }


            ContentResolver sunshineContentResolver = mContext.getContentResolver();
            sunshineContentResolver.bulkInsert(
                    InsectEntry.CONTENT_URI,
                    weatherContentValues);
        }
    }
}
