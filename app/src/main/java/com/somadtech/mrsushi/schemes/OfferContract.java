package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class OfferContract {
    static final String SQL_CREATE_OFFER =
            "CREATE TABLE " + OfferEntry.TABLE_NAME + " (" +
                    OfferEntry._ID + " INTEGER PRIMARY KEY," +
                    OfferEntry.COLUMN_NAME_PRODUCT + " INTEGER," +
                    OfferEntry.COLUMN_NAME_VARIANT + " INTEGER," +
                    OfferEntry.COLUMN_NAME_OBSERVATIONS + " TEXT," +
                    OfferEntry.COLUMN_NAME_ORDER + " INTEGER," +
                    OfferEntry.COLUMN_NAME_QTY + " INTEGER," +
                    OfferEntry.COLUMN_NAME_STATE + " TEXT)";

    static final String SQL_DELETE_OFFER =
            "DROP TABLE IF EXISTS " + OfferEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private OfferContract() {}

    /* Inner class that defines the table contents */
    static class OfferEntry implements BaseColumns {
        static final String TABLE_NAME = "cart";
        static final String COLUMN_NAME_PRODUCT = "product_id";
        static final String COLUMN_NAME_VARIANT = "variant_id";
        static final String COLUMN_NAME_OBSERVATIONS = "observations";
        static final String COLUMN_NAME_ORDER = "order_id";
        static final String COLUMN_NAME_QTY = "quantity";
        static final String COLUMN_NAME_STATE = "state";
    }
}


