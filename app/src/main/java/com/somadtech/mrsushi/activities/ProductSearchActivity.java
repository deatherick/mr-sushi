package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.adapters.ProductsAdapter;
import com.somadtech.mrsushi.entities.Banner;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Ingredient;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.helpers.Utils;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private ProductsAdapter adapter;
    private List<Product> productList;
    MrSushiDbHelper mDbHelper;
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;
    ImageView img_banner;
    LinearLayout layout_banner;
    private int mNotificationsCount = 0;
    final int NAV_LOCATIONS = 1000001;
    final int NAV_SETTINGS = 1000002;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        mDbHelper = new MrSushiDbHelper(this);

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


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        productList =  new ArrayList<>();

        layout_banner = (LinearLayout) findViewById(R.id.layout_banner);
        img_banner = (ImageView) findViewById(R.id.img_banner);



        setBanner();

        adapter = new ProductsAdapter(this, productList);

        int CARD_VIEW_COLUMNS;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            CARD_VIEW_COLUMNS = 3;
        } else {
            CARD_VIEW_COLUMNS = 2;
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, CARD_VIEW_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(CARD_VIEW_COLUMNS, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        new FetchCountTask().execute();
    }

    private void setBanner(){
        if(mDbHelper.getAllBanners().size() > 0){
            final Banner banner = mDbHelper.getRandomBanner();
            Picasso.with(this)
                    .load(banner.getImage())
                    .placeholder(R.drawable.bebidas)
                    .error(R.drawable.bebidas)
                    .into(img_banner);
            if(banner.getTarget().getId() != 0){
                img_banner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ProductSearchActivity.this, ProductDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("product_id", banner.getTarget().getId());
                        startActivity(intent);
                    }
                });
            }
        } else {
            layout_banner.setVisibility(View.GONE);
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
        else if(id == NAV_LOCATIONS ){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("fragment_id", 2);
            startActivity(intent);
        }
        else if(id == NAV_SETTINGS ){
            Intent intent = new Intent(this, SettingsActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_cart){
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
//            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchMenuItem.setVisible(true);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        int category_id = getIntent().getIntExtra("category_id", 1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Buscador de productos");

        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productList = mDbHelper.getAllProducts();
                final List<Product> filteredModelList = filter(productList, query);
                adapter = new ProductsAdapter(getBaseContext(), productList);
                adapter.setFilter(filteredModelList);

                recyclerView.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("") || newText == null){
                    adapter = new ProductsAdapter(getBaseContext(), new ArrayList<Product>());
                    recyclerView.setAdapter(adapter);
                    return false;
                }

                return true;
            }

        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setItemsVisibility(menu, item, false);
            }
        });
        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //setItemsVisibility(menu, item, true);
                return false;
            }
        });
        return true;
    }


    private List<Product> filter(List<Product> models, String query) {
        query = query.toLowerCase();
        final List<Product> filteredModelList = new ArrayList<>();
        for (Product model : models) {
            String ingr_text = "";
            for (Ingredient ingredient : model.getIngredients()) {
                ingr_text += " " + ingredient.getName().toLowerCase();
            }
            final String text = model.getName().toLowerCase() + ingr_text;
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    /*
  Updates the count of notifications in the ActionBar.
  */
    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    /*
    Sample AsyncTask to fetch the notifications count
    */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            mNotificationsCount = mDbHelper.getCountCart();
            return mNotificationsCount;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }
}
