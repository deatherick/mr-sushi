package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class CategoryContract {
    static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                    CategoryEntry.COLUMN_NAME_NAME + " TEXT," +
                    CategoryEntry.COLUMN_NAME_IMAGE + " TEXT)";

    static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CategoryContract() {}

    /* Inner class that defines the table contents */
    static class CategoryEntry implements BaseColumns {
        static final String TABLE_NAME = "categories";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_IMAGE = "image";
    }
}


