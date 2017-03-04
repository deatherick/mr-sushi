package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class BannerContract {
    static final String SQL_CREATE_BANNERS =
            "CREATE TABLE " + BannerEntry.TABLE_NAME + " (" +
                    BannerEntry._ID + " INTEGER PRIMARY KEY," +
                    BannerEntry.COLUMN_NAME_NAME + " TEXT," +
                    BannerEntry.COLUMN_NAME_SLUG + " TEXT," +
                    BannerEntry.COLUMN_NAME_DESC + " TEXT," +
                    BannerEntry.COLUMN_NAME_TARGET + " INTEGER," +
                    BannerEntry.COLUMN_NAME_IMAGE + " TEXT)";

    static final String SQL_DELETE_BANNERS =
            "DROP TABLE IF EXISTS " + BannerEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BannerContract() {}

    /* Inner class that defines the table contents */
    static class BannerEntry implements BaseColumns {
        static final String TABLE_NAME = "banners";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SLUG = "slug";
        static final String COLUMN_NAME_DESC = "description";
        static final String COLUMN_NAME_TARGET = "target";
        static final String COLUMN_NAME_IMAGE = "image";
    }
}


