package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.MySingleton;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Ingredient;
import com.somadtech.mrsushi.entities.Location;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Variant;
import com.somadtech.mrsushi.entities.Promotion;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button button = (Button) findViewById(R.id.button2);
        mDbHelper = new MrSushiDbHelper(this);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getBulkData();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public boolean getBulkData(){

        // Gets the data repository in write mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        RequestQueue mRequestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        // Start the queue
        mRequestQueue.start();

//        String url ="http://192.168.1.15:3000/db";
        String url = "http://vardhost.com/mrsushi/api";

        // Create a new map of values, where column names are the keys
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MainActivity", response);
                        Gson gson = new Gson();
                        try {
                            JSONObject obj = new JSONObject(response);

                            Type categoryListType = new TypeToken<ArrayList<Category>>(){}.getType();
                            List<Category> categoriesListObject = gson.fromJson(obj.getJSONArray("Categories").toString(), categoryListType);
                            for (Category category: categoriesListObject) {
                                mDbHelper.createCategory(category);
                            }

                            Type variantListType = new TypeToken<ArrayList<Variant>>(){}.getType();
                            List<Variant> variantsListObject = gson.fromJson(obj.getJSONArray("Variants").toString(), variantListType);
                            for (Variant variant: variantsListObject) {
                                mDbHelper.createVariant(variant);
                            }

                            Type productListType = new TypeToken<ArrayList<Product>>(){}.getType();
                            List<Product> productsListObject = gson.fromJson(obj.getJSONArray("Products").toString(), productListType);
                            for (Product product: productsListObject) {
                                mDbHelper.createProduct(product);
                            }

                            Type ingredientListType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
                            List<Ingredient> ingredientsListObject = gson.fromJson(obj.getJSONArray("Ingredients").toString(), ingredientListType);
                            for (Ingredient ingredient: ingredientsListObject) {
                                mDbHelper.createIngredient(ingredient);
                            }

                            Type locationListType = new TypeToken<ArrayList<Location>>(){}.getType();
                            List<Location> locationListObject = gson.fromJson(obj.getJSONArray("Locations").toString(), locationListType);
                            for (Location location: locationListObject) {
                                mDbHelper.createLocation(location);
                            }



//                            Type promotionListType = new TypeToken<ArrayList<Promotion>>(){}.getType();
//                            List<Promotion> promotionsListObject = gson.fromJson(obj.getJSONArray("Promotions").toString(), promotionListType);
//                            for (Promotion promotion: promotionsListObject) {
//                                mDbHelper.createPromotion(promotion);
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] covers = new String[]{
                                "http://logok.org/wp-content/uploads/2014/04/Apple-Logo-black.png"};

//                        mDbHelper.createIngredient(new Ingredient(1, "Atun", "atun", covers[0]));
//                        mDbHelper.createIngredient(new Ingredient(2, "Pescado", "pescado", covers[0]));
//                        mDbHelper.createIngredient(new Ingredient(3, "Aguacate con aguacate y mas", "aguacate", covers[0]));
//                        mDbHelper.createIngredient(new Ingredient(4, "Pulpo", "pulpo", covers[0]));
//
//                        mDbHelper.createVariant(new Variant(1, 1, "Camarón", 51, covers[0]));
//                        mDbHelper.createVariant(new Variant(2, 1, "Cangrego", 51, covers[0]));
//                        mDbHelper.createVariant(new Variant(3, 1, "Salmón", 67, covers[0]));
//                        mDbHelper.createVariant(new Variant(4, 1, "Vegetariano", 45, covers[0]));

                        // Insert the new row, returning the primary key value of the new row
//                        long newRowId = mDbHelper.createCategory(new Category(1, "Ensaladas y entradas", covers[0]));
//                        mDbHelper.createCategory(new Category(2, "Sopas", covers[0]));
//                        mDbHelper.createCategory(new Category(3, "Barra Sushi", covers[0]));
//                        mDbHelper.createCategory(new Category(4, "Combos", covers[0]));
//                        mDbHelper.createCategory(new Category(5, "Cocina Caliente", covers[0]));
//                        mDbHelper.createCategory(new Category(6, "Extras", covers[0]));
//                        mDbHelper.createCategory(new Category(7, "Postres", covers[0]));
//                        mDbHelper.createCategory(new Category(8, "Bebidas", covers[0]));
//                        mDbHelper.createCategory(new Category(0, "Test", covers[0]));
//                        Log.i("Row insercion", String.valueOf(newRowId));
//
//
//                        String[] product_covers = new String[]{
//                                "https://s3-media4.fl.yelpcdn.com/bphoto/4fWgAz_uHWfhvB0OiS4OVA/348s.jpg"};
//                        String json = gson.toJson(mDbHelper.getProduct(1));
//
//                        Log.i("producto 1: ", json);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Error: ", error.getMessage());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        return true;
    }

}
