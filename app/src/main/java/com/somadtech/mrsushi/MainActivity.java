package com.somadtech.mrsushi;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.somadtech.mrsushi.activities.CartActivity;
import com.somadtech.mrsushi.activities.ProductListActivity;
import com.somadtech.mrsushi.activities.ProductSearchActivity;
import com.somadtech.mrsushi.activities.SettingsActivity;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.fragments.CategoriesFragment;
import com.somadtech.mrsushi.fragments.LocationsFragment;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationsFragment.OnFragmentInteractionListener {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;

    final int NAV_LOCATIONS = 1000001;
    final int NAV_SETTINGS = 1000002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        ArrayList<Category> categories = mDbHelper.getAllCategories();
        Menu menu = navigationView.getMenu();
        for (Category category: categories) {
            menu.add(Menu.NONE, category.getItemId(), Menu.NONE, category.getItemName())
                    .setTitleCondensed(category.getSlug())
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        menu.add(Menu.NONE, NAV_LOCATIONS, Menu.NONE, "Ubicaciones")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(Menu.NONE, NAV_SETTINGS, Menu.NONE, "Ajustes")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        navigationView.setNavigationItemSelectedListener(this);
        int  fragment_id = getIntent().getIntExtra("fragment_id", 1);
        onSectionAttached(fragment_id);
        invalidateOptionsMenu();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_cart){
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            onSectionAttached(1);
        }
        else if(id == R.id.nav_search_product ){
            Intent intent = new Intent(this, ProductSearchActivity.class);
            startActivity(intent);
        }
        else if(id == NAV_LOCATIONS ){
            onSectionAttached(2);
        }
        else if(id == NAV_SETTINGS ){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra("category_id", getCategoryId(item));
            startActivity(intent);
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

    int getCategoryId(MenuItem item){
        String category_slug = item.getTitleCondensed().toString();
        if (!category_slug.equals("")) {
            int category_id = mDbHelper.getCategoryId(category_slug);
            return category_id;
        } else {
            return 1;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void onSectionAttached(int number) {
        // update the main content by replacing fragments
        Fragment fragment;
        switch (number) {
            case 1:
                fragment = new CategoriesFragment();
                break;
            case 2:
                fragment = new LocationsFragment();
                break;
            default:
                fragment = new CategoriesFragment();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
