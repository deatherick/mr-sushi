package com.somadtech.mrsushi.schemes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Variant;

import java.util.ArrayList;

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
        db.execSQL(VariantContract.SQL_CREATE_VARIANTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CategoryContract.SQL_DELETE_CATEGORIES);
        db.execSQL(ProductContract.SQL_DELETE_PRODUCTS);
        db.execSQL(VariantContract.SQL_DELETE_VARIANTS);
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

    //region Categories
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

        String selectQuery = "SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_NAME + " WHERE "
                + CategoryContract.CategoryEntry._ID + " = " + category_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }


        Category cat = new Category();
        cat.setItemId(c.getInt(c.getColumnIndex(CategoryContract.CategoryEntry._ID)));
        cat.setItemName(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME)));
        cat.setItemImage(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE)));

        return cat;
    }

    /**
     * @return ArrayList<Category>
     */
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        String selectQuery = "SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Category cat = new Category();
                cat.setItemId(c.getInt((c.getColumnIndex(CategoryContract.CategoryEntry._ID))));
                cat.setItemName((c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME))));
                cat.setItemImage(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE)));

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
    //endregion

    //region Products
    /**
     * @param product Product
     * @return long
     */
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
     * @return Product
     */
    public Product getProduct(long product_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ProductContract.ProductEntry.TABLE_NAME + " WHERE "
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
        prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
        prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
        prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
        prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));

        return prod;
    }

    /**
     * @return ArrayList<Product>
     */
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
                prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
                prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
                prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
                prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));

                products.add(prod);
            } while (c.moveToNext());
        }

        return products;
    }

    /**
     * @return ArrayList<Product>
     */
    public ArrayList<Product> getProductsByCategory(long category_id) {
        ArrayList<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + ProductContract.ProductEntry.TABLE_NAME+ " WHERE "
                + ProductContract.ProductEntry.COLUMN_NAME_CAT + " = " + category_id;

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
                prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
                prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
                prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
                prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));

                products.add(prod);
            } while (c.moveToNext());
        }

        return products;
    }

    /**
     * @param product Product
     * @return int
     */
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
    //endregion

    //region Variants
    /**
     * @param variant Variant
     * @return long
     */
    public long createVariant(Variant variant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VariantContract.VariantEntry._ID, variant.getId());
        values.put(VariantContract.VariantEntry.COLUMN_NAME_NAME, variant.getName());
        values.put(VariantContract.VariantEntry.COLUMN_NAME_PRICE, variant.getPrice());
        values.put(VariantContract.VariantEntry.COLUMN_NAME_PR_ID, variant.getProduct_id());

        // insert row

        int id = (int) db.insertWithOnConflict(VariantContract.VariantEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateVariant(variant);
        }
        return id;
    }

    /**
     * @param variant Variant
     * @return int
     */
    public int updateVariant(Variant variant) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VariantContract.VariantEntry.COLUMN_NAME_NAME, variant.getName());
        values.put(VariantContract.VariantEntry.COLUMN_NAME_PRICE, variant.getPrice());
        values.put(VariantContract.VariantEntry.COLUMN_NAME_PR_ID, variant.getProduct_id());

        // updating row
        return db.update(VariantContract.VariantEntry.TABLE_NAME, values, VariantContract.VariantEntry._ID + " = ?",
                new String[] { String.valueOf(variant.getId()) });
    }

    /**
     * @return ArrayList<Variant>
     */
    public ArrayList<Variant> getAllVariants() {
        ArrayList<Variant> variants = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + VariantContract.VariantEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Variant variant = new Variant();
                variant.setId(c.getInt((c.getColumnIndex(VariantContract.VariantEntry._ID))));
                variant.setName(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_NAME)));
                variant.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PRICE))));
                variant.setProduct_id(c.getInt(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PR_ID)));

                variants.add(variant);
            } while (c.moveToNext());
        }

        return variants;
    }

    /**
     * @return ArrayList<Variant>
     */
    public ArrayList<Variant> getVariantsByProduct(long product_id) {
        ArrayList<Variant> variants = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + VariantContract.VariantEntry.TABLE_NAME + " WHERE "
                + VariantContract.VariantEntry.COLUMN_NAME_PR_ID + " = " + product_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Variant variant = new Variant();
                variant.setId(c.getInt((c.getColumnIndex(VariantContract.VariantEntry._ID))));
                variant.setName(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_NAME)));
                variant.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PRICE))));
                variant.setProduct_id(c.getInt(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PR_ID)));

                variants.add(variant);
            } while (c.moveToNext());
        }

        return variants;
    }

    /**
     * @param variant_id long
     * @return Variant
     */
    public Variant getVariant(long variant_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + VariantContract.VariantEntry.TABLE_NAME + " WHERE "
                + VariantContract.VariantEntry._ID + " = " + variant_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }

        Variant variant = new Variant();
        variant.setId(c.getInt((c.getColumnIndex(VariantContract.VariantEntry._ID))));
        variant.setName(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_NAME)));
        variant.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PRICE))));
        variant.setProduct_id(c.getInt(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PR_ID)));

        return variant;
    }
    //endregion
}