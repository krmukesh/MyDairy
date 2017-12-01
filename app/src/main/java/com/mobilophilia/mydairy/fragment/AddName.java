package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;

/**
 * Created by mukesh on 06/08/17.
 */

public class AddName extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DBHelper dbHelper;
    private Button btnSave;
    private View rootView;

    private TextView inPutCode;
    private EditText inPutName;
    private EditText inPutPhone;
    private int positionDropDown = 0;

    private String errorMessage = "";

    public AddName() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_name, container, false);
        inPutCode = (TextView) rootView.findViewById(R.id.input_code);
        inPutName = (EditText) rootView.findViewById(R.id.input_name);
        inPutPhone = (EditText) rootView.findViewById(R.id.input_phone);

        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        dbHelper = new DBHelper(getActivity());
        lastEnterdCode();

        Spinner mySpinner = (Spinner) rootView.findViewById(R.id.spinner_cow_buffalo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.sp_item, Constants.type);
        mySpinner.setOnItemSelectedListener(this);
        mySpinner.setAdapter(adapter);


        Util.setBlankAndIsEnable(inPutName, true);
        Util.setBlankAndIsEnable(inPutPhone, false);
        Util.iSenableButton(getActivity(), btnSave, false);

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
                        Util.iSenableButton(getActivity(), btnSave, false);
                    } else {
                        Util.setBlankAndIsEnable(inPutPhone, false);
                        Util.iSenableButton(getActivity(), btnSave, false);
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
                    Util.iSenableButton(getActivity(), btnSave, true);
                }

               /* String phone = inPutPhone.getText().toString().trim();
                if (Util.isValidPhoneNumber(phone)) {
                    Util.iSenableButton(getActivity(), btnSave, true);
                } else {
                    Util.iSenableButton(getActivity(), btnSave, false);
                    errorMessage = Constants.ERROR_VALIDATION_PHONE;
                    Util.validatioMessage(rootView, errorMessage);
                }*/
            }
        });


        // Inflate the layout for this fragment
        return rootView;
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
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_save:
                Util.hideKeyboard(getActivity());
                if(feildValidation()){
                    saveEntry();
                }else{
                    Util.validatioMessage(rootView, errorMessage);
                }
                break;
            default:
                break;
        }
    }


    private void lastEnterdCode() {
        int lastCode = dbHelper.getLastEnterCode();
        if (lastCode >= 0) {
            inPutCode.setText("" + (lastCode + 1));
        }
    }


    private void saveEntry() {
        EnterNameEntry ene = new EnterNameEntry();
        ene.setNameCode(Integer.parseInt(inPutCode.getText().toString().trim()));
        ene.setEnterName(inPutName.getText().toString().trim());
        ene.setType(positionDropDown);
        ene.setPhoneNo(inPutPhone.getText().toString().trim());
        dbHelper.createEnterNameEntry(ene);
        reSetField();
        positionDropDown = 0;
    }


    private void reSetField() {
        inPutCode.setText("");
        Util.setBlankAndIsEnable(inPutName, true);
        Util.setBlankAndIsEnable(inPutPhone, false);
        lastEnterdCode();
        Util.iSenableButton(getActivity(), btnSave, false);
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

