package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.activity.PriceTableActivity;
import com.mobilophilia.mydairy.activity.UpdateEntry;
import com.mobilophilia.mydairy.adapter.CustomerListAdapter;
import com.mobilophilia.mydairy.adapter.EntriesListByCodeAdapter;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.RecyclerItemClickListener;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.MyEntries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukesh on 04/08/17.
 */

public class EntriesByCode extends Fragment {

    private List<MyEntries> myEntries = new ArrayList<>();
    private RecyclerView recyclerView;
    private EntriesListByCodeAdapter mAdapter;
    private DBHelper dbHelper;
    int id;

    public EntriesByCode() {
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

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("ID");
        }

        myEntries = dbHelper.getListOfEntriesByCode(id);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_entry_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EntriesListByCodeAdapter(myEntries);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (myEntries.get(position).getContentType() != 0) {
                            Intent priceIntent = new Intent(getActivity(), UpdateEntry.class);
                            priceIntent.putExtra(Constants.RECORD_ID, myEntries.get(position).getId());
                            startActivityForResult(priceIntent, 300);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (myEntries.get(position).getContentType() != 0) {
                            launchMessageDialog(myEntries.get(position).getId());
                        }
                    }
                })
        );

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        myEntries.clear();
        myEntries = dbHelper.getListOfEntriesByCode(id);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EntriesListByCodeAdapter(myEntries);
        recyclerView.setAdapter(mAdapter);
    }

    public void launchMessageDialog(final int id) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_update_delete_price_set);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCancelable(true);

        TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText(Constants.DIALOG_MSG_DELETE_ENTRY);

        Button btnNo = (Button) dialog.findViewById(R.id.btn_update);
        btnNo.setText(Constants.DIALOG_BUTTON_TEXT_NO);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnYes = (Button) dialog.findViewById(R.id.btn_delete);
        btnYes.setText(Constants.DIALOG_BUTTON_TEXT_YES);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteEntry(id);
                Toast.makeText(getActivity(), Constants.TOAST_MSG_SUCCES_ENTRY_DELETE, Toast.LENGTH_LONG).show();
                myEntries.clear();
                myEntries = dbHelper.getListOfEntriesByCode(id);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new EntriesListByCodeAdapter(myEntries);
                recyclerView.setAdapter(mAdapter);

                dialog.dismiss();
            }
        });
        dialog.show();
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
