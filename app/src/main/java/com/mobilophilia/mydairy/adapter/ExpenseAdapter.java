package com.mobilophilia.mydairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.database.ExpenseBean;
import com.mobilophilia.mydairy.database.MyEntries;
import com.mobilophilia.mydairy.manager.beans.DownloadBean;

import java.util.List;

/**
 * Created by mukesh on 16/08/17.
 */
public class ExpenseAdapter extends RecyclerView.Adapter {


    private List<ExpenseBean> expenses;
    public static final int HEADER_TYPE = 0;
    public static final int CONTENT_TYPE = 1;

    public ExpenseAdapter(List<ExpenseBean> dataSet) {
        this.expenses = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (expenses != null) {
            ExpenseBean object = expenses.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_row, parent, false);
                return new ExpenseAdapter.HeaderViewHolder(view);
            case CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_expenses_list, parent, false);
                return new ExpenseAdapter.ContentViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpenseBean object = expenses.get(position);
        if (object != null) {
            switch (object.getType()) {
                case HEADER_TYPE:
                    ((ExpenseAdapter.HeaderViewHolder) holder).tvHeaderText.setText(object.getHeaderText());
                    break;
                case CONTENT_TYPE:
                    ((ExpenseAdapter.ContentViewHolder) holder).customerCode.setText("Code: " + object.getCode());
                    ((ExpenseAdapter.ContentViewHolder) holder).customerName.setText("" + object.getName());
                    ((ExpenseAdapter.ContentViewHolder) holder).total.setText("Expense: ( " + object.getExpense()+" )");
                    ((ExpenseAdapter.ContentViewHolder) holder).time.setText("" + object.getHeaderText());

                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderText = (TextView) itemView.findViewById(R.id.tv_group_by);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView customerCode, customerName, total,time;

        public ContentViewHolder(View itemView) {
            super(itemView);

            customerCode = (TextView) itemView.findViewById(R.id.ex_customer_code);
            total = (TextView) itemView.findViewById(R.id.ex_expense);
            customerName = (TextView) itemView.findViewById(R.id.ex_customer_name);
            time = (TextView) itemView.findViewById(R.id.ex_save_time);
        }
    }
}

