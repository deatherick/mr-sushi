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
    private List<Ingredient> ingredientList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView thumbnail;
        Ingredient ingredient;

        MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtIngredientName);
            thumbnail = (ImageView) view.findViewById(R.id.imgIngredient);
        }
    }

    public IngredientsAdapter(Context mContext, List<Ingredient> ingredientList) {
        this.mContext = mContext;
        this.ingredientList = ingredientList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_box, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.name.setText(ingredient.getName());
        holder.ingredient = ingredient;
        Picasso.with(mContext)
                .load(ingredientList.get(position).getImage())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void setFilter(List<Ingredient> ingredientModels) {
        ingredientList = new ArrayList<>();
        ingredientList.addAll(ingredientModels);
        notifyDataSetChanged();
    }
}
