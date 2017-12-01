package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.database.ExpenseBean;
import com.mobilophilia.mydairy.manager.AppManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by mukesh on 16/08/17.
 */

public class AddExpense extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private View rootView;
    private DBHelper dbHelper;
    private TextView customerName;
    private TextView customerPhone;
    private EditText inputCode;
    private EditText inPutExpenses;
    private String errorMessage;
    private Button btnSubmit;
    private AppCompatCheckBox isDebitBox;
    private int positionDropDown = 0;

    public AddExpense() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);
        dbHelper = new DBHelper(getActivity());
        customerName = (TextView) rootView.findViewById(R.id.customer_name);
        customerPhone = (TextView) rootView.findViewById(R.id.customer_phone);
        inputCode = (EditText) rootView.findViewById(R.id.input_code);
        inPutExpenses = (EditText) rootView.findViewById(R.id.input_expenses);
        inPutExpenses.setEnabled(false);


        btnSubmit = (Button) rootView.findViewById(R.id.btn_expeanses);
        btnSubmit.setOnClickListener(this);

        Spinner mySpinner = (Spinner) rootView.findViewById(R.id.expense_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.sp_item, Constants.expense_type);
        mySpinner.setOnItemSelectedListener(this);
        mySpinner.setAdapter(adapter);

        Util.setBlankAndIsEnable(inPutExpenses, false);
        Util.iSenableButton(getActivity(), btnSubmit, false);

        isDebitBox = (AppCompatCheckBox) rootView.findViewById(R.id.cb_isdebit);
        isDebitBox.setChecked(false);
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
                    Util.iSenableButton(getActivity(), btnSubmit, true);
                } else {
                    Util.iSenableButton(getActivity(), btnSubmit, false);
                    isDebitBox.setChecked(false);
                }
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
            case R.id.btn_expeanses:
                if (feildValidation()) {
                    submitExpenses();
                } else {
                    errorMessage(errorMessage);
                }
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

    private void submitExpenses() {
        String code = inputCode.getText().toString().trim();
        String expenses = inPutExpenses.getText().toString().trim();
        ExpenseBean bean = new ExpenseBean();
        bean.setCode(Integer.parseInt(code));
        bean.setExpenseType(positionDropDown);
        bean.setExpense(Double.parseDouble(expenses));

        DBHelper dbHelper = new DBHelper(getActivity());
        dbHelper.createExpense(bean);

        reSetValues();

        // Util.launchBarDialog(getActivity(), Constants.PROGRESS_DIALOG_EXPENSES);
        // AppManager.getInstance().getExpenseManager().saveEXpenes(getActivity(), code, expenses);
    }

   /* private boolean feildValidation() {
        boolean rtn = true;
        if (Util.isEmpty(inputCode.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CODE;
            rtn = false;
        } else if (Util.isEmpty(inPutExpenses.getText().toString())) {
            errorMessage = Constants.ERROR_VALIDATION_CLR;
            rtn = false;
        }
        return rtn;
    }*/

    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    private void reSetValues() {
        Util.setBlankAndIsEnable(inputCode, true);
        Util.setBlankAndIsEnable(inPutExpenses, false);
        Util.iSenableButton(getActivity(), btnSubmit, false);
        isDebitBox.setChecked(false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.EXPENSES_SUCCESS:
                Util.dismissBarDialog();
                reSetValues();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;
            case MessageEvent.EXPENSES_FAILURE:
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
