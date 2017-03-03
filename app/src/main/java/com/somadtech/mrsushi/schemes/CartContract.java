package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class CartContract {
    static final String SQL_CREATE_CART =
            "CREATE TABLE " + CartEntry.TABLE_NAME + " (" +
                    CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CartEntry.COLUMN_NAME_PRODUCT + " INTEGER," +
                    CartEntry.COLUMN_NAME_VARIANT + " INTEGER," +
                    CartEntry.COLUMN_NAME_OBSERVATIONS + " TEXT," +
                    CartEntry.COLUMN_NAME_ORDER + " INTEGER," +
                    CartEntry.COLUMN_NAME_QTY + " INTEGER," +
                    CartEntry.COLUMN_NAME_PROM_PROD + " INTEGER," +
                    CartEntry.COLUMN_NAME_STATE + " TEXT)";

    static final String SQL_DELETE_CART =
            "DROP TABLE IF EXISTS " + CartEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CartContract() {}

    /* Inner class that defines the table contents */
    static class CartEntry implements BaseColumns {
        static final String TABLE_NAME = "cart";
        static final String COLUMN_NAME_PRODUCT = "product_id";
        static final String COLUMN_NAME_VARIANT = "variant_id";
        static final String COLUMN_NAME_OBSERVATIONS = "observations";
        static final String COLUMN_NAME_ORDER = "order_id";
        static final String COLUMN_NAME_QTY = "quantity";
        static final String COLUMN_NAME_PROM_PROD = "promotion_product";
        static final String COLUMN_NAME_STATE = "state";
    }
}


