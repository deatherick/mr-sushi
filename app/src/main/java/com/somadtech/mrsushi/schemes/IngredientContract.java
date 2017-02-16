package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class IngredientContract {
    static final String SQL_CREATE_INGREDIENTS =
            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                    IngredientEntry._ID + " INTEGER PRIMARY KEY," +
                    IngredientEntry.COLUMN_NAME_NAME + " TEXT," +
                    IngredientEntry.COLUMN_NAME_SLUG + " TEXT," +
                    IngredientEntry.COLUMN_NAME_IMAGE + " TEXT)";

    static final String SQL_DELETE_INGREDIENTS =
            "DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private IngredientContract() {}

    /* Inner class that defines the table contents */
    static class IngredientEntry implements BaseColumns {
        static final String TABLE_NAME = "ingredients";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SLUG = "slug";
        static final String COLUMN_NAME_IMAGE = "image";
    }
}


