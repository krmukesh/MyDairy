package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;

/**
 * Created by mukesh on 20/08/17.
 */

public class MenuReport extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView tabAddEntry;
    private TextView tabEntries;

    public MenuReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_report, container, false);


        tabAddEntry = (TextView) rootView.findViewById(R.id.tab_add_entry);
        tabAddEntry.setOnClickListener(this);
        tabEntries = (TextView) rootView.findViewById(R.id.tab_my_entry);
        tabEntries.setOnClickListener(this);
        selectFrag(0);

        return rootView;
    }


    public void selectFrag(int tab) {
        Fragment fr;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (tab == 0) {
            fr = new ReportFragment();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            tabAddEntry.setTextColor(getResources().getColor(R.color.white));
            tabEntries.setTextColor(getResources().getColor(R.color.black_overlay));
            tabAddEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_left_blue));
            tabEntries.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_right_white));
        } else {
            fr = new DownloadList();
            fragmentTransaction.addToBackStack("DownloadList");
            tabAddEntry.setTextColor(getResources().getColor(R.color.black_overlay));
            tabEntries.setTextColor(getResources().getColor(R.color.white));
            tabAddEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_left_white));
            tabEntries.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_right_blue));
        }
        fragmentTransaction.replace(R.id.fragment_place, fr);
        fragmentTransaction.commit();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.tab_add_entry:
                selectFrag(0);
                break;
            case R.id.tab_my_entry:
                selectFrag(1);
                break;
            default:
                break;
        }
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
