package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.Category;

import java.util.ArrayList;

import static com.somadtech.mrsushi.R.id.txtCatName;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    public ArrayList<Category> employeeArrayList;
    public ArrayList<Category> orig;

    public CategoryAdapter(Context context, ArrayList<Category> users) {
        super(context, 0, users);
        this.employeeArrayList = users;
    }

    private class CategoryHolder
    {
        TextView name;
        TextView age;
    }

    @Override
    public int getCount() {
        return employeeArrayList.size();
    }

    @Override
    public Category getItem(int position) {
        return employeeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Category obj = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CategoryHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
            holder = new CategoryHolder();
            holder.name = (TextView) convertView.findViewById(txtCatName);
            convertView.setTag(holder);
            //TextView txtCatName = (TextView) convertView.findViewById(txtCatName);
        } else {
            holder = (CategoryHolder) convertView.getTag();
        }
        // Lookup view for data population
        holder.name.setText(employeeArrayList.get(position).getItemName());
        holder.name.setBackgroundResource(employeeArrayList.get(position).getItemImage());
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
                final ArrayList<Category> results = new ArrayList<Category>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Category g : orig) {
                            if (g.getItemName().toLowerCase()
                                    .contains(constraint.toString()))
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
                employeeArrayList = (ArrayList<Category>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
