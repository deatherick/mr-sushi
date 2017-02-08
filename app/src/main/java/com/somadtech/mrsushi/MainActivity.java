package com.somadtech.mrsushi;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.fragments.CategoriesFragment;
import com.somadtech.mrsushi.schemes.CategoryContract;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_categories) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            CategoriesFragment firstFragment = new CategoriesFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, firstFragment)
                    .commit();
        }


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

        String url ="http://www.tedimedia.com/backoffice/api.locations.php";

        // Create a new map of values, where column names are the keys
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("MainActivity", response);
                        int[] covers = new int[]{
                                R.drawable.ensaldas_entradas,
                                R.drawable.sopas,
                                R.drawable.sushi,
                                R.drawable.combos,
                                R.drawable.cocina_caliente,
                                R.drawable.extras,
                                R.drawable.postres,
                                R.drawable.bebidas};

                        // Do something with the response


                        // Insert the new row, returning the primary key value of the new row
                        long newRowId = mDbHelper.createCategory(new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createCategory(new Category(2, "Sopas", covers[1]));
                        mDbHelper.createCategory(new Category(3, "Barra Sushi", covers[2]));
                        mDbHelper.createCategory(new Category(4, "Combos", covers[3]));
                        mDbHelper.createCategory(new Category(5, "Cocina Caliente", covers[4]));
                        mDbHelper.createCategory(new Category(6, "Extras", covers[5]));
                        mDbHelper.createCategory(new Category(7, "Postres", covers[6]));
                        mDbHelper.createCategory(new Category(8, "Bebidas", covers[7]));
                        Log.i("Row insercion", String.valueOf(newRowId));


                        int[] product_covers = new int[]{
                                R.drawable.image1,
                                R.drawable.image2,
                                R.drawable.image3,
                                R.drawable.image4,
                                R.drawable.image5,
                                R.drawable.image1,
                                R.drawable.image2,
                                R.drawable.image3,
                                R.drawable.image4,
                                R.drawable.image5,
                                R.drawable.image1};

                        Product a = new Product(1, "Calamares Tempura", 45, product_covers[0], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(2, "Manchego Maki", 52, product_covers[1], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(3, "Kani Roll", 58, product_covers[2], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(4, "Lotto Roll", 65, product_covers[3], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(5, "Meshi Ebi Maki", 74, product_covers[4], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(6, "Calamares Tempura", 45, product_covers[5], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(7, "Manchego Maki", 52, product_covers[6], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(8, "Kani Roll", 58, product_covers[7], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(9, "Lotto Roll", 65, product_covers[8], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        a = new Product(10, "Meshi Ebi Maki", 74, product_covers[9], new Category(1, "Ensaladas y entradas", covers[0]));
                        mDbHelper.createProduct(a);

                        Gson gson = new Gson();
                        String json = gson.toJson(mDbHelper.getProduct(1));

                        Log.i("producto 1: ", json);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    @Override
    protected void onDestroy() {
        mDbHelper.closeDB();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                //super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (findViewById(R.id.fragment_categories) == null) {
                // Create a new Fragment to be placed in the activity layout
                CategoriesFragment firstFragment = new CategoriesFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                firstFragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, firstFragment)
                        .commit();
            }

            // Handle the camera action
        }
       /* else if (id == R.id.nav_gallery) {

        }
        else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
