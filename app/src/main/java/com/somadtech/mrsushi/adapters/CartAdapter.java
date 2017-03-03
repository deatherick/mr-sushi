package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.somadtech.mrsushi.entities.Ingredient;
import com.somadtech.mrsushi.helpers.CartClickListener;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Cart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.somadtech.mrsushi.R.id.cart_label_img;
import static com.somadtech.mrsushi.R.id.cart_minus_img;
import static com.somadtech.mrsushi.R.id.cart_plus_img;
import static com.somadtech.mrsushi.R.id.cart_product_quantity;
import static com.somadtech.mrsushi.R.id.cart_remove_img;
import static com.somadtech.mrsushi.R.id.from_name;
import static com.somadtech.mrsushi.R.id.list_image;
import static com.somadtech.mrsushi.R.id.plist_observation_text;
import static com.somadtech.mrsushi.R.id.variant_name;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class CartAdapter extends ArrayAdapter<Cart> {

    public ArrayList<Cart> cartArrayList;
    public ArrayList<Cart> orig;
    public Context myContext;
    private CartClickListener mCartClickListener;

    public CartAdapter(Context context, ArrayList<Cart> cart_rows) {
        super(context, 0, cart_rows);
        try {
            mCartClickListener = (CartClickListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context + " must implements CartClickListener");
        }
        this.cartArrayList = cart_rows;
        this.myContext = context;
    }

    private class CartHolder
    {
        TextView from_name;
        TextView variant_name;
        TextView plist_observation_text;
        TextView cart_product_quantity;
        ImageView list_image;
        ImageView cart_plus_img;
        ImageView cart_minus_img;
        ImageView cart_remove_img;
        ImageView cart_label_img;
    }

    @Override
    public int getCount() {
        return cartArrayList.size();
    }

    @Override
    public Cart getItem(int position) {
        return cartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Category obj = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CartHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_list_row, parent, false);
            holder = new CartHolder();
            holder.from_name = (TextView) convertView.findViewById(from_name);
            holder.variant_name = (TextView) convertView.findViewById(variant_name);
            holder.plist_observation_text = (TextView) convertView.findViewById(plist_observation_text);
            holder.cart_product_quantity = (TextView) convertView.findViewById(cart_product_quantity);
            holder.list_image = (ImageView) convertView.findViewById(list_image);
            holder.cart_plus_img = (ImageView) convertView.findViewById(cart_plus_img);
            holder.cart_minus_img = (ImageView) convertView.findViewById(cart_minus_img);
            holder.cart_remove_img = (ImageView) convertView.findViewById(cart_remove_img);
            holder.cart_label_img = (ImageView) convertView.findViewById(cart_label_img);
            convertView.setTag(holder);
            //TextView txtCatName = (TextView) convertView.findViewById(txtCatName);
        } else {
            holder = (CartHolder) convertView.getTag();
        }
        // Lookup view for data population
        holder.from_name.setText(cartArrayList.get(position).getProduct().getName());
        holder.variant_name.setText(cartArrayList.get(position).getVariant().getName());
        holder.plist_observation_text.setText(cartArrayList.get(position).getObservations());
        holder.cart_product_quantity.setText(String.valueOf(cartArrayList.get(position).getQuantity()));
        Picasso.with(myContext)
                .load(cartArrayList.get(position).getProduct().getThumbnail())
                .placeholder(R.drawable.sopas)
                .error(R.drawable.sopas)
                .into(holder.list_image);
        holder.cart_plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCartClickListener.onPlusClickListener(cartArrayList.get(position).getId());
            }
        });
        holder.cart_minus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCartClickListener.onMinusClickListener(cartArrayList.get(position).getId());
            }
        });
        holder.cart_remove_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCartClickListener.onRemoveClickListener(cartArrayList.get(position).getId());
            }
        });

        Cart cart = (Cart) getItem(position);
        assert cart != null;
        if(cart.getPromotion_product() == 1){
            holder.cart_label_img.setVisibility(View.VISIBLE);
            holder.cart_minus_img.setVisibility(View.GONE);
            holder.cart_plus_img.setVisibility(View.GONE);
            holder.cart_product_quantity.setVisibility(View.GONE);
        } else {
            holder.cart_label_img.setVisibility(View.GONE);
            holder.cart_minus_img.setVisibility(View.VISIBLE);
            holder.cart_plus_img.setVisibility(View.VISIBLE);
            holder.cart_product_quantity.setVisibility(View.VISIBLE);
        }
        // Populate the data into the template view using the data object

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Cart> results = new ArrayList<>();
                if (orig == null)
                    orig = cartArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Cart g : orig) {
                            String ingr_text = "";
                            for (Ingredient ingredient : g.getProduct().getIngredients()) {
                                ingr_text += " " + ingredient.getName().toLowerCase();
                            }
                            final String text = g.getProduct().getName().toLowerCase() + ingr_text;
                            if (text.contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cartArrayList = (ArrayList<Cart>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
