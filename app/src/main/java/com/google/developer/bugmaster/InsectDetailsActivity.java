package com.google.developer.bugmaster;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.developer.bugmaster.data.InsectContract;

import static com.google.developer.bugmaster.data.InsectContract.DETAIL_INSECT_PROJECTION;

public class InsectDetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String EXTRA_INSECT_ID = "EXTRA_INSECT_ID";
    private static final int ID_INSECT_LOADER = 24;
    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //TODO: Implement layout and display insect details
        mId = getIntent().getIntExtra(EXTRA_INSECT_ID, 0);
        getSupportLoaderManager().initLoader(ID_INSECT_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {

            case ID_INSECT_LOADER:
                /* URI for all rows of weather data in our weather table */
                Uri insectQueryUri = InsectContract.InsectEntry.buildInsectUriWithId(mId);

                return new CursorLoader(this,
                        insectQueryUri,
                        DETAIL_INSECT_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        initData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        initData(null);
    }

    private void initData(Cursor data) {
        data.moveToPosition(0);
        ImageView iv = findViewById(R.id.image_view);

        Glide.with(getBaseContext())
                .load(Uri.parse("file:///android_asset/" + data.getString(InsectContract.ProjectionIndex.INDEX_IMAGE_ASSET)))
                .into(iv);

        TextView tvCommonName = findViewById(R.id.common_name_view);
        TextView tvScientificName = findViewById(R.id.scientific_name_view);
        TextView tvClassification = findViewById(R.id.classification_view);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        tvCommonName.setText(data.getString(InsectContract.ProjectionIndex.INDEX_FRIENDLY_NAME));
        tvScientificName.setText(data.getString(InsectContract.ProjectionIndex.INDEX_SCIENTIFIC_NAME));
        tvClassification.setText(getString(R.string.classification, data.getString(InsectContract.ProjectionIndex.INDEX_CLASSIFICATION)));
        ratingBar.setRating(data.getInt(InsectContract.ProjectionIndex.INDEX_DANGER_LEVEL));
    }
}
