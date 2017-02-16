package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

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
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.MySingleton;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Button button = (Button) findViewById(R.id.button2);

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

        String url ="http://192.168.1.145:3000/db";

        // Create a new map of values, where column names are the keys
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MainActivity", response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JavaType type = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Product.class);
                            ArrayList<Product> products = objectMapper.readValue(obj.getJSONArray("Products").toString(), type);
                            for (Product product: products) {
                                mDbHelper.createProduct(product);
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        String[] covers = new String[]{
                                "http://logok.org/wp-content/uploads/2014/04/Apple-Logo-black.png"};

                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = mDbHelper.createCategory(new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createCategory(new Category(2, "Sopas", covers[0]));
                        mDbHelper.createCategory(new Category(3, "Barra Sushi", covers[0]));
                        mDbHelper.createCategory(new Category(4, "Combos", covers[0]));
                        mDbHelper.createCategory(new Category(5, "Cocina Caliente", covers[0]));
                        mDbHelper.createCategory(new Category(6, "Extras", covers[0]));
                        mDbHelper.createCategory(new Category(7, "Postres", covers[0]));
                        mDbHelper.createCategory(new Category(8, "Bebidas", covers[0]));
                        Log.i("Row insercion", String.valueOf(newRowId));


                        String[] product_covers = new String[]{
                                "https://s3-media4.fl.yelpcdn.com/bphoto/4fWgAz_uHWfhvB0OiS4OVA/348s.jpg"};
                        Gson gson = new Gson();
                        String json = gson.toJson(mDbHelper.getProduct(1));

                        Log.i("producto 1: ", json);

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
