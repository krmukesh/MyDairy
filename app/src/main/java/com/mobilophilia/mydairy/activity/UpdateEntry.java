package com.mobilophilia.mydairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.MyEntries;
import com.mobilophilia.mydairy.database.SetPriceEntry;
import com.mobilophilia.mydairy.timer.TimeoutActivity;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by mukesh on 17/08/17.
 */

public class UpdateEntry extends TimeoutActivity implements View.OnClickListener {

    public Double calculatedSNF = 0d;
    private DBHelper dbHelper;
    private TextView customerName;
    private TextView customerPhone;
    private TextView inputCode;
    private EditText inputClr;
    private EditText inputFat;
    private EditText inputLtr;
    private TextView inputTotal;
    private String errorMessage;
    private Button btnSubmit;
    private int milkType;
    private TextView calPrice;
    private String setTotalPrice;
    private Double calculatedPrice;
    private Button cancelUp;
    private TextView calSnf;
    private int morOrEveTime;
    private int recordId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_update_entry);

        dbHelper = new DBHelper(UpdateEntry.this);

        customerName = (TextView) findViewById(R.id.customer_name);
        customerPhone = (TextView) findViewById(R.id.customer_phone);
        inputCode = (TextView) findViewById(R.id.input_code);

        inputClr = (EditText) findViewById(R.id.input_clr);
        inputFat = (EditText) findViewById(R.id.input_fat);
        inputLtr = (EditText) findViewById(R.id.input_ltr);
        inputTotal = (TextView) findViewById(R.id.input_total);
        calPrice = (TextView) findViewById(R.id.cal_price);
        calSnf = (TextView) findViewById(R.id.cal_snf);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        cancelUp = (Button) findViewById(R.id.btn_cancel_up);
        cancelUp.setOnClickListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             recordId = extras.getInt(Constants.RECORD_ID);

            MyEntries myEntries = dbHelper.getEntryById(recordId);
            customerName.setText(myEntries.getName());
            customerPhone.setText(myEntries.getPhone());
            inputCode.setText("" + myEntries.getCode());
            milkType =  myEntries.getMilkType();

            inputClr.setText("" + myEntries.getClr());
            inputFat.setText("" + myEntries.getFat());
            inputLtr.setText("" + myEntries.getLtr());




            setTotalPrice = String.format("%.2f", myEntries.getTotal());
            calculatedPrice = Util.round0ffTwoPlace(myEntries.getPrice());
            calculatedSNF = Util.round0ffOnePlace(myEntries.getSnf());


            inputTotal.setText("Total: " + setTotalPrice);
            calPrice.setText("Price: " + calculatedPrice);
            calSnf.setText("Snf: "+ calculatedSNF);
            morOrEveTime =  myEntries.getMorOrEveTime();
        }

        Util.setEnable(inputClr, true);
        Util.setEnable(inputFat, true);
        Util.setEnable(inputLtr, true);
        Util.iSenableButton(this, btnSubmit, true);


        inputClr.addTextChangedListener(new TextWatcher() {
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
                String clr = inputClr.getText().toString().trim();
                if (!Util.isEmpty(clr)) {
                    Double lclr = Double.parseDouble(clr);
                    if (lclr < 1) {
                        errorMessage = Constants.ERROR_VALIDATION_CLR;
                        errorMessage(errorMessage);
                        Util.setBlankAndIsEnable(inputFat, false);
                        Util.setBlankAndIsEnable(inputLtr, false);
                        Util.setTextBlank(inputTotal);
                        Util.setTextBlank(calPrice);
                        Util.setTextBlank(calSnf);
                    } else {
                        Util.setBlankAndIsEnable(inputFat, true);
                        Util.setBlankAndIsEnable(inputLtr, false);
                        Util.setTextBlank(inputTotal);
                        Util.setTextBlank(calPrice);
                        Util.setTextBlank(calSnf);
                    }
                } else {
                   // errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(inputFat, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);
                    Util.setTextBlank(calSnf);
                }
            }
        });


        inputFat.addTextChangedListener(new TextWatcher() {


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
                String fat = inputFat.getText().toString().trim();
                if (!Util.isEmpty(fat)) {
                    Double lFat = Double.parseDouble(fat);
                    if (lFat > 11 || lFat < 0) {
                        Util.setBlankAndIsEnable(inputLtr, false);
                        Util.setTextBlank(calPrice);
                        Util.setTextBlank(calSnf);
                        errorMessage = Constants.ERROR_FAT;
                        errorMessage(errorMessage);
                    } else {
                        String clr = inputClr.getText().toString().trim();
                        calculatedSNF = Util.convertIntoSNF(Double.parseDouble(clr), lFat);
                        Log.d("CalculatedSNF", "calculatedSNF>= " +calculatedSNF);
                        String cSNF = String.format("%.1f", calculatedSNF);
                        if (calculatedSNF > 13) {
                            errorMessage = Constants.ERROR_SNF + " Calculated SNF is: " + cSNF;
                            errorMessage(errorMessage);
                            Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                            Util.setBlankAndIsEnable(inputLtr, false);
                            Util.setTextBlank(inputTotal);
                            Util.setTextBlank(calPrice);
                        } else {
                            List<SetPriceEntry> arr = dbHelper.getPriceSetForGivenSNF(milkType, morOrEveTime, cSNF);
                            if (arr.size() > 0) {
                                Log.d("arrarrarrarrarr", "Type>= " + milkType + " , Fat >= " + fat + " , CSNF >= " + Double.valueOf(cSNF));

                                List<SetPriceEntry> arrData = dbHelper.getSetPriceTypeAndTime(milkType, morOrEveTime, fat, "" + Double.valueOf(cSNF));
                                if (arrData.size() > 0) {
                                    Double baseprice = arrData.get(0).getStartPrice();
                                    Double fatIntervl = arrData.get(0).getFatInterval();
                                    Double snfIntervl = arrData.get(0).getSnfInterval();
                                    Double lowFat = arrData.get(0).getLowFat();
                                    Double highFat = arrData.get(0).getHighFat();
                                    Double lowSnf = arrData.get(0).getLowSnf();
                                    Double highSnf = arrData.get(0).getHighSnf();

                                    Double snf = Double.valueOf(cSNF);
                                    if ((snf >= lowSnf) && (lFat >= lowFat)) {
                                       // calculatedPrice = Util.getPrice(baseprice, Double.valueOf(fat), snf, intervl, lowFat, highFat, lowSnf);
                                        calculatedPrice = Util.getPrice(baseprice, Double.valueOf(fat), snf, fatIntervl, snfIntervl,lowFat, highFat, lowSnf,highSnf);


                                        Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                                        Util.setBlankAndIsEnable(inputLtr, true);
                                        Util.setTextBlank(inputTotal);
                                        Util.setTextBlank(calPrice);
                                        Util.setTextBlank(calSnf);
                                    } else {
                                        errorMessage = Constants.NO_SET;
                                        errorMessage(errorMessage);
                                        Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                                        Util.setBlankAndIsEnable(inputLtr, false);
                                        Util.setTextBlank(inputTotal);
                                        Util.setTextBlank(calPrice);
                                        Util.setTextBlank(calSnf);
                                    }
                                } else {
                                    errorMessage = Constants.NO_SET;
                                    errorMessage(errorMessage);
                                    Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                                    Util.setBlankAndIsEnable(inputLtr, false);
                                    Util.setTextBlank(inputTotal);
                                    Util.setTextBlank(calPrice);
                                    Util.setTextBlank(calSnf);
                                }
                            } else {
                                Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                                Util.setBlankAndIsEnable(inputLtr, false);
                                Util.setTextBlank(inputTotal);
                                Util.setTextBlank(calPrice);
                                errorMessage = Constants.NO_SET;
                                errorMessage(errorMessage);
                            }
                        }
                    }
                } else {
                   // errorMessage = Constants.ERROR_FAT_BLANK;
                   // errorMessage(errorMessage);
                    Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);
                    Util.setTextBlank(calSnf);
                }
            }
        });


        inputLtr.addTextChangedListener(new TextWatcher() {
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

                if (!Util.isEmpty(inputLtr.getText().toString())) {
                    String ltr = inputLtr.getText().toString();
                    String priceCal = String.format("%.2f", calculatedPrice);
                    Double total = Double.valueOf(priceCal) * Double.valueOf(ltr);
                    setTotalPrice = String.format("%.2f", total);
                    calPrice.setText("Price: " +  String.format("%.2f",priceCal));
                    inputTotal.setText("Total: " + setTotalPrice);
                    calSnf.setText("Snf: "+ Util.round0ffOnePlace(calculatedSNF));
                    Util.iSenableButton(UpdateEntry.this, btnSubmit, true);
                } else {
                    Util.iSenableButton(UpdateEntry.this, btnSubmit, false);
                    inputTotal.setText("Total");
                    Util.setTextBlank(calPrice);
                    Util.setTextBlank(calSnf);
                }
            }
        });




    }


    private boolean feildValidation() {
        boolean rtn = true;
       if (Util.isEmpty(inputClr.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CLR;
            rtn = false;
        } else if (Util.isEmpty(inputFat.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_FAT;
            rtn = false;
        } else if (Util.isEmpty(inputLtr.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_LTR;
            rtn = false;
        }
        return rtn;
    }

    private boolean zeroValidation() {
        boolean rtn = true;

        String clr = inputClr.getText().toString().trim();
        String fat = inputFat.getText().toString().trim();
        String ltr = inputLtr.getText().toString().trim();
        if (!Util.isEmpty(clr)) {
            Double dClr = Double.parseDouble(clr);
            if (dClr < 1) {
                errorMessage = Constants.ERROR_VALIDATION_CLR;
                rtn = false;
            }
        }
        if (!Util.isEmpty(fat)) {
            Double dFat = Double.parseDouble(fat);
            if (dFat == 0 || dFat > 11) {
                errorMessage = Constants.ERROR_FAT;
                rtn = false;
            }
        }
        if (!Util.isEmpty(ltr)) {
            Double dLtr = Double.parseDouble(ltr);
            if (dLtr < 1) {
                errorMessage = Constants.ERROR_VALIDATION_LTR_Z;
                rtn = false;
            }
        }
        return rtn;
    }


    private void errorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), "" + message, Snackbar.LENGTH_LONG).show();
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
        switch (v.getId()) {
            case R.id.btn_submit:
                if (feildValidation()) {
                    saveMyEntry();
                    errorMessage("Entry has been updated successfully.");
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",500);
                    setResult(Activity.RESULT_OK,returnIntent);
                    UpdateEntry.this.finish();
                } else {
                    errorMessage = Constants.FIELD_MAINDATORY;
                    errorMessage(errorMessage);
                }
                break;
            case R.id.btn_cancel_up:
                finish();
                break;
            default:
                break;
        }
    }


    private void reSetValues() {
        Util.setBlankAndIsEnable(inputClr, false);
        Util.setBlankAndIsEnable(inputFat, false);
        Util.setBlankAndIsEnable(inputLtr, false);
        Util.setTextBlank(inputTotal);
        Util.setTextBlank(calPrice);
    }


    private void saveMyEntry() {
        Util.hideKeyboard(UpdateEntry.this);
        MyEntries myEntries = new MyEntries();

        String code = inputCode.getText().toString().trim();
        String clr = inputClr.getText().toString().trim();
        String fat = inputFat.getText().toString().trim();
        String ltr = inputLtr.getText().toString().trim();
        myEntries.setId(recordId);
        myEntries.setCode(Integer.parseInt(code));
        myEntries.setName(customerName.getText().toString());
        myEntries.setClr(Double.valueOf(clr));
        myEntries.setFat(Double.valueOf(fat));
        myEntries.setLtr(Double.valueOf(ltr));
        myEntries.setSnf(Util.round0ffOnePlace(calculatedSNF));
        myEntries.setPrice(Util.round0ffTwoPlace(calculatedPrice));
        myEntries.setTotal(Double.valueOf(setTotalPrice));
        myEntries.setMorOrEveTime(morOrEveTime);
        dbHelper.updateEntryWithId(myEntries);

        reSetValues();

    }


    private void checkIntervalForGivenSet(String fat,Double cSNF){
        List<SetPriceEntry> arr = dbHelper.getPriceSetForGivenSNF(milkType, morOrEveTime, ""+cSNF); // get set for given snf
        if (arr.size() > 0) {
            Log.d("arrarrarrarrarr", "Type>= " + milkType + " , Fat >= " + fat + " , CSNF >= " + cSNF);

            List<SetPriceEntry> arrData = dbHelper.getSetPriceTypeAndTime(milkType, morOrEveTime, fat, ""+cSNF); // get price set
            if (arrData.size() > 0) {
                Double baseprice = arrData.get(0).getStartPrice();
                Double fatIntervl = arrData.get(0).getFatInterval();
                Double snfIntervl = arrData.get(0).getSnfInterval();
                Double lowFat = arrData.get(0).getLowFat();
                Double highFat = arrData.get(0).getHighFat();
                Double lowSnf = arrData.get(0).getLowSnf();
                Double highSnf = arrData.get(0).getHighSnf();


                Double lFat = Double.valueOf(fat);
                if ((cSNF >= lowSnf) && (lFat >= lowFat)) {
                    calculatedPrice = Util.getPrice(baseprice, lFat, cSNF, fatIntervl,snfIntervl, lowFat, highFat, lowSnf,highSnf);
                    calPrice.setText("Price: "+ Util.round0ffOnePlace(calculatedPrice));
                } else {
                    /*errorMessage = Constants.NO_SET;
                    errorMessage(errorMessage);
                    Util.iSenableButton(this, btnSubmit, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);*/
                }
            } else {
               /* errorMessage = Constants.NO_SET;
                errorMessage(errorMessage);
                Util.iSenableButton(this, btnSubmit, false);
                Util.setBlankAndIsEnable(inputLtr, false);
                Util.setTextBlank(inputTotal);
                Util.setTextBlank(calPrice);*/
            }
        } else {
           /* Util.iSenableButton(this, btnSubmit, false);
            Util.setBlankAndIsEnable(inputLtr, false);
            Util.setTextBlank(inputTotal);
            Util.setTextBlank(calPrice);
            errorMessage = Constants.NO_SET;
            errorMessage(errorMessage);*/
        }
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
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
