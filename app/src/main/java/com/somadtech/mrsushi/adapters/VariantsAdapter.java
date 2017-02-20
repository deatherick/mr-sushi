package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Variant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smt on 1/02/17.
 * Project Name: Mrsushi
 */

public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Variant> variantList;
    public int mSelectedItem = 0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Variant variant;
        RadioButton radioButton;

        MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtVariantName);
            price = (TextView) view.findViewById(R.id.txtVariantPrice);
            radioButton = (RadioButton) view.findViewById(R.id.radioVariant);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyItemRangeChanged(0, variantList.size());
                }
            };
            itemView.setOnClickListener(clickListener);
            radioButton.setOnClickListener(clickListener);
        }
    }

    public VariantsAdapter(Context mContext, List<Variant> variantList) {
        this.mContext = mContext;
        this.variantList = variantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.variant_box, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Variant ingredient = variantList.get(position);
        holder.name.setText(ingredient.getName());
        holder.price.setText("Q." + ingredient.getPrice());
        holder.variant = ingredient;

        holder.radioButton.setChecked(position == mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return variantList.size();
    }

    public void setFilter(List<Variant> ingredientModels) {
        variantList = new ArrayList<>();
        variantList.addAll(ingredientModels);
        notifyDataSetChanged();
    }

    public Variant getItem(int position) {
        return variantList.get(position);
    }
}
