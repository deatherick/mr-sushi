package com.somadtech.mrsushi.schemes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class MrSushiDbHelper extends SQLiteOpenHelper {
    private static final String LOG = "MrSushiDbHelper";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MrSushi.db";

    public MrSushiDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategoryContract.SQL_CREATE_CATEGORIES);
        db.execSQL(ProductContract.SQL_CREATE_PRODUCTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CategoryContract.SQL_DELETE_CATEGORIES);
        db.execSQL(ProductContract.SQL_DELETE_PRODUCTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    /**
     * @param category Category
     * @return int
     */
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryContract.CategoryEntry._ID, category.getItemId());
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_NAME, category.getItemName());
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE, category.getItemImage());

        // insert row

        int id = (int) db.insertWithOnConflict(CategoryContract.CategoryEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateCategory(category);
        }
        return id;
    }

    /**
     * @param category_id long
     * @return Category
     */
    public Category getCategory(long category_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + CategoryContract.CategoryEntry.TABLE_NAME + " WHERE "
                + CategoryContract.CategoryEntry._ID + " = " + category_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }


        Category cat = new Category();
        cat.setItemId(c.getInt(c.getColumnIndex(CategoryContract.CategoryEntry._ID)));
        cat.setItemName(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME)));
        cat.setItemImage(Integer.parseInt(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE))));

        return cat;
    }

    /**
     * @return List<Category>
     */
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + CategoryContract.CategoryEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setItemId(c.getInt((c.getColumnIndex(CategoryContract.CategoryEntry._ID))));
                cat.setItemName((c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME))));
                cat.setItemImage(Integer.parseInt(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE))));

                categories.add(cat);
            } while (c.moveToNext());
        }

        return categories;
    }

    /**
     * @param category Category
     * @return int
     */
    public int updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_NAME, category.getItemName());
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE, category.getItemImage());

        // updating row
        return db.update(CategoryContract.CategoryEntry.TABLE_NAME, values, CategoryContract.CategoryEntry._ID + " = ?",
                new String[] { String.valueOf(category.getItemId()) });
    }

    /**
     * @param category_id long
     */
    public void deleteCategory(long category_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CategoryContract.CategoryEntry.TABLE_NAME, CategoryContract.CategoryEntry._ID + " = ?",
                new String[] { String.valueOf(category_id) });
    }


    public long createProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry._ID, product.getId());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_IMAGE, product.getThumbnail());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_DESC, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.getOriginalPrice());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_CAT, product.getCategory().getItemId());

        // insert row

        int id = (int) db.insertWithOnConflict(ProductContract.ProductEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateProduct(product);
        }
        return id;
    }

    /**
     * @param product_id long
     * @return Category
     */
    public Product getProduct(long product_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + ProductContract.ProductEntry.TABLE_NAME + " WHERE "
                + ProductContract.ProductEntry._ID + " = " + product_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }

        Product prod = new Product();
        prod.setId(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID)));
        prod.setName(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_NAME)));
        prod.setDescription(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_DESC)));
        prod.setThumbnail(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
        prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
        prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));

        return prod;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + ProductContract.ProductEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Product prod = new Product();
                prod.setId(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID)));
                prod.setName(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_NAME)));
                prod.setDescription(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_DESC)));
                prod.setThumbnail(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
                prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
                prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));

                products.add(prod);
            } while (c.moveToNext());
        }

        return products;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_IMAGE, product.getThumbnail());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_DESC, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.getOriginalPrice());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_CAT, product.getCategory().getItemId());

        // updating row
        return db.update(ProductContract.ProductEntry.TABLE_NAME, values, ProductContract.ProductEntry._ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }
}
