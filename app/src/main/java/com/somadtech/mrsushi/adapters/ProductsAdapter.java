package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, count;
        ImageView thumbnail, overflow;
        Button button_product_detail;
        Product product;

        MyViewHolder(View view) {
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("product_id", product.getId());
                    mContext.startActivity(intent);
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
}
