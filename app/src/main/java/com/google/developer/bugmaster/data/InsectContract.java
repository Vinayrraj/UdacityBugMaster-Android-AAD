package com.google.developer.bugmaster.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class InsectContract {

    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_INSECTS_PROJECTION = {
            InsectEntry._ID,
            InsectEntry.COLUMN_FRIENDLY_NAME,
            InsectEntry.COLUMN_SCIENTIFIC_NAME,
            InsectEntry.COLUMN_DANGER_LEVEL
    };

    public static final String[] DETAIL_INSECT_PROJECTION = {
            InsectEntry._ID,
            InsectEntry.COLUMN_FRIENDLY_NAME,
            InsectEntry.COLUMN_SCIENTIFIC_NAME,
            InsectEntry.COLUMN_DANGER_LEVEL,
            InsectEntry.COLUMN_CLASSIFICATION,
            InsectEntry.COLUMN_IMAGE_ASSET
    };
    public interface ProjectionIndex{
        public int INDEX_ID = 0;
        public int INDEX_FRIENDLY_NAME = 1;
        public int INDEX_SCIENTIFIC_NAME = 2;
        public int INDEX_DANGER_LEVEL = 3;
        public int INDEX_CLASSIFICATION = 4;
        public int INDEX_IMAGE_ASSET = 5;
    }

    public static final String CONTENT_AUTHORITY = "com.google.developer.bugmaster";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "insect";

    public static final class InsectEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static Uri buildInsectUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(String.valueOf(id))
                    .build();
        }

        public static final String TABLE_NAME = "insect";
        public static final String COLUMN_FRIENDLY_NAME = "friendly_name";
        public static final String COLUMN_SCIENTIFIC_NAME = "scientific_name";
        public static final String COLUMN_CLASSIFICATION = "classification";
        public static final String COLUMN_IMAGE_ASSET = "image_asset";

        public static final String COLUMN_DANGER_LEVEL = "danger_level";

    }
}
