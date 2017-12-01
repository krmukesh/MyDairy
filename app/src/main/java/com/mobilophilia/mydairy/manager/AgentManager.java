package com.mobilophilia.mydairy.manager;

/**
 * Created by Hanji on 7/21/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

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
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * Created by hanji on 21/7/11.
 */
public class AgentManager {
    private String TAG = "AgentManager-->";

    public void loginUser(final Context context, String agentId, String password) {
        final JSONObject inputDetails = new JSONObject();
        try {
            inputDetails.put("agentId", agentId);
            inputDetails.put("password", password);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        RestClient.loginPost(Constants.LOGIN, inputDetails.toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == HttpStatus.SC_OK) {
                    try {
                        int status = response.getInt("status");
                        if (status == Constants.SERVICE_STATUS) {
                            //Handle Service Result
                            JSONObject resultData = response.getJSONObject("resultData");
                            int id = resultData.getInt("id");
                            String token = resultData.getString("token");
                            String agentId = resultData.getString("agentId");
                            String name = resultData.getString("name");
                            String phone = resultData.getString("phone");
                            String email = resultData.getString("email");
                            String address = resultData.getString("address");

                            saveData(context, id, token, agentId, name, phone, email, address);


                            Log.e(TAG + "response", response.toString());
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_IN_SUCCESS));

                        } else {
                            //Handle Service Error Message
                            String message = "mmm";
                            message = response.getString("message");
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_IN_FAILURE, message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.SERVER_ERROR_OCCURRED));
                    }
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
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.LOG_IN_FAILURE, message));
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
    }

    public void synService(final Context context, JSONObject inputData, final boolean isEntry) {

        RestClient.post(context, Constants.SYNC, inputData.toString(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.e(TAG + "Response Sync", response.toString());
                    int status = response.getInt("status");
                    if (status == Constants.SERVICE_STATUS) {
                        String message = Constants.TOAST_MSG_SUCCES_SYNC;
                        if(isEntry) {
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_SUCCESS_ENTRY, message));
                        }else {
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_SUCCESS, message));
                        }
                    } else {
                        String message = response.getString("message");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_FAILURE, message));
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
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.ENTRY_FAILURE, message));
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
    }


    private void saveData(Context context, int id, String userToken, String agentId, String agentName, String agentPhone, String agentEmail, String agentAddress) {

        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.SP_ID_KEY, id);
        editor.putString(Constants.SP_AGENT_TOKEN_KEY, userToken);
        editor.putString(Constants.SP_AGENT_ID_KEY, agentId);
        editor.putString(Constants.SP_AGENT_NAME_KEY, agentName);
        editor.putString(Constants.SP_AGENT_PHONE_KEY, agentPhone);
        editor.putString(Constants.SP_AGENT_EMAIL_KEY, agentEmail);
        editor.putString(Constants.SP_AGENT_ADDRESS_KEY, agentAddress);
        editor.apply();

    }
}
