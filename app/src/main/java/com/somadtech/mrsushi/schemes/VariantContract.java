package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/7/17.
 * Project: mrsushi-android
 */

public class VariantContract {
    static final String SQL_CREATE_VARIANTS =
            "CREATE TABLE " + VariantEntry.TABLE_NAME + " (" +
                    VariantEntry._ID + " INTEGER PRIMARY KEY," +
                    VariantEntry.COLUMN_NAME_NAME + " TEXT," +
                    VariantEntry.COLUMN_NAME_PRICE + " REAL," +
                    VariantEntry.COLUMN_NAME_PR_ID + " INTEGER)";

    static final String SQL_DELETE_VARIANTS =
            "DROP TABLE IF EXISTS " + VariantEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private VariantContract() {}

    /* Inner class that defines the table contents */
    static class VariantEntry implements BaseColumns {
        static final String TABLE_NAME = "variants";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_PRICE = "price";
        static final String COLUMN_NAME_PR_ID = "product_id";
    }
}


