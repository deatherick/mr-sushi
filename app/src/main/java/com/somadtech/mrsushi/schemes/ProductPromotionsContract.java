package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class ProductPromotionsContract {
    static final String SQL_CREATE_PRODUCT_PROMOTIONS =
            "CREATE TABLE " + ProductPromotionsEntry.TABLE_NAME + " (" +
                    ProductPromotionsEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductPromotionsEntry.COLUMN_NAME_TYPE + " TEXT," +
                    ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID + " INTEGER," +
                    ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID + " INTEGER," +
                    "UNIQUE(" + ProductPromotionsEntry.COLUMN_NAME_TYPE + ", " +
                    ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID + ", " +
                    ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID + ") ON CONFLICT REPLACE);";

    static final String SQL_DELETE_PRODUCT_PROMOTIONS =
            "DROP TABLE IF EXISTS " + ProductPromotionsEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProductPromotionsContract() {}

    /* Inner class that defines the table contents */
    static class ProductPromotionsEntry implements BaseColumns {
        static final String TABLE_NAME = "product_promotions";
        static final String COLUMN_NAME_TYPE =  "type";
        static final String COLUMN_NAME_PROMOTION_ID = "promotion_id";
        static final String COLUMN_NAME_PRODUCT_ID = "product_id";
    }
}


