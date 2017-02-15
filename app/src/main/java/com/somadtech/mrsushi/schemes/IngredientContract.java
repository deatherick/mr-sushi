package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class IngredientContract {
    static final String SQL_CREATE_INGREDIENT =
            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                    IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    IngredientEntry.COLUMN_NAME_PRODUCT + " INTEGER," +
                    IngredientEntry.COLUMN_NAME_VARIANT + " INTEGER," +
                    IngredientEntry.COLUMN_NAME_OBSERVATIONS + " TEXT," +
                    IngredientEntry.COLUMN_NAME_ORDER + " INTEGER," +
                    IngredientEntry.COLUMN_NAME_QTY + " INTEGER," +
                    IngredientEntry.COLUMN_NAME_STATE + " TEXT)";

    static final String SQL_DELETE_INGREDIENT =
            "DROP TABLE IF EXISTS " + IngredientEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private IngredientContract() {}

    /* Inner class that defines the table contents */
    static class IngredientEntry implements BaseColumns {
        static final String TABLE_NAME = "cart";
        static final String COLUMN_NAME_PRODUCT = "product_id";
        static final String COLUMN_NAME_VARIANT = "variant_id";
        static final String COLUMN_NAME_OBSERVATIONS = "observations";
        static final String COLUMN_NAME_ORDER = "order_id";
        static final String COLUMN_NAME_QTY = "quantity";
        static final String COLUMN_NAME_STATE = "state";
    }
}


