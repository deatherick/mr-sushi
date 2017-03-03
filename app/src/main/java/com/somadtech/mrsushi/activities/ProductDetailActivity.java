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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.adapters.IngredientsAdapter;
import com.somadtech.mrsushi.adapters.VariantsAdapter;
import com.somadtech.mrsushi.entities.Cart;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Variant;
import com.somadtech.mrsushi.helpers.Utils;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MrSushiDbHelper mDbHelper = new MrSushiDbHelper(this);
    TextView txtName;
    TextView txtDesc;
    EditText observations_text;
    ImageView imgProduct;
    Product product;
    Variant variant;
    private IngredientsAdapter adapter;
    private VariantsAdapter adapter_variant;
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;
    private int mNotificationsCount = 0;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewVariants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        int  product_id = getIntent().getIntExtra("product_id", 1);



        product = mDbHelper.getProduct(product_id);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewVariants = (RecyclerView) findViewById(R.id.recycler_view_variants);
        //variant = new Variant();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(product.getName());
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtName = (TextView) findViewById(R.id.txtName);
        txtDesc = (TextView) findViewById(R.id.txtDescription);
        imgProduct = (ImageView) findViewById(R.id.product_image);

        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        Picasso.with(this)
                .load(product.getFull_image())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(imgProduct);

        Button button = (Button) findViewById(R.id.add_to_cart);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                observations_text = (EditText) findViewById(R.id.observations_text);
                String observations = observations_text.getText().toString();
                variant = adapter_variant.getItem(adapter_variant.mSelectedItem);
                Cart cart = new Cart(product, variant, observations);
                mDbHelper.createCart(cart);
                Toast.makeText(getBaseContext(), R.string.product_added, Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        adapter = new IngredientsAdapter(this, product.getIngredients());
        adapter_variant =  new VariantsAdapter(this, product.getVariants());


        int INGREDIENT_COLUMNS;
        int VARIANT_COLUMNS;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            INGREDIENT_COLUMNS = 3;
            VARIANT_COLUMNS = 4;
        } else {
            INGREDIENT_COLUMNS = 2;
            VARIANT_COLUMNS = 2;
        }

        if(adapter.getItemCount() != 0){
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, INGREDIENT_COLUMNS);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(INGREDIENT_COLUMNS, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } else {
            LinearLayout txt_ingredient_layout = (LinearLayout) findViewById(R.id.card_qualification_layout_id);
            txt_ingredient_layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }


        if(adapter_variant.getItemCount() != 0){
            RecyclerView.LayoutManager mLayoutManagerVariant = new GridLayoutManager(this, VARIANT_COLUMNS);
            recyclerViewVariants.setLayoutManager(mLayoutManagerVariant);
            recyclerViewVariants.addItemDecoration(new GridSpacingItemDecoration(VARIANT_COLUMNS, dpToPx(10), true));
            recyclerViewVariants.setItemAnimator(new DefaultItemAnimator());
            recyclerViewVariants.setAdapter(adapter_variant);
        } else {
            LinearLayout txt_variant_layout = (LinearLayout) findViewById(R.id.txt_variant_layout);
            txt_variant_layout.setVisibility(View.GONE);
            recyclerViewVariants.setVisibility(View.GONE);
        }

        new FetchCountTask().execute();
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();

        // Update LayerDrawable's BadgeDrawable
        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
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
Updates the count of notifications in the ActionBar.
*/
    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        //getActivity().invalidateOptionsMenu();
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
