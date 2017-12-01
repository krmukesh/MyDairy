package com.mobilophilia.mydairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.database.MyEntries;

import java.util.List;

/**
 * Created by Hanji on 8/2/2017.
 */

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.MyViewHolder> {

    private List<MyEntries> entries;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customerCode, customerName, total;

        public MyViewHolder(View view) {
            super(view);
            customerCode = (TextView) view.findViewById(R.id.costomer_code_el);
            customerName = (TextView) view.findViewById(R.id.customer_name_el);
            total = (TextView) view.findViewById(R.id.custome_total_amount_el);
        }
    }

    public EntryListAdapter(List<MyEntries> entriesList) {
        this.entries = entriesList;
    }

    @Override
    public EntryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_entry_list, parent, false);

        return new EntryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EntryListAdapter.MyViewHolder holder, int position) {
        MyEntries entry = entries.get(position);
        holder.customerCode.setText("Code: " + entry.getCode());
        holder.customerName.setText("" + entry.getName());
        holder.total.setText("" + String.format("%.2f", entry.getTotal()));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}