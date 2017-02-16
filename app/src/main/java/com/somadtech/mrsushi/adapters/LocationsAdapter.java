package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.activities.LocationsActivity;
import com.somadtech.mrsushi.entities.Product;

import java.util.List;

/**
 * Created by smt on 2/15/17.
 * Project: mrsushi-android
 */

public class LocationsAdapter  extends RecyclerView.Adapter<LocationsAdapter.ViewHolder>  {

    private List<Product> productList;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public Button btn_loc_detail;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.location_name);
            btn_loc_detail = (Button) view.findViewById(R.id.btn_loc_detail);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LocationsAdapter(Context mContext, List<Product> productList) {
        this.productList = productList;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LocationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_row, parent, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(productList.get(position).getName());
        holder.btn_loc_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LocationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mContext.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return productList.size();
    }
}
