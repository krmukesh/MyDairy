package com.mobilophilia.mydairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.manager.AppManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yogen on 14-07-2017.
 */

public class LoginActivity extends Activity implements View.OnClickListener {


    private Button loginButton;
    private EditText edtAgentId;
    private EditText edtPassword;
    private String errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);

        edtAgentId = (EditText) findViewById(R.id.input_agent_id);
        edtPassword = (EditText) findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);


        Util.setBlankAndIsEnable(edtAgentId, true);
        Util.setBlankAndIsEnable(edtPassword, false);
        Util.iSenableButton(LoginActivity.this, loginButton, false);

        edtAgentId.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String agentId = edtAgentId.getText().toString().trim();
                if (!Util.isEmpty(agentId)) {
                    if (agentId.length() >= 8) {
                        Util.setBlankAndIsEnable(edtPassword, true);
                        Util.iSenableButton(LoginActivity.this, loginButton, false);
                    } else {
                        Util.setBlankAndIsEnable(edtPassword, false);
                        Util.iSenableButton(LoginActivity.this, loginButton, false);
                    }
                } else {
                    Util.setBlankAndIsEnable(edtPassword, false);
                    Util.iSenableButton(LoginActivity.this, loginButton, false);
                    errorMessage = Constants.ERROR_VALIDATION_AGENT_ID;
                    errorMessage(errorMessage);
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = edtPassword.getText().toString().trim();
                if (!Util.isEmpty(password)) {
                    if (password.length() >= 8) {
                        Util.iSenableButton(LoginActivity.this, loginButton, true);
                    } else {
                        Util.iSenableButton(LoginActivity.this, loginButton, false);
                    }
                } else {
                    Util.iSenableButton(LoginActivity.this, loginButton, false);
                    errorMessage = Constants.ERROR_VALIDATION_PASSWORD;
                    errorMessage(errorMessage);
                }
                // TODO Auto-generated method stub
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_login:
                prepareForLogin();
                break;
            default:
                break;
        }
    }

    private void reSetValues() {
        Util.setBlankAndIsEnable(edtAgentId, true);
        Util.setBlankAndIsEnable(edtPassword, false);
        Util.iSenableButton(LoginActivity.this, loginButton, false);
    }
    private void prepareForLogin() {
        Util.hideKeyboard(LoginActivity.this);
        Util.launchBarDialog(LoginActivity.this, Constants.PROGRESS_DIALOG_MSG);
        String agentID = edtAgentId.getText().toString().trim();
        String pwd = edtPassword.getText().toString().trim();
        AppManager.getInstance().getLoginManager().loginUser(LoginActivity.this, agentID, pwd);
    }

    private void errorMessage(String message) {
        Snackbar.make(edtAgentId, "" + message, Snackbar.LENGTH_LONG).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.LOG_IN_SUCCESS:
                Util.dismissBarDialog();
                reSetValues();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;
            case MessageEvent.LOG_IN_FAILURE:
                Util.dismissBarDialog();
                Util.launchMessageDialog(LoginActivity.this, "" + event.getMessage());
                reSetValues();
                break;

            case MessageEvent.NETWORK_TIME_OUT:
                Util.dismissBarDialog();
                reSetValues();
                Toast.makeText(getApplicationContext(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;

            case MessageEvent.SERVER_ERROR_OCCURRED:
                Util.dismissBarDialog();
                reSetValues();
                Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

}
