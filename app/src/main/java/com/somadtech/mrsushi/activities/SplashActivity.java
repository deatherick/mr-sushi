package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.somadtech.mrsushi.entities.Banner;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Ingredient;
import com.somadtech.mrsushi.entities.Location;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Promotion;
import com.somadtech.mrsushi.entities.Variant;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper;
    public static final String PREFS_NAME = "configuraciones";

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


        //@TODO usar la variable location para obtener los productos de esa ubicacion
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String location = settings.getString("location", "");

        // Create a new map of values, where column names are the keys
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MainActivity", response);
                        Gson gson = new Gson();
                        try {
                            JSONObject obj = new JSONObject(response);

                            mDbHelper.truncatePivotTables();

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

                            Type promotionsListType = new TypeToken<ArrayList<Promotion>>(){}.getType();
                            List<Promotion> promotionListObject = gson.fromJson(obj.getJSONArray("Promotions").toString(), promotionsListType);
                            for (Promotion promotion: promotionListObject) {
                                mDbHelper.createPromotion(promotion);
                            }

                            Type bannersListType = new TypeToken<ArrayList<Banner>>(){}.getType();
                            List<Banner> bannerListObject = gson.fromJson(obj.getJSONArray("Banners").toString(), bannersListType);
                            for (Banner banner: bannerListObject) {
                                mDbHelper.createBanner(banner);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
