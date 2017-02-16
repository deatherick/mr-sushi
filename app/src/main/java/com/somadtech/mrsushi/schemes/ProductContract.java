package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/7/17.
 * Project: mrsushi-android
 */

public class ProductContract {
    static final String SQL_CREATE_PRODUCTS =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductEntry.COLUMN_NAME_NAME + " TEXT," +
                    ProductEntry.COLUMN_NAME_DESC + " TEXT," +
                    ProductEntry.COLUMN_NAME_PRICE + " REAL," +
                    ProductEntry.COLUMN_NAME_CAT + " INTEGER," +
                    ProductEntry.COLUMN_NAME_SLUG + " TEXT," +
                    ProductEntry.COLUMN_NAME_THUMB + " TEXT," +
                    ProductEntry.COLUMN_NAME_IMAGE + " TEXT)";

    static final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProductContract() {}

    /* Inner class that defines the table contents */
    static class ProductEntry implements BaseColumns {
        static final String TABLE_NAME = "products";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_DESC = "description";
        static final String COLUMN_NAME_PRICE = "price";
        static final String COLUMN_NAME_CAT = "category";
        static final String COLUMN_NAME_SLUG = "slug";
        static final String COLUMN_NAME_THUMB = "thumbnail";
        static final String COLUMN_NAME_IMAGE = "full_image";
    }
}


