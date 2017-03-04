package com.somadtech.mrsushi.activities;

import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.somadtech.mrsushi.adapters.PromotionsViewPagerAdapter;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.entities.Promotion;
import com.somadtech.mrsushi.fragments.EmptyCartDialogFragment;
import com.somadtech.mrsushi.helpers.CartClickListener;
import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.helpers.Utils;
import com.somadtech.mrsushi.adapters.CartAdapter;
import com.somadtech.mrsushi.entities.Cart;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EmptyCartDialogFragment.NoticeDialogListener, CartClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {

    CartAdapter adapter;
    ListView mListView;
    MrSushiDbHelper mDbHelper;
    private int mNotificationsCount = 0;
    private ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawer;
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private PromotionsViewPagerAdapter mAdapter;
    private List<Promotion> promotion_list;
    final int NAV_LOCATIONS = 1000001;
    final int NAV_SETTINGS = 1000002;

    private int[] mImageResources = {
            R.drawable.sopas,
            R.drawable.cocina_caliente,
            R.drawable.combos,
            R.drawable.postres,
            R.drawable.bebidas
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mDbHelper = new MrSushiDbHelper(this);
        ArrayList<Cart> cart_rows = mDbHelper.getAllCart();
        ArrayList<Product> product_rows = new ArrayList<>();

        for (Cart cart: cart_rows) {
            product_rows.add(cart.getProduct());
        }
        promotion_list = mDbHelper.getPromotionsByProductsTrigger(product_rows);

        setReference();
        //super.onCreate();

        adapter = new CartAdapter(this, cart_rows);

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

        Button btn_empty_cart = (Button) findViewById(R.id.btn_empty_cart);

        btn_empty_cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showNoticeDialog();

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

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());

        MenuItemCompat.setShowAsAction(searchItem, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(searchItem, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem mCartItem = menu.findItem(R.id.action_cart);
        mCartItem.setVisible(false);

        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchMenuItem.setVisible(true);
        //SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cart_toolbar);

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
        else if(id == R.id.nav_search_product ){
            Intent intent = new Intent(this, ProductSearchActivity.class);
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

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        startActivity(intent);
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
        Cart cart = mDbHelper.getCart(id);
        if(cart.getPromotion_target() != 0){
            Cart newRow = mDbHelper.checkProductInCart(cart.getPromotion_target(), 0);
            if(newRow != null){
                mDbHelper.deleteCart(newRow.getId());
            }
        }
        mDbHelper.deleteCart(id);
        adapter = new CartAdapter(this, mDbHelper.getAllCart());
        updateListView(adapter);
        Toast.makeText(getBaseContext(), R.string.product_removed, Toast.LENGTH_SHORT).show();
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new EmptyCartDialogFragment();
        dialog.show(getSupportFragmentManager(), "EmptyCartDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        mDbHelper.emptyCart();
        adapter = new CartAdapter(CartActivity.this, mDbHelper.getAllCart());

        updateListView(adapter);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }


    //@Override
    public void setReference() {
        //view = LayoutInflater.from(this).inflate(R.layout.activity_cart, container);

        intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        mAdapter = new PromotionsViewPagerAdapter(CartActivity.this, promotion_list);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        if(dotsCount > 0){
            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            final int pos = i;
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            dots[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dots[pos].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    intro_images.setCurrentItem(pos);
                }
            });
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
