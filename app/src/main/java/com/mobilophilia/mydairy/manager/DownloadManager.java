package com.mobilophilia.mydairy.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.RestClient;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Hanji on 7/26/2017.
 */

public class DownloadManager {
    private String TAG = "EntryManager-->";
    private StringEntity se;

    String[] fileType = {

            "image/png",
            "image/jpeg",
            "image/gif"
    };


   /* public void downLoadReport(final Context context, String code, String clr, String fat, String ltr, String total) {
        final JSONObject inputDetails = new JSONObject();
        try {
            inputDetails.put("agentId", code);
            inputDetails.put("password", clr);
            inputDetails.put("agentId", fat);
            inputDetails.put("password", ltr);
            inputDetails.put("agentId", total);
            se = new StringEntity(inputDetails.toString());
            se.setContentType("application/json");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.e(TAG + "Input -Data for saveEntry", inputDetails.toString());
        RestClient.getFile(context, Constants.REPORT_FILE, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

            }
        });
    }*/
/*
    String[] allowedTypes = new String[] { "image/png" };
    AsyncHttpClient client = new AsyncHttpClient();
    client.get("http://example.com/dock.png",
            new BinaryHttpResponseHandler(allowedTypes) {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {

            Toast.makeText(getApplicationContext(),"Successful in finding file",Toast.LENGTH_SHORT).show();
            try {
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                FileOutputStream f = new FileOutputStream((new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)   + File.separator + "imgdwo.png")));
                f.write(bytes); //your bytes
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }


    });*/


    /*public void fileDownload(Context context, String date) {

        RequestParams params = new RequestParams();
        params.put("username", "date");

        RestClient.getFile(context, "https://api.androidhive.info/progressdialog/hive.jpg", null, params, new FileAsyncHttpResponseHandler(context) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                // super.onSuccess(statusCode,headers,file);

                EventBus.getDefault().post(new MessageEvent(MessageEvent.DOWNLOAD_REPORT_SUCCESS));
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int totProgress = (int) (((float) bytesWritten * 100) / totalSize);
                Log.d("Progress::::", "" + totProgress);
                if (totProgress > 0) {
                    String progressValue = "" + totProgress;
                    //progressBar.setProgress(totProgress);
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.DOWNLOAD_REPORT_PROGRESS, progressValue));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File response) {
                // super.onFailure(statusCode, headers,throwable,response);
                if (throwable != null && throwable instanceof SocketTimeoutException) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.NETWORK_TIME_OUT));
                } else {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                }
            }
        });
        // }
    }*/


   /* public void dd(final Context context){

        RequestParams params = new RequestParams();
        params.put("username", "date");
        final String url = "https://api.androidhive.info/progressdialog/hive.jpg";

        RestClient.getFileBinary(context, url, null, params, new BinaryHttpResponseHandler(fileType) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {

                try {
                    //Splitting a File Name from SourceFileName
                    String DestinationName = url.substring(url.lastIndexOf('/')+1, url.length());
                    //Saving an image into DCIM Folder
                    File _f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),DestinationName);
                    FileOutputStream output =new  FileOutputStream(_f);
                    output.write(binaryData);
                    output.close();

                    //Refreshing MediaScanner, so that our downloaded image can be shown in Gallery
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),DestinationName);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    context.sendBroadcast(mediaScanIntent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int totProgress = (int) (((float) bytesWritten * 100) / totalSize);
                Log.d("Progress::::", "" + totProgress);
                if (totProgress == 1) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.DOWNLOAD_REPORT_PROGRESS));
                }else{

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.DOWNLOAD_REPORT_PROGRESS_VALUE, totProgress));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

            }
        });


    }*/



}
