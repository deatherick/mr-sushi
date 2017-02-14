package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.somadtech.mrsushi.helpers.CartClickListener;
import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.helpers.Utils;
import com.somadtech.mrsushi.adapters.CartAdapter;
import com.somadtech.mrsushi.entities.Cart;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CartClickListener {

    CartAdapter adapter;
    ListView mListView;
    MrSushiDbHelper mDbHelper;
    private int mNotificationsCount = 0;
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //super.onCreate();
        mDbHelper = new MrSushiDbHelper(this);
        adapter = new CartAdapter(this, mDbHelper.getAllCart());

        updateListView(adapter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button btn_empty_cart = (Button) findViewById(R.id.btn_empty_cart);

        btn_empty_cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDbHelper.emptyCart();
                adapter = new CartAdapter(CartActivity.this, mDbHelper.getAllCart());

                updateListView(adapter);
            }
        });
    }

    private void updateListView(CartAdapter adapter){
        mListView = (ListView) findViewById(R.id.cart_list);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("CartActivity", "popping backstack");
                fm.popBackStack();
            } else {
                Log.i("CartActivity", "nothing on backstack, calling super");
                //super.onBackPressed();
            }
        }
    }

    @Override
    public void onMinusClickListener(int id) {
        Cart cart = mDbHelper.getCart(id);
        if(cart.getQuantity() > 1){
            mDbHelper.removeProductOfCart(id);
            adapter = new CartAdapter(this, mDbHelper.getAllCart());
            updateListView(adapter);
        }
    }

    @Override
    public void onPlusClickListener(int id) {
        mDbHelper.addProductToCart(id);
        adapter = new CartAdapter(this, mDbHelper.getAllCart());
        updateListView(adapter);
    }

    @Override
    public void onRemoveClickListener(int id) {
        mDbHelper.deleteCart(id);
        adapter = new CartAdapter(this, mDbHelper.getAllCart());
        updateListView(adapter);
    }
}
