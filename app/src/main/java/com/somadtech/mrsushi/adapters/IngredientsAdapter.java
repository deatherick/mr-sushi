package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smt on 1/02/17.
 * Project Name: Mrsushi
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Ingredient> productList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;
        //Button button_product_detail;
        Ingredient product;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public IngredientsAdapter(Context mContext, List<Ingredient> productList) {
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
        Ingredient product = productList.get(position);
        holder.title.setText(product.getName());
        holder.product = product;
        Picasso.with(mContext)
                .load(productList.get(position).getImage())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setFilter(List<Ingredient> countryModels) {
        productList = new ArrayList<>();
        productList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
