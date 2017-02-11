package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.activities.ProductDetailActivity;
import com.somadtech.mrsushi.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smt on 1/02/17.
 * Project Name: Mrsushi
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public Button button_product_detail;
        public Product product;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            button_product_detail = (Button) view.findViewById(R.id.button_product_detail);
            button_product_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("product_id", product.getId());
                    mContext.startActivity(intent);
                    //showPopup(view, product);
                }
            });
        }
    }


    public ProductsAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getName());
        holder.count.setText("Q" + product.getOriginalPrice());
        holder.product = product;
        Picasso.with(mContext)
                .load(productList.get(position).getThumbnail())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(holder.thumbnail);
        // loading product cover using Glide library
        //Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setFilter(List<Product> countryModels) {
        productList = new ArrayList<>();
        productList.addAll(countryModels);
        notifyDataSetChanged();
    }


    public void showPopup(View view, Product product) {
        View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_product_detail, null);

//        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
//        int width = display.widthPixels;
//        int height = display.heightPixels;



        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button btnDismiss = (Button) popupView.findViewById(R.id.listen_button);
        TextView txtName = (TextView) popupView.findViewById(R.id.txtName);
        TextView txtDesc = (TextView) popupView.findViewById(R.id.txtDescription);

        txtName.setText(product.getName());
        txtDesc.setText(product.getDescription());
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//        TypedValue tv = new TypedValue();
//        int actionBarHeight = 0;
//        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//        {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,mContext.getResources().getDisplayMetrics());
//        }
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//        popupWindow.setWidth(width-64);
        //popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    popupWindow.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
        popupWindow.showAsDropDown(popupView, 0, 0);
//        RelativeLayout rl = (RelativeLayout)popupView.findViewById(R.id.relative_layout);
//        rl.setAlpha(0.5F);
    }

}
