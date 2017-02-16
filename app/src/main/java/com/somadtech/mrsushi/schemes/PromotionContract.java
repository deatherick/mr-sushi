package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class PromotionContract {
    static final String SQL_CREATE_PROMOTIONS =
            "CREATE TABLE " + PromotionEntry.TABLE_NAME + " (" +
                    PromotionEntry._ID + " INTEGER PRIMARY KEY," +
                    PromotionEntry.COLUMN_NAME_NAME + " TEXT," +
                    PromotionEntry.COLUMN_NAME_SLUG + " TEXT," +
                    PromotionEntry.COLUMN_NAME_IMAGE_S + " TEXT," +
                    PromotionEntry.COLUMN_NAME_IMAGE_L + " TEXT)";

    static final String SQL_DELETE_PROMOTIONS =
            "DROP TABLE IF EXISTS " + PromotionEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PromotionContract() {}

    /* Inner class that defines the table contents */
    static class PromotionEntry implements BaseColumns {
        static final String TABLE_NAME = "promotions";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SLUG = "slug";
        static final String COLUMN_NAME_IMAGE_S = "image_small";
        static final String COLUMN_NAME_IMAGE_L = "image_large";
    }
}


