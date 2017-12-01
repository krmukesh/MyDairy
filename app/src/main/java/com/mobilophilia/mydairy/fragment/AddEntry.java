package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.database.MyEntries;
import com.mobilophilia.mydairy.database.SetPriceEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanji on 8/2/2017.
 */

public class AddEntry extends Fragment implements View.OnClickListener {

    private View rootView;
    private DBHelper dbHelper;
    private TextView customerName;
    private TextView customerPhone;
    private EditText inputCode;
    private EditText inputClr;
    private EditText inputFat;
    private EditText inputLtr;
    private TextView inputTotal;
    private String errorMessage;
    private Button btnSubmit;
    private int milkType;

    public Double calculatedSNF = 0d;
    private TextView calPrice;
    private String setTotalPrice;
    private Double calculatedPrice;
    private int id;

    public AddEntry() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_entry, container, false);


        dbHelper = new DBHelper(getActivity());

        customerName = (TextView) rootView.findViewById(R.id.customer_name);
        customerPhone = (TextView) rootView.findViewById(R.id.customer_phone);
        inputCode = (EditText) rootView.findViewById(R.id.input_code);

        inputClr = (EditText) rootView.findViewById(R.id.input_clr);
        inputFat = (EditText) rootView.findViewById(R.id.input_fat);
        inputLtr = (EditText) rootView.findViewById(R.id.input_ltr);
        inputTotal = (TextView) rootView.findViewById(R.id.input_total);
        calPrice = (TextView) rootView.findViewById(R.id.cal_price);

        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        Util.setBlankAndIsEnable(inputCode, true);
        Util.setBlankAndIsEnable(inputClr, false);
        Util.setBlankAndIsEnable(inputFat, false);
        Util.setBlankAndIsEnable(inputLtr, false);
        Util.iSenableButton(getActivity(), btnSubmit, false);


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
                        customerName.setTag(ene.getNameCode());
                        customerPhone.setText(ene.getPhoneNo());
                        milkType = ene.getType();
                        if(ene.getEntry()){
                            Util.setBlankAndIsEnable(inputClr, false);
                            Util.setBlankAndIsEnable(inputFat, false);
                            Util.setBlankAndIsEnable(inputLtr, false);
                            Util.setTextBlank(inputTotal);
                            Util.setTextBlank(calPrice);
                            errorMessage = Constants.ERROR_ENTRY_ADDED;
                            errorMessage(errorMessage);
                        }else {
                            Util.setBlankAndIsEnable(inputClr, true);
                            Util.setBlankAndIsEnable(inputFat, false);
                            Util.setBlankAndIsEnable(inputLtr, false);
                            Util.setTextBlank(inputTotal);
                            Util.setTextBlank(calPrice);
                        }
                    } else {
                        Util.setTextBlank(customerName);
                        Util.setTextBlank(customerPhone);
                        Util.setBlankAndIsEnable(inputClr, false);
                        Util.setBlankAndIsEnable(inputFat, false);
                        Util.setBlankAndIsEnable(inputLtr, false);
                        Util.setTextBlank(inputTotal);
                        Util.setTextBlank(calPrice);
                        errorMessage = Constants.ERROR_CODE_INVALID;
                        errorMessage(errorMessage);
                    }
                } else {
                    Util.setTextBlank(customerName);
                    Util.setTextBlank(customerPhone);
                    Util.setBlankAndIsEnable(inputClr, false);
                    Util.setBlankAndIsEnable(inputFat, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);
                    //errorMessage = Constants.ERROR_CODE_INVALID_BLANK;
                    //errorMessage(errorMessage);
                }
                // TODO Auto-generated method stub
            }
        });


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
                    } else {
                        Util.setBlankAndIsEnable(inputFat, true);
                        Util.setBlankAndIsEnable(inputLtr, false);
                        Util.setTextBlank(inputTotal);
                        Util.setTextBlank(calPrice);
                    }
                } else {
                    //errorMessage = Constants.ERROR_VALIDATION_CLR_BLANK;
                    //errorMessage(errorMessage);
                    Util.setBlankAndIsEnable(inputFat, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);
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
                        Util.setTextBlank(inputTotal);
                        Util.setTextBlank(calPrice);
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
                            Util.iSenableButton(getActivity(), btnSubmit, false);
                            Util.setBlankAndIsEnable(inputLtr, false);
                            Util.setTextBlank(inputTotal);
                            Util.setTextBlank(calPrice);
                        } else {
                            List<SetPriceEntry> arr = dbHelper.getPriceSetForGivenSNF(milkType, Util.getTime(), cSNF);
                            if (arr.size() > 0) {
                                Log.d("arrarrarrarrarr", "Type>= " + milkType + " , Fat >= " + fat + " , CSNF >= " + Double.valueOf(cSNF));

                                List<SetPriceEntry> arrData = dbHelper.getSetPriceTypeAndTime(milkType, Util.getTime(), fat, "" + Double.valueOf(cSNF));
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
                                        //calculatedPrice = Util.getPrice(baseprice, Double.valueOf(fat), snf, intervl, lowFat, highFat, lowSnf);
                                        calculatedPrice = Util.getPrice(baseprice, Double.valueOf(fat), snf, fatIntervl, snfIntervl,lowFat, highFat, lowSnf,highSnf);
                                        Log.e("calculatedPrice", "calculatedPrice>= " +calculatedPrice + "> fat-> )"+Double.valueOf(fat)+"- snf-> "+snf);
                                        //getPrice(Double basePrice, Double inputFat, Double calSnf, Double fatInterval, Double snfInterval, Double lowFat, Double highFat, Double lowSnf, Double HighSnf) {

                                        Util.iSenableButton(getActivity(), btnSubmit, false);
                                        Util.setBlankAndIsEnable(inputLtr, true);
                                        Util.setTextBlank(inputTotal);
                                        Util.setTextBlank(calPrice);
                                    } else {
                                        errorMessage = Constants.NO_SET;
                                        errorMessage(errorMessage);
                                        Util.iSenableButton(getActivity(), btnSubmit, false);
                                        Util.setBlankAndIsEnable(inputLtr, false);
                                        Util.setTextBlank(inputTotal);
                                        Util.setTextBlank(calPrice);
                                    }
                                } else {
                                    errorMessage = Constants.NO_SET;
                                    errorMessage(errorMessage);
                                    Util.iSenableButton(getActivity(), btnSubmit, false);
                                    Util.setBlankAndIsEnable(inputLtr, false);
                                    Util.setTextBlank(inputTotal);
                                    Util.setTextBlank(calPrice);
                                }
                            } else {
                                Util.iSenableButton(getActivity(), btnSubmit, false);
                                Util.setBlankAndIsEnable(inputLtr, false);
                                Util.setTextBlank(inputTotal);
                                Util.setTextBlank(calPrice);
                                errorMessage = Constants.NO_SET;
                                errorMessage(errorMessage);
                            }
                        }
                    }
                } else {
                    Util.iSenableButton(getActivity(), btnSubmit, false);
                    Util.setBlankAndIsEnable(inputLtr, false);
                    Util.setTextBlank(inputTotal);
                    Util.setTextBlank(calPrice);
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
                    calPrice.setText("Price: " + priceCal);
                    inputTotal.setText("Total: " + setTotalPrice);
                    Util.iSenableButton(getActivity(), btnSubmit, true);
                } else {
                    Util.iSenableButton(getActivity(), btnSubmit, false);
                    inputTotal.setText("Total");
                    Util.setTextBlank(calPrice);
                    //errorMessage = Constants.ERROR_VALIDATION_LTR;
                    //errorMessage(errorMessage);
                }
            }
        });

        return rootView;
    }


    private void reSetValues() {
        Util.setBlankAndIsEnable(inputCode, true);
        Util.setBlankAndIsEnable(inputClr, false);
        Util.setBlankAndIsEnable(inputFat, false);
        Util.setBlankAndIsEnable(inputLtr, false);
        Util.setTextBlank(inputTotal);
        Util.setTextBlank(calPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_submit:
                if (feildValidation()) {
                    saveMyEntry();
                } else {
                    errorMessage = Constants.FIELD_MAINDATORY;
                    errorMessage(errorMessage);
                }
                break;
            default:
                break;
        }
    }

    private void saveMyEntry() {
        Util.hideKeyboard(getActivity());
        //Util.launchBarDialog(getActivity(), Constants.PROGRESS_DIALOG_ENTRY);
        MyEntries myEntries = new MyEntries();

        String code = inputCode.getText().toString().trim();
        String clr = inputClr.getText().toString().trim();
        String fat = inputFat.getText().toString().trim();
        String ltr = inputLtr.getText().toString().trim();

        myEntries.setCode(Integer.parseInt(code));
        myEntries.setName(customerName.getText().toString());
        myEntries.setClr(Double.valueOf(clr));
        myEntries.setFat(Double.valueOf(fat));
        myEntries.setLtr(Double.valueOf(ltr));
        myEntries.setSnf(Util.round0ffOnePlace(calculatedSNF));
        myEntries.setPrice(Util.round0ffTwoPlace(calculatedPrice));
        myEntries.setTotal(Double.valueOf(setTotalPrice));
        myEntries.setMorOrEveTime(Util.getTime());
        dbHelper.createEntry(myEntries);

        reSetValues();


        //AppManager.getInstance().getEntryManager().saveEntry(getActivity(), code, clr, fat, ltr, setTotalPrice);
    }


    private boolean feildValidation() {
        boolean rtn = true;
        if (Util.isEmpty(inputCode.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CODE;
            rtn = false;
        } else if (Util.isEmpty(inputClr.getText().toString())) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.ENTRY_SUCCESS:
                Util.dismissBarDialog();
                reSetValues();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;
            case MessageEvent.ENTRY_FAILURE:
                Util.dismissBarDialog();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;

            case MessageEvent.NETWORK_TIME_OUT:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;

            case MessageEvent.SERVER_ERROR_OCCURRED:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }



    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}