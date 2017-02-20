package com.somadtech.mrsushi.schemes;

import android.provider.BaseColumns;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class IngredientProductContract {
    static final String SQL_CREATE_INGREDIENT_PRODUCT =
            "CREATE TABLE " + IngredientProductEntry.TABLE_NAME + " (" +
                    IngredientProductEntry._ID + " INTEGER PRIMARY KEY," +
                    IngredientProductEntry.COLUMN_NAME_INGREDIENT_ID + " INTEGER," +
                    IngredientProductEntry.COLUMN_NAME_PRODUCT_ID + " INTEGER," +
                    "UNIQUE(" + IngredientProductEntry.COLUMN_NAME_INGREDIENT_ID + ", " +
                    IngredientProductEntry.COLUMN_NAME_PRODUCT_ID + ") ON CONFLICT REPLACE);";

    static final String SQL_DELETE_INGREDIENT_PRODUCT =
            "DROP TABLE IF EXISTS " + IngredientProductEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private IngredientProductContract() {}

    /* Inner class that defines the table contents */
    static class IngredientProductEntry implements BaseColumns {
        static final String TABLE_NAME = "ingredient_product";
        static final String COLUMN_NAME_INGREDIENT_ID = "ingredient_id";
        static final String COLUMN_NAME_PRODUCT_ID = "product_id";
    }
}


