package com.mobilophilia.mydairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.EnterNameEntry;

import java.util.List;

/**
 * Created by Hanji on 7/22/2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter {


    private List<EnterNameEntry> namesList;
    public static final int HEADER_TYPE = 0;
    public static final int CONTENT_TYPE = 1;

    public CustomerListAdapter(List<EnterNameEntry> dataSet) {
        this.namesList = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (namesList != null) {
            EnterNameEntry object = namesList.get(position);
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
                return new CustomerListAdapter.HeaderViewHolder(view);
            case CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_list, parent, false);
                return new CustomerListAdapter.ContentViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EnterNameEntry object = namesList.get(position);
        if (object != null) {
            switch (object.getContentType()) {
                case HEADER_TYPE:
                    ((CustomerListAdapter.HeaderViewHolder) holder).tvHeaderText.setText(object.getHeaderText());
                    break;
                case CONTENT_TYPE:
                    ((CustomerListAdapter.ContentViewHolder) holder).customerCode.setText("Code : " + object.getNameCode());
                    ((CustomerListAdapter.ContentViewHolder) holder).customerName.setText("" + Util.capitalizeString(object.getEnterName()));
                    ((CustomerListAdapter.ContentViewHolder) holder).customerPhone.setText("" + object.getPhoneNo());
                    ((CustomerListAdapter.ContentViewHolder) holder).type.setText("Type : " + Util.getTypeFromSelected(object.getType()));
                    ((ContentViewHolder) holder).timeStamp.setText("" + object.getHeaderText());
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return namesList.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderText = (TextView) itemView.findViewById(R.id.tv_group_by);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView customerCode, customerName, customerPhone, type, timeStamp;

        public ContentViewHolder(View view) {
            super(view);

            customerCode = (TextView) view.findViewById(R.id.customer_code);
            customerName = (TextView) view.findViewById(R.id.costomer_name_lv);
            customerPhone = (TextView) view.findViewById(R.id.customer_phone);
            type = (TextView) view.findViewById(R.id.customer_type);
            timeStamp = (TextView) view.findViewById(R.id.time_stamp);
        }
    }
}