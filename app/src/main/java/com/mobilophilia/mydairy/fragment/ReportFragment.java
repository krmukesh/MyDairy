package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.MyEntries;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yogen on 13-07-2017.
 */

public class ReportFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView startDate;
    private TextView endDate;
    private TextView reportOnlyDate;

    private LinearLayout llTabDate;
    private LinearLayout llTabCode;

    private int mEndYear;
    private int mEndMonth;
    private int mEndDay;

    private int mStartYear;
    private int mStartMonth;
    private int mStartDay;
    private DatePickerDialog startDialog;
    private Button btnDwldReport;
    private EditText codeOne;
    private EditText codeTwo;
    private View rootView;
    private String errorMessage;
    private SwitchCompat mySwitchTab;
    private boolean checkReportType = false;

    private DatePickerDialog.OnDateSetListener startDateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mStartYear = year;
                    mStartMonth = monthOfYear;
                    mStartDay = dayOfMonth;
                    updateDisplay();
                }
            };
    private DatePickerDialog.OnDateSetListener endDateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mEndYear = year;
                    mEndMonth = monthOfYear;
                    mEndDay = dayOfMonth;
                    updateEndDate();
                }
            };
    private DatePickerDialog.OnDateSetListener reportDateSetListener = new
            DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mStartYear = year;
                    mStartMonth = monthOfYear;
                    mStartDay = dayOfMonth;
                    updateReportDateDisplay();
                }
            };


    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);


        codeOne = (EditText) rootView.findViewById(R.id.edt_code_one);
        codeTwo = (EditText) rootView.findViewById(R.id.edt_code_second);

        startDate = (TextView) rootView.findViewById(R.id.input_date_one);
        startDate.setOnClickListener(this);
        endDate = (TextView) rootView.findViewById(R.id.input_date_second);
        endDate.setOnClickListener(this);


        reportOnlyDate = (TextView) rootView.findViewById(R.id.input_date_select);
        reportOnlyDate.setOnClickListener(this);

        llTabCode = (LinearLayout) rootView.findViewById(R.id.tap_code_layout);
        llTabDate = (LinearLayout) rootView.findViewById(R.id.tap_date_layout);


        btnDwldReport = (Button) rootView.findViewById(R.id.btn_download_report);
        btnDwldReport.setOnClickListener(this);

        mySwitchTab = (SwitchCompat) rootView.findViewById(R.id.switch_tab);
        mySwitchTab.setOnCheckedChangeListener(this);

        Calendar c = Calendar.getInstance();
        mStartYear = c.get(Calendar.YEAR);
        mStartMonth = c.get(Calendar.MONTH);
        mStartDay = c.get(Calendar.DAY_OF_MONTH);

        Util.iSenableButton(getActivity(), btnDwldReport, false);
        changeTabView(0);
        Util.setBlankAndIsEnable(codeOne, true);
        Util.setBlankAndIsEnable(codeTwo, false);
        Util.setTVAndIsEnable(startDate, false);
        Util.setTVAndIsEnable(endDate, false);


        codeOne.addTextChangedListener(new TextWatcher() {
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

                String codeInterval1st = codeOne.getText().toString().trim();
                if (!Util.isEmpty(codeInterval1st)) {
                    Double code = Double.parseDouble(codeInterval1st);
                    if (code >= 1) {
                        Util.setEnable(codeTwo, true);
                        Util.iSenableButton(getActivity(), btnDwldReport, true);
                    } else {
                        Util.iSenableButton(getActivity(), btnDwldReport, false);
                    }
                } else {
                    Util.iSenableButton(getActivity(), btnDwldReport, false);
                }
            }

        });


        codeTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //isOnTextChangedTwo = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String codeInterval2nd = codeTwo.getText().toString().trim();

                if (!Util.isEmpty(codeInterval2nd)) {
                    Double code = Double.parseDouble(codeInterval2nd);
                    if (code >= 1) {
                        Util.setEnableTv(startDate, true);
                        String start = startDate.getText().toString().trim();
                        String end = endDate.getText().toString().trim();
                        if ((!Util.isEmpty(start)) && !Util.isEmpty(end)) {
                            Util.iSenableButton(getActivity(), btnDwldReport, true);
                        }
                    } else {
                        Util.iSenableButton(getActivity(), btnDwldReport, false);
                    }
                } else {
                    Util.iSenableButton(getActivity(), btnDwldReport, false);
                }
            }

        });
        // Inflate the layout for this fragment
        return rootView;
    }


    private boolean emptyValidation() {
        boolean rtn = true;

        String date = reportOnlyDate.getText().toString().trim();
        if (Util.isEmpty(date)) {
            errorMessage = Constants.SELECT_DATE;
            rtn = false;

        }

        return rtn;
    }


    private boolean zeroValidation() {
        boolean rtn = true;

        String startCode = codeOne.getText().toString().trim();
        String endCode = codeTwo.getText().toString().trim();
        if (!Util.isEmpty(startCode)) {
            Double dClr = Double.parseDouble(startCode);
            if (dClr < 1) {
                errorMessage = Constants.ERROR_VALIDATION_CODE;
                rtn = false;
            }
        }

        if (!Util.isEmpty(endCode)) {
            Double dLtr = Double.parseDouble(endCode);
            if (dLtr < 1) {
                errorMessage = Constants.ERROR_VALIDATION_CODE;
                rtn = false;
            }
        }
        return rtn;
    }

    private boolean inputValidation() {
        boolean rtn = true;
        String startCode = codeOne.getText().toString().trim();
        String endCode = codeTwo.getText().toString().trim();
        if (!Util.isEmpty(startCode)) {
            Double sCode = Double.parseDouble(startCode);
            Double eCode = Double.parseDouble(endCode);
            if (eCode < sCode) {
                errorMessage = Constants.ERROR_VALIDATION_END;
                rtn = false;
            }
        }
        return rtn;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.input_date_one:
                showCurrentDate(startDateSetListener);
                break;
            case R.id.input_date_second:
                showEndDate(endDateSetListener);
                break;
            case R.id.input_date_select:
                getReportDate(reportDateSetListener);
                break;
            case R.id.btn_download_report:
                downloadButtonEvent();
                break;
            default:
                break;
        }
    }


    private void downloadButtonEvent() {
        if (checkReportType) {
            genrateFromDateAndCodeReport();
        } else {
            genrateReportFromDate();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            changeTabView(1);
        } else {
            changeTabView(0);
        }
        checkReportType = isChecked;
    }


    private String getAgentId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SP_MY_DAIRY_MK, getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        return id;
    }


    private void genrateReportFromDate() {
        if (emptyValidation()) {

            try {
                String date = reportOnlyDate.getText().toString().trim();
                DBHelper dbHelper = new DBHelper(getActivity());
                List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();
                date = DBHelper.timeStampFromDate(date+" 00:00:00");
                arrayMyEntries = dbHelper.getAllEntriesForReportByDate("", "", date, "", true);
                if (arrayMyEntries.size() > 0) {
                    String path = Util.createAppFolder(getAgentId());
                    String[] info = new String[1];
                    info[0] = Util.getDate();
                    Util.createPDF("Report", getActivity(), arrayMyEntries, path, info, true);
                    clearText();
                } else {
                    errorMessage = Constants.NO_RECORD;
                    errorMessage(errorMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorMessage(errorMessage);

        }
    }

    private void genrateFromDateAndCodeReport() {
        if (zeroValidation()) {
            if (inputValidation()) {
                try {
                    DBHelper dbHelper = new DBHelper(getActivity());
                    List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();


                    String startCode = codeOne.getText().toString().trim();
                    String endCode = codeTwo.getText().toString().trim();
                    String sDate = startDate.getText().toString().trim();
                    String edate = endDate.getText().toString().trim();

                    arrayMyEntries = dbHelper.getAllEntriesForReportByDate(startCode, endCode, DBHelper.timeStampFromDate(sDate+" 00:00:00"), DBHelper.timeStampFromDate(edate+" 99:99:99"), false);
                    if (arrayMyEntries.size() > 0) {
                        String path = Util.createAppFolder(getAgentId());
                        String[] info = new String[5];
                        info[0] = startCode;
                        info[1] = endCode;
                        info[2] = sDate;
                        info[3] = edate;
                        Util.createPDF("Report", getActivity(), arrayMyEntries, path, info, false);
                        clearText();
                    } else {
                        errorMessage = Constants.NO_RECORD;
                        errorMessage(errorMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                errorMessage(errorMessage);
            }
        } else {
            errorMessage = Constants.ERROR_VALIDATION_CODE;
            errorMessage(errorMessage);
        }
    }


    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    private void showCurrentDate(DatePickerDialog.OnDateSetListener dateListenee) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        startDialog = new DatePickerDialog(getActivity(), dateListenee, year, month, day);
        startDialog.getDatePicker().setMaxDate(new Date().getTime());
        startDialog.show();
    }

    private void showEndDate(DatePickerDialog.OnDateSetListener dateListenee) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateListenee, year, month, day);
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.set(startDialog.getDatePicker().getYear(), startDialog.getDatePicker().getMonth(), startDialog.getDatePicker().getDayOfMonth(), 0, 0, 0);
        long startTime = calendar.getTimeInMillis();
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.getDatePicker().setMinDate(startTime);
        dialog.show();
    }

    private void getReportDate(DatePickerDialog.OnDateSetListener dateListenee) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog reporDialog = new DatePickerDialog(getActivity(), dateListenee, year, month, day);
        reporDialog.getDatePicker().setMaxDate(new Date().getTime());
        reporDialog.show();
    }

    private void changeTabView(int t) {
        if (t == 0) {
            Util.hideKeyboard(getActivity());
            llTabDate.setVisibility(View.VISIBLE);
            llTabCode.setVisibility(View.GONE);
            clearText();
        } else if (t == 1) {
            llTabDate.setVisibility(View.GONE);
            llTabCode.setVisibility(View.VISIBLE);
            clearText();
        }
    }


    private void clearText() {
        Util.setTextBlank(reportOnlyDate);
        Util.setTextBlank(startDate);
        Util.setTextBlank(endDate);
        Util.setBlankAndIsEnable(codeOne, true);
        Util.setBlankAndIsEnable(codeTwo, true);
        Util.iSenableButton(getActivity(), btnDwldReport, false);

    }

    private void updateDisplay() {
        mStartMonth = mStartMonth + 1;
        String dates ="";
        if(mStartDay<10){
            dates = "0"+mStartDay+"-";
        }else {
            dates = ""+mStartDay+"-";
        }
        if(mStartMonth<10){
            dates = dates+"0"+mStartMonth+"-" + mStartYear;
        }else {
            dates = dates+""+mStartMonth+"-" + mStartYear;
        }
       // String Dates = (mStartDay < 10 ? ("0" + mStartDay) : (mStartDay) + "-" + (mStartMonth < 10 ? ("0" + mStartMonth) : (mStartMonth)) + "-" + mStartYear);
        startDate.setText(dates);
        Util.setTVAndIsEnable(endDate, true);
        Util.iSenableButton(getActivity(), btnDwldReport, false);
    }

    private void updateReportDateDisplay() {
        mStartMonth = mStartMonth + 1;
        String dates ="";
        if(mStartDay<10){
            dates = "0"+mStartDay+"-";
        }else {
            dates = ""+mStartDay+"-";
        }
        if(mStartMonth<10){
            dates = dates+"0"+mStartMonth+"-" + mStartYear;
        }else {
            dates = dates+""+mStartMonth+"-" + mStartYear;
        }
       // String Dates = (mStartDay < 10 ? ("0" + mStartDay) : (mStartDay) + "-" + (mStartMonth < 10 ? ("0" + mStartMonth) : (mStartMonth)) + "-" + mStartYear);
        reportOnlyDate.setText(dates);
        Util.iSenableButton(getActivity(), btnDwldReport, true);
    }

    private void updateEndDate() {
        mEndMonth = mEndMonth + 1;
        String dates ="";
        if(mStartDay<10){
            dates = "0"+mEndDay+"-";
        }else {
            dates = ""+mEndDay+"-";
        }
        if(mEndMonth<10){
            dates = dates+"0"+mEndMonth+"-" + mEndYear;
        }else {
            dates = dates+""+mEndMonth+"-" + mEndYear;
        }
       // String Dates = (mEndDay < 10 ? ("0" + mEndDay) : (mEndDay) + "-" + (mEndMonth < 10 ? ("0" + mEndMonth) : (mEndMonth)) + "-" + mEndYear);
        endDate.setText(dates);
        Util.iSenableButton(getActivity(), btnDwldReport, true);
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
