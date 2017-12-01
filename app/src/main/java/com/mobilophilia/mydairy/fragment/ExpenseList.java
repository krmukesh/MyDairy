package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.mobilophilia.mydairy.activity.UpdateExpense;
import com.mobilophilia.mydairy.activity.UpdateName;
import com.mobilophilia.mydairy.adapter.CustomerListAdapter;
import com.mobilophilia.mydairy.adapter.ExpenseAdapter;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.RecyclerItemClickListener;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.ExpenseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mukesh on 16/08/17.
 */

public class ExpenseList extends Fragment {

    private List<ExpenseBean> expenseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExpenseAdapter mAdapter;
    private DBHelper dbHelper;

    public ExpenseList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expenses_list, container, false);

        dbHelper = new DBHelper(getActivity());
        expenseList.clear();
        expenseList = dbHelper.getAllExpenses();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_entry_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (expenseList.get(position).getType() != 0) {
                            Intent priceIntent = new Intent(getActivity(), UpdateExpense.class);
                            priceIntent.putExtra(Constants.RECORD_ID, expenseList.get(position).getId());
                            startActivityForResult(priceIntent, 800);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        if (expenseList.get(position).getType() != 0) {
                            launchMessageDialog(expenseList.get(position).getId());
                        }
                    }
                })
        );


        return rootView;
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
        text.setText(Constants.DIALOG_MSG_DELETE_EXPENSE);

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

                dbHelper.deleteExpense(id);
                Toast.makeText(getActivity(), Constants.TOAST_MSG_SUCCES_EXPENSE_DELETE, Toast.LENGTH_LONG).show();
                expenseList.clear();
                expenseList = dbHelper.getAllExpenses();
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new ExpenseAdapter(expenseList);
                recyclerView.setAdapter(mAdapter);

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        expenseList.clear();
        expenseList = dbHelper.getAllExpenses();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(mAdapter);
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