package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Location;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

public class LocationsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;
    MrSushiDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDbHelper = new MrSushiDbHelper(this);

        try {
            Picasso.with(this)
                    .load(R.drawable.staticmap)
                    .placeholder(R.drawable.staticmap)
                    .error(R.drawable.staticmap)
                    .into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ubicaciones");

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_locations ){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment_id", 2);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ProductListActivity.class);
            intent.putExtra("category_id", getCategoryId(item));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(LocationsActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
