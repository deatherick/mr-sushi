package com.somadtech.mrsushi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.entities.ObjectItem;

import java.util.ArrayList;

import static com.somadtech.mrsushi.R.id.txtCatName;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class CategoryAdapter extends ArrayAdapter<ObjectItem> {

    public ArrayList<ObjectItem> employeeArrayList;
    public ArrayList<ObjectItem> orig;

    public CategoryAdapter(Context context, ArrayList<ObjectItem> users) {
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
    public ObjectItem getItem(int position) {
        return employeeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //ObjectItem obj = getItem(position);
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
        holder.name.setText(employeeArrayList.get(position).getName());
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
                final ArrayList<ObjectItem> results = new ArrayList<ObjectItem>();
                if (orig == null)
                    orig = employeeArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final ObjectItem g : orig) {
                            if (g.getName().toLowerCase()
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
                employeeArrayList = (ArrayList<ObjectItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
