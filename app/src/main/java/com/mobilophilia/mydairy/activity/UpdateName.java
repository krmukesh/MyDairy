package com.mobilophilia.mydairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.timer.TimeoutActivity;


import io.fabric.sdk.android.Fabric;

/**
 * Created by mukesh on 27/08/17.
 */

public class UpdateName extends TimeoutActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private DBHelper dbHelper;
    private Button btnUpdate;
    private Button btnCancel;

    private TextView inPutCode;
    private EditText inPutName;
    private EditText inPutPhone;
    private int positionDropDown = 0;

    private String errorMessage = "";
    private int recordId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_update_name);

        inPutCode = (TextView) findViewById(R.id.input_code);
        inPutName = (EditText) findViewById(R.id.input_name);
        inPutPhone = (EditText) findViewById(R.id.input_phone);

        btnUpdate = (Button) findViewById(R.id.btn_update_name);
        btnCancel = (Button) findViewById(R.id.btn_cancel_name);
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        dbHelper = new DBHelper(this);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner_cow_buffalo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.sp_item, Constants.type);
        mySpinner.setOnItemSelectedListener(this);
        mySpinner.setAdapter(adapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Util.setEnable(inPutName, true);
        Util.setEnable(inPutPhone, true);
        Util.iSenableButton(this, btnUpdate, true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getInt(Constants.RECORD_ID);
            EnterNameEntry nameBean = dbHelper.getEnteredName(recordId);
            inPutCode.setText(""+nameBean.getNameCode());
            inPutName.setText(nameBean.getEnterName());
            inPutPhone.setText(nameBean.getPhoneNo());
            positionDropDown = nameBean.getType();
            mySpinner.setSelection(positionDropDown);
        }

        inPutName.addTextChangedListener(new TextWatcher() {
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
                String name = inPutName.getText().toString().trim();
                if (!Util.isEmpty(name)) {
                    if (name.length() >0) {
                        Util.setBlankAndIsEnable(inPutPhone, true);
                        Util.iSenableButton(UpdateName.this, btnUpdate, false);
                    } else {
                        Util.setBlankAndIsEnable(inPutPhone, false);
                        Util.iSenableButton(UpdateName.this, btnUpdate, false);
                    }
                }
            }
        });

        inPutPhone.addTextChangedListener(new TextWatcher() {
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

                String phone = inPutPhone.getText().toString().trim();
                if (!Util.isEmpty(phone) && phone.length()>9) {
                    Util.iSenableButton(UpdateName.this, btnUpdate, true);
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                positionDropDown = 0;
                break;
            case 1:
                positionDropDown = 1;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_update_name:
                Util.hideKeyboard(this);
                if(feildValidation()){
                    saveEntry();
                    errorMessage("Name has been updated successfully.");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",1000);
                    setResult(Activity.RESULT_OK,returnIntent);
                    UpdateName.this.finish();
                }else{
                    errorMessage(errorMessage);
                }
                break;
            case R.id.btn_cancel_name:
               finish();
                break;
            default:
                break;
        }
    }

    private void saveEntry() {
        EnterNameEntry ene = new EnterNameEntry();
        ene.setNameCode(Integer.parseInt(inPutCode.getText().toString().trim()));
        ene.setEnterName(inPutName.getText().toString().trim());
        ene.setType(positionDropDown);
        ene.setPhoneNo(inPutPhone.getText().toString().trim());
        dbHelper.updateEnteredName(ene);
    }

    private boolean feildValidation() {
        String phone = inPutPhone.getText().toString().trim();
        boolean rtn = true;
        if (Util.isEmpty(inPutName.getText().toString().trim())) {
            errorMessage = Constants.ERROR_VALIDATION_NAME_ii;
            rtn = false;
        } else if (Util.isEmpty(phone) || phone.length()<10) {
            errorMessage = Constants.ERROR_VALIDATION_PHONE;
            rtn = false;
        }
        return rtn;
    }

    private void errorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onTimeout() {
        new MainActivity().autologout();
    }

    @Override
    protected long getTimeoutInSeconds() {
        return Constants.TIMER;
    }
}
