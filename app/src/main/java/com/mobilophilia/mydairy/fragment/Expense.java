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
import com.mobilophilia.mydairy.common.Util;


/**
 * Created by yogen on 13-07-2017.
 */

public class Expense extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView tabAddEntry;
    private TextView tabEntries;

    public Expense() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_expenses, container, false);


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
            fr = new AddExpense();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            tabAddEntry.setTextColor(getResources().getColor(R.color.white));
            tabEntries.setTextColor(getResources().getColor(R.color.black_overlay));
            tabAddEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_left_blue));
            tabEntries.setBackgroundDrawable(getResources().getDrawable(R.drawable.circular_right_white));
        } else {
            fr = new ExpenseList();
            Util.hideKeyboard(getActivity());
            fragmentTransaction.addToBackStack("ExpenseList");
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
