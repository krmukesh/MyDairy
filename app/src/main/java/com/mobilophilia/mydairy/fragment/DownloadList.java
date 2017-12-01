package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.adapter.AdapterDownloadList;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.LastModifiedFileComparator;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.RecyclerItemClickListener;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.manager.beans.DownloadBean;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mukesh on 18/08/17.
 */

public class DownloadList extends Fragment {

    private List<DownloadBean> filesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterDownloadList mAdapter;
    private DBHelper dbHelper;

    public DownloadList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_list, container, false);

        dbHelper = new DBHelper(getActivity());
        filesList.clear();
        try {
            filesList = downloadFileList();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_download_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterDownloadList(filesList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int isheader = filesList.get(position).getType();
                        if (isheader != 0) {
                            String path = filesList.get(position).getFilePath();
                            try {
                                File myFile = new File(path);
                                openFile(myFile);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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



    public  void openFile(File url) throws IOException {

        File file=url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private List<DownloadBean>  downloadFileList() throws java.text.ParseException {

         List<DownloadBean> filesList = new ArrayList<>();

        String path = Util.createAppFolder(getAgentId());
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File[] files = directory.listFiles();
        DownloadBean downloadBean ;
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);

        String isHeadertext = "";

        for (int i = 0; i < files.length; i++) {

            Date lastModDate = new Date(files[i].lastModified());
            String []str = Util.formatToYesterdayOrToday(lastModDate);

            if(!isHeadertext.equalsIgnoreCase(str[0])){
                downloadBean = new DownloadBean();
                downloadBean.setType(0);
                downloadBean.setHeaderText(str[0]);
                filesList.add(downloadBean);
                isHeadertext = str[0];
            }

            downloadBean = new DownloadBean();
            downloadBean.setFileName(files[i].getName());
            downloadBean.setFilePath(path+"/"+files[i].getName());
            downloadBean.setType(1);
            downloadBean.setDownloadTime(""+str[1]);
            filesList.add(downloadBean);
        }
       return filesList;
    }



    private String getAgentId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SP_MY_DAIRY_MK, getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        return id;
    }
}