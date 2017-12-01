package com.mobilophilia.mydairy.manager;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.RestClient;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Hanji on 7/26/2017.
 */

public class ExpenseManager {

    private String TAG = "ExpenseManager-->";
    private StringEntity se;

   /* public void saveEXpenes(final Context context, String code, String expenses) {
        final JSONObject inputDetails = new JSONObject();
        try {
            inputDetails.put("code", code);
            inputDetails.put("expenses", expenses);
            se = new StringEntity(inputDetails.toString());
            se.setContentType("application/json");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Log.e(TAG + "Input -Data for saveEntry", inputDetails.toString());
        RestClient.postWithHeader(context, Constants.EXPENSES, se, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e(TAG + "Response ExpenseManager", response.toString());
                    int status = response.getInt("status");
                    if (status == Constants.SERVICE_STATUS) {
                        String message = response.getString("message");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.EXPENSES_SUCCESS, message));
                    } else {
                        String message = response.getString("message");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.ENTRY_FAILURE, message));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (throwable != null && throwable instanceof SocketTimeoutException) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.NETWORK_TIME_OUT));
                } else {
                    if (errorResponse != null) {
                        if (errorResponse.has("message")) {
                            try {
                                String message = errorResponse.getString("message");
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.EXPENSES_FAILURE, message));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
            }
        });
    }*/


}
