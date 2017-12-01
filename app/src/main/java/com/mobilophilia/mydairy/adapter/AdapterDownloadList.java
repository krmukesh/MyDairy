package com.mobilophilia.mydairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.manager.beans.DownloadBean;

import java.util.List;

/**
 * Created by mukesh on 18/08/17.
 */



public class AdapterDownloadList extends RecyclerView.Adapter {

    private List<DownloadBean> dataSet;
    public static final int HEADER_TYPE = 0;
    public static final int CONTENT_TYPE = 1;

    public AdapterDownloadList(List<DownloadBean> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet != null) {
            DownloadBean object = dataSet.get(position);
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
                return new HeaderViewHolder(view);
            case CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_download_list, parent, false);
                return new ContentViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DownloadBean object = dataSet.get(position);
        if (object != null) {
            switch (object.getType()) {
                case HEADER_TYPE:
                    ((HeaderViewHolder) holder).tvHeaderText.setText(object.getHeaderText());
                    break;
                case CONTENT_TYPE:
                    ((ContentViewHolder) holder).tvFileName.setText(object.getFileName());
                    ((ContentViewHolder) holder).tvDownloadTime.setText(object.getDownloadTime());
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeaderText = (TextView) itemView.findViewById(R.id.tv_group_by);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFileName, tvDownloadTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView) itemView.findViewById(R.id.tv_file_name );
            tvDownloadTime = (TextView) itemView.findViewById(R.id.tv_download_time);
        }
    }
}
