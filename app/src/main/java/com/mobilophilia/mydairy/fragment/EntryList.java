package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.adapter.EntryListAdapter;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.RecyclerItemClickListener;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.MyEntries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanji on 8/2/2017.
 */

public class EntryList extends Fragment {

    private List<MyEntries> myEntries = new ArrayList<>();
    private RecyclerView recyclerView;
    private EntryListAdapter mAdapter;
    private DBHelper dbHelper;

    public EntryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entry_list, container, false);

        dbHelper = new DBHelper(getActivity());
        myEntries.clear();
        myEntries = dbHelper.getAllEntries();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_entry_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EntryListAdapter(myEntries);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Fragment fr = new EntriesByCode();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", myEntries.get(position).getCode());
                        fr.setArguments(bundle);
                        fragmentTransaction.addToBackStack("EntriesByCode");
                        fragmentTransaction.replace(R.id.fragment_place, fr);
                        fragmentTransaction.commit();

                        Log.d("position", "positionposition " + position);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("onLongItemClick", "onLongItemClick " + position);
                    }
                })
        );

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}