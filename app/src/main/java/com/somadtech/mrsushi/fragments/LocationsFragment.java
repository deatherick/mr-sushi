package com.somadtech.mrsushi.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.somadtech.mrsushi.MainActivity;
import com.somadtech.mrsushi.R;
import com.somadtech.mrsushi.activities.ProductListActivity;
import com.somadtech.mrsushi.adapters.LocationsAdapter;
import com.somadtech.mrsushi.adapters.ProductsAdapter;
import com.somadtech.mrsushi.entities.Category;
import com.somadtech.mrsushi.entities.Location;
import com.somadtech.mrsushi.entities.Product;
import com.somadtech.mrsushi.schemes.MrSushiDbHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView mRecyclerView;
    private LocationsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    MrSushiDbHelper mDbHelper;
    List<Location> locations;


    private OnFragmentInteractionListener mListener;

    public LocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationsFragment newInstance(String param1, String param2) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new MrSushiDbHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        locations = mDbHelper.getAllLocations();

        // specify an adapter (see also next example)
        mAdapter = new LocationsAdapter(getContext(), locations);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchMenuItem.setVisible(true);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.location_toolbar);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(((MainActivity) getContext()).getSupportActionBar().getThemedContext());

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                final List<Location> filteredModelList = filter(locations, newText);

                mAdapter.setFilter(filteredModelList);

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
    }
    private List<Location> filter(List<Location> models, String query) {
        query = query.toLowerCase();
        final List<Location> filteredModelList = new ArrayList<>();
        for (Location model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
