package com.mobilophilia.mydairy.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * Created by akshit_sharma on 24/8/15.
 */
public class RestClient {

    //public static final AsyncHttpClient client = new AsyncHttpClient();

    private static final String TAG = RestClient.class.getSimpleName();
    private static StringEntity se = null;
    private static final int CONNECTION_TIMEOUT = 20000;
    public static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();


    private static AsyncHttpClient getClient() {
        if (Looper.myLooper() == null) {
            AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
            syncHttpClient.setMaxRetriesAndTimeout(1, CONNECTION_TIMEOUT);
            return syncHttpClient;
        } else {
            AsyncHttpClient.allowRetryExceptionClass(SocketTimeoutException.class);
            asyncHttpClient.setMaxRetriesAndTimeout(1, CONNECTION_TIMEOUT);
            return asyncHttpClient;
        }
    }


    public static void loginPost(String url, String post_data, AsyncHttpResponseHandler responseHandler) {
        Log.e(TAG, "POST-URL---> " + url + " <-> POST-DATA---> " + post_data);
        try {
            se = null;
            se = new StringEntity(post_data);
            se.setContentType("application/json");
            getClient().post(null, url, se, "application/json", responseHandler);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }



    public static void post(Context context,String url, String post_data, AsyncHttpResponseHandler responseHandler) {
        try {
            se = null;
            se = new StringEntity(post_data);
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
            String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);
            getClient().addHeader("token", token);
            Log.e("post", "POST-URL---> " + url + "token "+token +" <-> POST-DATA---> " + post_data);
            getClient().post(null, url, se, "application/json", responseHandler);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }


    public static void get(Context context,String url, String post_data, AsyncHttpResponseHandler responseHandler) {
        Log.e(TAG, "POST-URL---> " + url + " <-> POST-DATA---> " + post_data);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);
        getClient().addHeader("token", token);
        getClient().get(url, responseHandler);

    }



   /* public static void get(String url, Header[] header, AsyncHttpResponseHandler responseHandler) {
        client.get(null, url, header, null, responseHandler);
        client.setTimeout(30000);
    }
    public static void post(Context context, String url, Header[] headers, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.post(context, url, headers, entity, "application/json", responseHandler);
        client.setTimeout(30000);
    }

    public static void getFile(Context context, String url, Header[] headers, RequestParams requestParams, FileAsyncHttpResponseHandler responseHandler) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);
        client.addHeader("token", token);
        client.get(context, url, headers, requestParams, responseHandler);
        client.setTimeout(30000);
    }


    public static void getFileBinary(Context context, String url, Header[] headers, RequestParams requestParams, BinaryHttpResponseHandler responseHandler) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);
        client.addHeader("token", token);
        client.get(context, url, headers, requestParams, responseHandler);
        client.setTimeout(30000);
    }

    public static void postWithHeader(Context context, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);
     //   client.allowRetryExceptionClass(SocketTimeoutException.class);
        client.addHeader("token", token);
        client.post(context, url, entity, "application/json", responseHandler);
        client.setTimeout(30000);
    }*/

}
