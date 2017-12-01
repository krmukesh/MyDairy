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
 * Created by mukesh on 04/08/17.
 */

public class EntriesListByCodeAdapter extends RecyclerView.Adapter {


    private List<MyEntries> entries;
    public static final int HEADER_TYPE = 0;
    public static final int CONTENT_TYPE = 1;

    public EntriesListByCodeAdapter(List<MyEntries> dataSet) {
        this.entries = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (entries != null) {
            MyEntries object = entries.get(position);
            if (object != null) {
                return object.getContentType();
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
                return new EntriesListByCodeAdapter.HeaderViewHolder(view);
            case CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_entries_by_code, parent, false);
                return new EntriesListByCodeAdapter.ContentViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyEntries entry = entries.get(position);
        if (entry != null) {
            switch (entry.getContentType()) {
                case HEADER_TYPE:
                    ((EntriesListByCodeAdapter.HeaderViewHolder) holder).tvHeaderText.setText(entry.getHeaderText());
                    break;
                case CONTENT_TYPE:
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).code.setText("Code: " + entry.getCode());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).clr.setText("Clr: " + entry.getClr());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).fat.setText("Fat: " + entry.getFat());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).snf.setText("Snf: " + entry.getSnf());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).ltr.setText("Litre: " + entry.getLtr());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).price.setText("Price: " + entry.getPrice());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).total.setText("Total:" + entry.getTotal());
                    ((EntriesListByCodeAdapter.ContentViewHolder) holder).timestamp.setText("" + entry.getHeaderText());
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderText = (TextView) itemView.findViewById(R.id.tv_group_by);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView code, clr, fat, snf, ltr, price, total, timestamp;

        public ContentViewHolder(View view) {
            super(view);

            code = (TextView) view.findViewById(R.id.txt_code);
            clr = (TextView) view.findViewById(R.id.txt_clr);
            fat = (TextView) view.findViewById(R.id.txt_fat);
            snf = (TextView) view.findViewById(R.id.txt_snf);
            ltr = (TextView) view.findViewById(R.id.txt_ltr);
            price = (TextView) view.findViewById(R.id.txt_price);
            total = (TextView) view.findViewById(R.id.total_price_ex);
            timestamp = (TextView) view.findViewById(R.id.es_time_stamp);
        }
    }
}