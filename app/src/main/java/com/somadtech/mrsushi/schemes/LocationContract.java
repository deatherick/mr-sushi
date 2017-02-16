package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/7/17.
 * Project: mrsushi-android
 */

public class LocationContract {
    static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_NAME + " TEXT," +
                    LocationEntry.COLUMN_NAME_DESC + " TEXT," +
                    LocationEntry.COLUMN_NAME_SLUG + " TEXT," +
                    LocationEntry.COLUMN_NAME_LAT + " TEXT," +
                    LocationEntry.COLUMN_NAME_LNG + " TEXT," +
                    LocationEntry.COLUMN_NAME_IMAGE + " TEXT)";

    static final String SQL_DELETE_LOCATIONS =
            "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private LocationContract() {}

    /* Inner class that defines the table contents */
    static class LocationEntry implements BaseColumns {
        static final String TABLE_NAME = "locations";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_DESC = "description";
        static final String COLUMN_NAME_SLUG = "slug";
        static final String COLUMN_NAME_LAT = "latitude";
        static final String COLUMN_NAME_LNG = "longitude";
        static final String COLUMN_NAME_IMAGE = "image";
    }
}


