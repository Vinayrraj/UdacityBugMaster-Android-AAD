package com.google.developer.bugmaster.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class InsectContract {

    public static final String CONTENT_AUTHORITY = "com.google.developer.bugmaster";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER = "insect";

    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "insect";

        public static final String COLUMN_FRIENDLY_NAME = "friendly_name";
        public static final String COLUMN_SCIENTIFIC_NAME = "scientific_name";
        public static final String COLUMN_CLASSIFICATION = "classification";
        public static final String COLUMN_IMAGE_ASSET = "image_asset";
        public static final String COLUMN_DANGER_LEVEL = "danger_level";

        public static Uri buildWeatherUriWithDate(String scientificName) {
            return CONTENT_URI.buildUpon()
                    .appendPath(scientificName)
                    .build();
        }

    }
}
