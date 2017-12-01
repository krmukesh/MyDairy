package com.mobilophilia.mydairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.database.ExpenseBean;
import com.mobilophilia.mydairy.timer.TimeoutActivity;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mukesh on 03/09/17.
 */

public class UpdateExpense extends TimeoutActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DBHelper dbHelper;
    private TextView customerName;
    private TextView customerPhone;
    private TextView inputCode;
    private EditText inPutExpenses;
    private String errorMessage;
    private Button update;
    private Button cancel;
    private AppCompatCheckBox isDebitBox;
    private int positionDropDown = 0;
    private int recordId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_update_expense);


        customerName = (TextView) findViewById(R.id.customer_name);
        customerPhone = (TextView) findViewById(R.id.customer_phone);
        inputCode = (TextView) findViewById(R.id.input_code_tv);

        inPutExpenses = (EditText) findViewById(R.id.input_expenses);
        Spinner mySpinner = (Spinner) findViewById(R.id.expense_type);
        isDebitBox = (AppCompatCheckBox) findViewById(R.id.cb_isdebit);

        update = (Button) findViewById(R.id.btn_update_ex);
        cancel = (Button) findViewById(R.id.btn_cancel_ex);

        inPutExpenses.setEnabled(false);
        update.setOnClickListener(this);
        cancel.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateExpense.this, R.layout.spinner_item, R.id.sp_item, Constants.expense_type);
        mySpinner.setOnItemSelectedListener(this);
        mySpinner.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        dbHelper = new DBHelper(UpdateExpense.this);

        if (extras != null) {

            recordId = extras.getInt(Constants.RECORD_ID);
            ExpenseBean exBean = dbHelper.getExpensesById(recordId);
            customerName.setText("" + exBean.getName());
            customerPhone.setText("" + exBean.getPhone());
            inputCode.setText("" + exBean.getCode());
            positionDropDown = exBean.getExpenseType();
            mySpinner.setSelection(positionDropDown);

            if(exBean.getExpense()<0)
                isDebitBox.setChecked(true);


            inPutExpenses.setText("" + String.format("%.2f",exBean.getExpense()));

        }


        isDebitBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String expenses = inPutExpenses.getText().toString().trim();
                    if (!Util.isEmpty(expenses)) {
                        Double exp = Double.parseDouble(expenses);
                        exp = exp * -1;
                        inPutExpenses.setText("" + exp);
                    }
                } else {
                    String expenses = inPutExpenses.getText().toString().trim();
                    if (!Util.isEmpty(expenses)) {
                        Double exp = Double.parseDouble(expenses);
                        exp = exp * -1;
                        inPutExpenses.setText("" + exp);
                    }
                }
            }
        });


        inputCode.addTextChangedListener(new TextWatcher() {
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
                if (!Util.isEmpty(inputCode.getText().toString())) {
                    EnterNameEntry ene = dbHelper.getEnteredName(Integer.parseInt(inputCode.getText().toString()));
                    if (ene != null) {
                        customerName.setText(ene.getEnterName());
                        customerPhone.setText(ene.getPhoneNo());
                        Util.setBlankAndIsEnable(inPutExpenses, true);
                    } else {
                        Util.setTextBlank(customerName);
                        Util.setTextBlank(customerPhone);
                        Util.setBlankAndIsEnable(inPutExpenses, false);
                        isDebitBox.setChecked(false);
                        errorMessage = Constants.ERROR_CODE_INVALID;
                        errorMessage(errorMessage);
                    }
                } else {
                    Util.setTextBlank(customerName);
                    Util.setTextBlank(customerPhone);
                    Util.setBlankAndIsEnable(inPutExpenses, false);
                    isDebitBox.setChecked(false);
                }
                // TODO Auto-generated method stub
            }
        });


        inPutExpenses.addTextChangedListener(new TextWatcher() {
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
                String expenses = inPutExpenses.getText().toString().trim();

                if (!Util.isEmpty(expenses)) {
                    Util.iSenableButton(UpdateExpense.this, update, true);
                } else {
                    Util.iSenableButton(UpdateExpense.this, update, false);
                    isDebitBox.setChecked(false);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            case 2:
                positionDropDown = 2;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_update_ex:
                if (feildValidation()) {
                    updateExpense(recordId);
                } else {
                    errorMessage(errorMessage);
                }
                break;
            case R.id.btn_cancel_ex:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean feildValidation() {
        String exp = inPutExpenses.getText().toString().trim();
        boolean rtn = true;
        if (Util.isEmpty(exp)) {
            errorMessage = Constants.ERROR_EXPENSE_BLANK;
            rtn = false;
        }

        if (!Util.isEmpty(exp)) {
            Double expV = Double.parseDouble(exp);
            if (expV == 0) {
                errorMessage = Constants.ERROR_EXPENSE_BLANK_ZERO;
                rtn = false;
            }
        }

        return rtn;
    }

    private void updateExpense(int id) {
        String code = inputCode.getText().toString().trim();
        String expenses = inPutExpenses.getText().toString().trim();
        ExpenseBean bean = new ExpenseBean();
        bean.setId(id);
        bean.setCode(Integer.parseInt(code));
        bean.setExpenseType(positionDropDown);
        bean.setExpense(Double.parseDouble(expenses));
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.updateExpense(bean);
        Toast.makeText(UpdateExpense.this, Constants.TOAST_MSG_SUCCES_EXPENSE_UPDATE, Toast.LENGTH_LONG).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",800);
        setResult(Activity.RESULT_OK,returnIntent);

        finish();
    }

    private void errorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onTimeout() {

    }

    @Override
    protected long getTimeoutInSeconds() {
        return 0;
    }
}