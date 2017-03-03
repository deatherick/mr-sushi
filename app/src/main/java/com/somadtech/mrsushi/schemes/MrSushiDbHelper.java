package com.somadtech.mrsushi.schemes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.somadtech.mrsushi.entities.Banner;
import com.somadtech.mrsushi.entities.Cart;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Ingredient;
import com.somadtech.mrsushi.entities.Location;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Promotion;
import com.somadtech.mrsushi.entities.Variant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        db.execSQL(CartContract.SQL_CREATE_CART);
        db.execSQL(CategoryContract.SQL_CREATE_CATEGORIES);
        db.execSQL(IngredientContract.SQL_CREATE_INGREDIENTS);
        db.execSQL(LocationContract.SQL_CREATE_LOCATIONS);
        db.execSQL(ProductContract.SQL_CREATE_PRODUCTS);
        db.execSQL(PromotionContract.SQL_CREATE_PROMOTIONS);
        db.execSQL(VariantContract.SQL_CREATE_VARIANTS);
        db.execSQL(BannerContract.SQL_CREATE_BANNERS);

        // Pivot tables
        db.execSQL(IngredientProductContract.SQL_CREATE_INGREDIENT_PRODUCT);
        db.execSQL(ProductPromotionsContract.SQL_CREATE_PRODUCT_PROMOTIONS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CartContract.SQL_DELETE_CART);
        db.execSQL(CategoryContract.SQL_DELETE_CATEGORIES);
        db.execSQL(IngredientContract.SQL_DELETE_INGREDIENTS);
        db.execSQL(LocationContract.SQL_DELETE_LOCATIONS);
        db.execSQL(ProductContract.SQL_DELETE_PRODUCTS);
        db.execSQL(PromotionContract.SQL_DELETE_PROMOTIONS);
        db.execSQL(VariantContract.SQL_DELETE_VARIANTS);
        db.execSQL(BannerContract.SQL_DELETE_BANNERS);

        //Pivot tables
        db.execSQL(IngredientProductContract.SQL_DELETE_INGREDIENT_PRODUCT);
        db.execSQL(ProductPromotionsContract.SQL_DELETE_PRODUCT_PROMOTIONS);
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

    //region Cart
    /**
     * @param cart Cart
     * @return int
     */
    public long createCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        Product product = cart.getProduct();
        Variant variant = cart.getVariant();

        ContentValues values = new ContentValues();
        values.put(CartContract.CartEntry.COLUMN_NAME_PRODUCT, product.getId());
        values.put(CartContract.CartEntry.COLUMN_NAME_VARIANT, variant.getId());
        values.put(CartContract.CartEntry.COLUMN_NAME_OBSERVATIONS, cart.getObservations());
        values.put(CartContract.CartEntry.COLUMN_NAME_ORDER, cart.getOrder_id());
        values.put(CartContract.CartEntry.COLUMN_NAME_QTY, cart.getQuantity());
        values.put(CartContract.CartEntry.COLUMN_NAME_PROM_PROD, cart.getPromotion_product());
        values.put(CartContract.CartEntry.COLUMN_NAME_STATE, cart.getState());

        // insert rowste
        int id;
        if(cart.getObservations().equals("") && cart.getPromotion_product() == 0){
            Cart productInCart = checkProductInCart(product.getId(), variant.getId());
            if(productInCart != null){
                id = addProductToCart(productInCart.getId());
            } else {
                id = (int) db.insertWithOnConflict(CartContract.CartEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } else {
            id = (int) db.insertWithOnConflict(CartContract.CartEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        return id;
    }

    public int addProductToCart(long cart_id){
        Cart cart = getCart(cart_id);
        cart.setQuantity(cart.getQuantity() + 1);
        return updateCart(cart);
    }

    public int removeProductOfCart(long cart_id){
        Cart cart = getCart(cart_id);
        cart.setQuantity(cart.getQuantity() - 1);
        return updateCart(cart);
    }

    private boolean checkPromotionAlreadyTaken(Promotion promotion){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + CartContract.CartEntry.TABLE_NAME + " WHERE "
                + CartContract.CartEntry.COLUMN_NAME_PRODUCT + " = " + promotion.getTarget().get(0).getId() + " AND "
                + CartContract.CartEntry.COLUMN_NAME_PROM_PROD + " = " + 1;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    private Cart checkProductInCart(long product_id, int variant_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CartContract.CartEntry.TABLE_NAME + " WHERE "
                + CartContract.CartEntry.COLUMN_NAME_PRODUCT + " = " + product_id + " AND "
                + CartContract.CartEntry.COLUMN_NAME_VARIANT + " = " + variant_id + " AND "
                + CartContract.CartEntry.COLUMN_NAME_PROM_PROD + " = " + 0;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return null;
        }

        Cart cart = new Cart();
        cart.setId(c.getInt(c.getColumnIndex(CartContract.CartEntry._ID)));
        cart.setProduct(getProduct(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PRODUCT))));
        cart.setVariant(getVariant(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_VARIANT))));
        cart.setOrder_id(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_ORDER)));
        cart.setObservations(c.getString(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_OBSERVATIONS)));
        cart.setQuantity(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QTY)));
        cart.setPromotion_product(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PROM_PROD)));
        cart.setState(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_STATE)));

        return cart;
    }



    /**
     * @param cart_id long
     * @return Cart
     */
    public Cart getCart(long cart_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CartContract.CartEntry.TABLE_NAME + " WHERE "
                + CartContract.CartEntry._ID + " = " + cart_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }


        Cart cart = new Cart();
        cart.setId(c.getInt(c.getColumnIndex(CartContract.CartEntry._ID)));
        cart.setProduct(getProduct(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PRODUCT))));
        cart.setVariant(getVariant(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_VARIANT))));
        cart.setOrder_id(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_ORDER)));
        cart.setObservations(c.getString(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_OBSERVATIONS)));
        cart.setQuantity(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QTY)));
        cart.setPromotion_product(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PROM_PROD)));
        cart.setState(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_STATE)));

        return cart;
    }

    /**
     * @return ArrayList<Cart>
     */
    public ArrayList<Cart> getAllCart() {
        ArrayList<Cart> carts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CartContract.CartEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setId(c.getInt(c.getColumnIndex(CartContract.CartEntry._ID)));
                cart.setProduct(getProduct(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PRODUCT))));
                cart.setVariant(getVariant(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_VARIANT))));
                cart.setOrder_id(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_ORDER)));
                cart.setObservations(c.getString(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_OBSERVATIONS)));
                cart.setQuantity(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_QTY)));
                cart.setPromotion_product(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_PROM_PROD)));
                cart.setState(c.getInt(c.getColumnIndex(CartContract.CartEntry.COLUMN_NAME_STATE)));

                carts.add(cart);
            } while (c.moveToNext());
        }

        return carts;
    }

    public int getCountCart(){
        ArrayList<Cart> carts = getAllCart();
        if(carts.size() == 0){
            return 0;
        }
        int contador = 0;
        for (Cart cart: carts) {
            contador += cart.getQuantity();
        }
        return contador;
    }

    /**
     * @param cart Cart
     * @return int
     */
    public int updateCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        Product product = cart.getProduct();
        Variant variant = cart.getVariant();

        ContentValues values = new ContentValues();
        values.put(CartContract.CartEntry.COLUMN_NAME_PRODUCT, product.getId());
        values.put(CartContract.CartEntry.COLUMN_NAME_VARIANT, variant.getId());
        values.put(CartContract.CartEntry.COLUMN_NAME_OBSERVATIONS, cart.getObservations());
        values.put(CartContract.CartEntry.COLUMN_NAME_ORDER, cart.getOrder_id());
        values.put(CartContract.CartEntry.COLUMN_NAME_QTY, cart.getQuantity());
        values.put(CartContract.CartEntry.COLUMN_NAME_PROM_PROD, cart.getPromotion_product());
        values.put(CartContract.CartEntry.COLUMN_NAME_STATE, cart.getState());

        // updating row
        return db.update(CartContract.CartEntry.TABLE_NAME, values, CartContract.CartEntry._ID + " = ?",
                new String[] { String.valueOf(cart.getId()) });
    }

    /**
     * @param cart_id long
     */
    public void deleteCart(long cart_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CartContract.CartEntry.TABLE_NAME, CartContract.CartEntry._ID + " = ?",
                new String[] { String.valueOf(cart_id) });
    }

    public void emptyCart(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ CartContract.CartEntry.TABLE_NAME);
    }
    //endregion

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
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_SLUG, category.getSlug());

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

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Category();
        }


        Category cat = new Category();
        cat.setItemId(c.getInt(c.getColumnIndex(CategoryContract.CategoryEntry._ID)));
        cat.setItemName(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME)));
        cat.setItemImage(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE)));
        cat.setSlug(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_SLUG)));

        return cat;
    }

    /**
     * @param category_slug String
     * @return long
     */
    public int getCategoryId(String category_slug) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_NAME + " WHERE "
                + CategoryContract.CategoryEntry.COLUMN_NAME_SLUG + " LIKE '%" + category_slug + "%'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return 0;
        }


        Category cat = new Category();
        cat.setItemId(c.getInt(c.getColumnIndex(CategoryContract.CategoryEntry._ID)));
        cat.setItemName(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_NAME)));
        cat.setItemImage(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_IMAGE)));
        cat.setSlug(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_SLUG)));

        return cat.getItemId();
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
                cat.setSlug(c.getString(c.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_NAME_SLUG)));

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
        values.put(CategoryContract.CategoryEntry.COLUMN_NAME_SLUG, category.getSlug());

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
        values.put(ProductContract.ProductEntry.COLUMN_NAME_THUMB, product.getThumbnail());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_IMAGE, product.getFull_image());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_DESC, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.getOriginalPrice());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_CAT, product.getCategory().getItemId());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, product.getSlug());
        createIngredientsByProduct(product, product.getIngredients());
        createVariantsByProduct(product, product.getVariants());
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

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Product();
        }

        Product prod = new Product();
        prod.setId(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID)));
        prod.setName(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_NAME)));
        prod.setDescription(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_DESC)));
        prod.setFull_image(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
        prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
        prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
        prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));
        prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_THUMB)));
        prod.setSlug(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_SLUG)));
        prod.setIngredients(getIngredientsByProduct(prod.getId()));

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
                prod.setFull_image(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
                prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
                prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
                prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));
                prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_THUMB)));
                prod.setSlug(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_SLUG)));
                prod.setIngredients(getIngredientsByProduct(prod.getId()));

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
                prod.setFull_image(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_IMAGE)));
                prod.setOriginalPrice(Double.parseDouble(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_PRICE))));
                prod.setCategory(getCategory(c.getInt(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_CAT))));
                prod.setVariants(getVariantsByProduct(c.getInt(c.getColumnIndex(ProductContract.ProductEntry._ID))));
                prod.setThumbnail(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_THUMB)));
                prod.setSlug(c.getString(c.getColumnIndex(ProductContract.ProductEntry.COLUMN_NAME_SLUG)));
                prod.setIngredients(getIngredientsByProduct(prod.getId()));

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
        values.put(ProductContract.ProductEntry._ID, product.getId());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_THUMB, product.getThumbnail());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_IMAGE, product.getFull_image());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_DESC, product.getDescription());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_PRICE, product.getOriginalPrice());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_CAT, product.getCategory().getItemId());
        values.put(ProductContract.ProductEntry.COLUMN_NAME_SLUG, product.getSlug());
        createIngredientsByProduct(product, product.getIngredients());
        createVariantsByProduct(product, product.getVariants());

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
        values.put(VariantContract.VariantEntry.COLUMN_NAME_SLUG, variant.getSlug());

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
        values.put(VariantContract.VariantEntry.COLUMN_NAME_SLUG, variant.getSlug());

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
                variant.setSlug(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_SLUG)));

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
                variant.setSlug(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_SLUG)));

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

        if (c != null  && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Variant();
        }

        Variant variant = new Variant();
        variant.setId(c.getInt((c.getColumnIndex(VariantContract.VariantEntry._ID))));
        variant.setName(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_NAME)));
        variant.setPrice(Double.parseDouble(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PRICE))));
        variant.setProduct_id(c.getInt(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_PR_ID)));
        variant.setSlug(c.getString(c.getColumnIndex(VariantContract.VariantEntry.COLUMN_NAME_SLUG)));

        return variant;
    }

    public long createVariantsByProduct(Product product, List<Variant> variants) {
        for (Variant variant: variants) {
            variant.setProduct_id(product.getId());
            createVariant(variant);
        }

        return 1;
    }
    //endregion

    //region Pivot

    public void truncatePivotTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(IngredientProductContract.SQL_TRUNCATE_INGREDIENT_PRODUCT);
        db.execSQL(ProductPromotionsContract.SQL_TRUNCATE_PRODUCT_PROMOTIONS);
    }

    public long createIngredientsByProduct(Product product, List<Ingredient> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Ingredient ingredient: ingredients) {
            ContentValues values = new ContentValues();
            values.put(IngredientProductContract.IngredientProductEntry.COLUMN_NAME_INGREDIENT_ID, ingredient.getId());
            values.put(IngredientProductContract.IngredientProductEntry.COLUMN_NAME_PRODUCT_ID, product.getId());
            db.insertWithOnConflict(IngredientProductContract.IngredientProductEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }

        return 1;
    }

    public int createProductPromotions(Promotion promotion){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Product> triggers = promotion.getTrigger();

        List<Product> targets = promotion.getTarget();

        for (Product product: triggers) {
            ContentValues values = new ContentValues();
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_TYPE, ProductPromotionsContract.ProductPromotionsEntry.TRIGGER);
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID, promotion.getId());
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID, product.getId());
            db.insertWithOnConflict(ProductPromotionsContract.ProductPromotionsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }

        for (Product product: targets) {
            ContentValues values = new ContentValues();
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_TYPE, ProductPromotionsContract.ProductPromotionsEntry.TARGET);
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID, promotion.getId());
            values.put(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID, product.getId());
            db.insertWithOnConflict(ProductPromotionsContract.ProductPromotionsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        return 1;
    }

    public List<Ingredient> getIngredientsByProduct(int product_id){
        ArrayList<Cart> carts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + IngredientProductContract.IngredientProductEntry.TABLE_NAME + " WHERE "
                + IngredientProductContract.IngredientProductEntry.COLUMN_NAME_PRODUCT_ID + " = " + product_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        List<Ingredient> ingredients = new ArrayList<>();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int ingredient_id = c.getInt(c.getColumnIndex(IngredientProductContract.IngredientProductEntry.COLUMN_NAME_INGREDIENT_ID));
                Ingredient ingredient = getIngredient(ingredient_id);
                ingredients.add(ingredient);
            } while (c.moveToNext());
        }

        return ingredients;
    }

    protected String getFilter(List<Product> products){
        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();

        //Looping through the list
        for ( int i = 0; i< products.size(); i++){
            //append the value into the builder
            commaSepValueBuilder.append(products.get(i).getId());

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if ( i != products.size()-1){
                commaSepValueBuilder.append(", ");
            }
        }
        return commaSepValueBuilder.toString();


    }

    public List<Promotion> getPromotionsByProductsTrigger(List<Product> products){
        List<Promotion> promotions = new ArrayList<>();
        if(products.size() == 0){
            return promotions;
        }
        String filter = getFilter(products);

        String selectQuery = "SELECT DISTINCT "+ ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID +" FROM " +
                ProductPromotionsContract.ProductPromotionsEntry.TABLE_NAME + " WHERE " +
                ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID +" IN ("+ filter +")";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int promotion_id = c.getInt(c.getColumnIndex(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID));
                Promotion promotion = getPromotion(promotion_id);
                if(!checkPromotionAlreadyTaken(promotion)){
                    promotions.add(promotion);
                }
            } while (c.moveToNext());
        }

        return promotions;
    }

    public List<Product> getProductsTargetByPromotion(int promotion_id){
        ArrayList<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ProductPromotionsContract.ProductPromotionsEntry.TABLE_NAME + " WHERE " +
                ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PROMOTION_ID + " = " + promotion_id + " AND " +
                ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_TYPE + " = '" + ProductPromotionsContract.ProductPromotionsEntry.TARGET+"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                int product_id = c.getInt((c.getColumnIndex(ProductPromotionsContract.ProductPromotionsEntry.COLUMN_NAME_PRODUCT_ID)));
                Product product = getProduct(product_id);
                products.add(product);
            } while (c.moveToNext());
        }
        return products;
    }

    //endregion

    //region Ingredients

    public Ingredient getIngredient(int ingredient_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + IngredientContract.IngredientEntry.TABLE_NAME + " WHERE "
                + IngredientContract.IngredientEntry._ID + " = " + ingredient_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Ingredient();
        }


        Ingredient ingredient = new Ingredient();
        ingredient.setId(c.getInt(c.getColumnIndex(IngredientContract.IngredientEntry._ID)));
        ingredient.setName(c.getString(c.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_NAME_NAME)));
        ingredient.setSlug(c.getString(c.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_NAME_SLUG)));
        ingredient.setImage(c.getString(c.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_NAME_IMAGE)));

        return ingredient;
    }

    public int createIngredient(Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IngredientContract.IngredientEntry._ID, ingredient.getId());
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_NAME, ingredient.getName());
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_IMAGE, ingredient.getImage());
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_SLUG, ingredient.getSlug());

        // insert row

        int id = (int) db.insertWithOnConflict(IngredientContract.IngredientEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateIngredient(ingredient);
        }
        return id;
    }

    public int updateIngredient(Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_NAME, ingredient.getName());
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_IMAGE, ingredient.getImage());
        values.put(IngredientContract.IngredientEntry.COLUMN_NAME_SLUG, ingredient.getSlug());

        // updating row
        return db.update(IngredientContract.IngredientEntry.TABLE_NAME, values, IngredientContract.IngredientEntry._ID + " = ?",
                new String[] { String.valueOf(ingredient.getId()) });
    }

    //endregion

    //region Locations

    public Location getLocation(int location_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + LocationContract.LocationEntry.TABLE_NAME + " WHERE "
                + LocationContract.LocationEntry._ID + " = " + location_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Location();
        }


        Location location = new Location();
        location.setId(c.getInt(c.getColumnIndex(LocationContract.LocationEntry._ID)));
        location.setName(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_NAME)));
        location.setDescription(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_DESC)));
        location.setSlug(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_SLUG)));
        location.setLatitude(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_LAT)));
        location.setLongitude(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_LNG)));
        location.setMap_image(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_MAP_IMAGE)));
        location.setImage(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_IMAGE)));


        return location;
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + LocationContract.LocationEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(c.getInt(c.getColumnIndex(LocationContract.LocationEntry._ID)));
                location.setName(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_NAME)));
                location.setDescription(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_DESC)));
                location.setSlug(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_SLUG)));
                location.setLatitude(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_LAT)));
                location.setLongitude(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_LNG)));
                location.setMap_image(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_MAP_IMAGE)));
                location.setImage(c.getString(c.getColumnIndex(LocationContract.LocationEntry.COLUMN_NAME_IMAGE)));

                locations.add(location);
            } while (c.moveToNext());
        }

        return locations;
    }

    public int createLocation(Location location){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry._ID, location.getId());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_NAME, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_DESC, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_SLUG, location.getSlug());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LAT, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LNG, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_MAP_IMAGE, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_IMAGE, location.getImage());
        // insert row

        int id = (int) db.insertWithOnConflict(LocationContract.LocationEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateLocation(location);
        }
        return id;
    }

    public int updateLocation(Location location){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.COLUMN_NAME_NAME, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_DESC, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_SLUG, location.getSlug());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LAT, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_LNG, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_MAP_IMAGE, location.getName());
        values.put(LocationContract.LocationEntry.COLUMN_NAME_IMAGE, location.getImage());

        // updating row
        return db.update(LocationContract.LocationEntry.TABLE_NAME, values, LocationContract.LocationEntry._ID + " = ?",
                new String[] { String.valueOf(location.getId()) });
    }

    //endregion

    //region Promotions

    public Promotion getPromotion(int promotion_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + PromotionContract.PromotionEntry.TABLE_NAME + " WHERE "
                + PromotionContract.PromotionEntry._ID + " = " + promotion_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Promotion();
        }


        Promotion promotion = new Promotion();
        promotion.setId(c.getInt(c.getColumnIndex(PromotionContract.PromotionEntry._ID)));
        promotion.setName(c.getString(c.getColumnIndex(PromotionContract.PromotionEntry.COLUMN_NAME_NAME)));
        promotion.setSlug(c.getString(c.getColumnIndex(PromotionContract.PromotionEntry.COLUMN_NAME_SLUG)));
        promotion.setDescription(c.getString(c.getColumnIndex(PromotionContract.PromotionEntry.COLUMN_NAME_DESC)));
        promotion.setImage_small(c.getString(c.getColumnIndex(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_S)));
        promotion.setImage_large(c.getString(c.getColumnIndex(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_L)));
        promotion.setTarget(getProductsTargetByPromotion(promotion_id));

        return promotion;
    }

    public int createPromotion(Promotion promotion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PromotionContract.PromotionEntry._ID, promotion.getId());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_NAME, promotion.getName());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_SLUG, promotion.getSlug());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_DESC, promotion.getDescription());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_S, promotion.getImage_small());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_L, promotion.getImage_large());

        // insert row

        int id = (int) db.insertWithOnConflict(PromotionContract.PromotionEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updatePromotion(promotion);
        }
        promotion.setId(id);
        createProductPromotions(promotion);
        return id;
    }

    public int updatePromotion(Promotion promotion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_NAME, promotion.getName());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_SLUG, promotion.getSlug());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_DESC, promotion.getDescription());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_S, promotion.getImage_small());
        values.put(PromotionContract.PromotionEntry.COLUMN_NAME_IMAGE_L, promotion.getImage_large());
        // updating row
        return db.update(PromotionContract.PromotionEntry.TABLE_NAME, values, PromotionContract.PromotionEntry._ID + " = ?",
                new String[] { String.valueOf(promotion.getId()) });
    }

    //endregion

    //region Banners
    /**
     * @param banner Banner
     * @return int
     */
    public long createBanner(Banner banner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BannerContract.BannerEntry._ID, banner.getId());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_NAME, banner.getName());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_SLUG, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_DESC, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_IMAGE, banner.getImage());


        // insert row

        int id = (int) db.insertWithOnConflict(BannerContract.BannerEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateBanner(banner);
        }
        return id;
    }

    /**
     * @param banner_id long
     * @return Category
     */
    public Banner getBanner(int banner_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + BannerContract.BannerEntry.TABLE_NAME + " WHERE "
                + BannerContract.BannerEntry._ID + " = " + banner_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Banner();
        }


        Banner banner = new Banner();
        banner.setId(c.getInt((c.getColumnIndex(BannerContract.BannerEntry._ID))));
        banner.setName((c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_NAME))));
        banner.setSlug(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_SLUG)));
        banner.setDescription(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_DESC)));
        banner.setImage(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_IMAGE)));

        return banner;
    }

    /**
     * @return ArrayList<Category>
     */
    public ArrayList<Banner> getAllBanners() {
        ArrayList<Banner> banners = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BannerContract.BannerEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Banner banner = new Banner();
                banner.setId(c.getInt((c.getColumnIndex(BannerContract.BannerEntry._ID))));
                banner.setName((c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_NAME))));
                banner.setSlug(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_SLUG)));
                banner.setDescription(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_DESC)));
                banner.setImage(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_IMAGE)));


                banners.add(banner);
            } while (c.moveToNext());
        }

        return banners;
    }

    /**
     * @return ArrayList<Category>
     */
    public Banner getRandomBanner() {
        ArrayList<Banner> banners = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BannerContract.BannerEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Banner banner = new Banner();
                banner.setId(c.getInt((c.getColumnIndex(BannerContract.BannerEntry._ID))));
                banner.setName((c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_NAME))));
                banner.setSlug(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_SLUG)));
                banner.setDescription(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_DESC)));
                banner.setImage(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_IMAGE)));


                banners.add(banner);
            } while (c.moveToNext());
        }
        Random rand = new Random();

        int  n = rand.nextInt(banners.size()) ;
        return banners.get(n);
    }

    /**
     * @param banner Banner
     * @return int
     */
    public int updateBanner(Banner banner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BannerContract.BannerEntry.COLUMN_NAME_NAME, banner.getName());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_SLUG, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_DESC, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_IMAGE, banner.getImage());

        // updating row
        return db.update(BannerContract.BannerEntry.TABLE_NAME, values, BannerContract.BannerEntry._ID + " = ?",
                new String[] { String.valueOf(banner.getId()) });
    }

    /**
     * @param banner_id int
     */
    public void deleteBanner(int banner_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BannerContract.BannerEntry.TABLE_NAME, BannerContract.BannerEntry._ID + " = ?",
                new String[] { String.valueOf(banner_id) });
    }
    //endregion

    //region Configurations
    /**
     * @param banner Banner
     * @return int
     */
    public long setConfiguration(Banner banner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BannerContract.BannerEntry._ID, banner.getId());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_NAME, banner.getName());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_SLUG, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_DESC, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_IMAGE, banner.getImage());


        // insert row

        int id = (int) db.insertWithOnConflict(BannerContract.BannerEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            id = updateBanner(banner);
        }
        return id;
    }

    /**
     * @param banner_id long
     * @return Category
     */
    public Banner getConfiguration(int banner_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + BannerContract.BannerEntry.TABLE_NAME + " WHERE "
                + BannerContract.BannerEntry._ID + " = " + banner_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0){
            c.moveToFirst();
        } else {
            return new Banner();
        }


        Banner banner = new Banner();
        banner.setId(c.getInt((c.getColumnIndex(BannerContract.BannerEntry._ID))));
        banner.setName((c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_NAME))));
        banner.setSlug(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_SLUG)));
        banner.setDescription(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_DESC)));
        banner.setImage(c.getString(c.getColumnIndex(BannerContract.BannerEntry.COLUMN_NAME_IMAGE)));

        return banner;
    }

    /**
     * @param banner Banner
     * @return int
     */
    public int updateConfiguration(Banner banner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BannerContract.BannerEntry.COLUMN_NAME_NAME, banner.getName());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_SLUG, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_DESC, banner.getSlug());
        values.put(BannerContract.BannerEntry.COLUMN_NAME_IMAGE, banner.getImage());

        // updating row
        return db.update(BannerContract.BannerEntry.TABLE_NAME, values, BannerContract.BannerEntry._ID + " = ?",
                new String[] { String.valueOf(banner.getId()) });
    }

    //endregion
}