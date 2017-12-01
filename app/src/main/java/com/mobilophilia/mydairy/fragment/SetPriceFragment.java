package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.activity.PriceTableActivity;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.SetPriceEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yogen on 18-07-2017.
 */

public class SetPriceFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private Button btnSave;
    private Button btnCancel;
    private TableLayout tableLayoutView;

    private EditText lowFat;
    private EditText highFat;
    private EditText lowSnf;
    private EditText highSnf;
    private EditText fatInterval;
    private EditText snfInterval;
    private EditText startPrice;


    private DBHelper dbHelper;
    private int recordId = 0;
    private int mtype = 0, mTiming = 0;
    private List<SetPriceEntry> savedPrice = new ArrayList<>();
    private LinearLayout llLable;
    private Spinner mySpinner_cb;
    private Spinner mySpinner_me;
    private String errorMessage = "";

    public SetPriceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setprice, container, false);
        llLable = (LinearLayout) rootView.findViewById(R.id.ll_lable);


        dbHelper = new DBHelper(getActivity());
        btnSave = (Button) rootView.findViewById(R.id.btn_save);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        lowFat = (EditText) rootView.findViewById(R.id.edt_low_fat);
        highFat = (EditText) rootView.findViewById(R.id.edt_high_fat);
        lowSnf = (EditText) rootView.findViewById(R.id.edt_low_snf);
        highSnf = (EditText) rootView.findViewById(R.id.edt_high_snf);
        startPrice = (EditText) rootView.findViewById(R.id.edt_strat_price);
        snfInterval = (EditText) rootView.findViewById(R.id.edt_interval);
        fatInterval = (EditText) rootView.findViewById(R.id.edt_interval_fat);


        Util.setBlankAndIsEnable(lowFat, true);
        Util.setBlankAndIsEnable(highFat, false);
        Util.setBlankAndIsEnable(lowSnf, false);
        Util.setBlankAndIsEnable(highSnf, false);
        Util.setBlankAndIsEnable(startPrice, false);
        Util.setBlankAndIsEnable(snfInterval, false);
        Util.setBlankAndIsEnable(fatInterval, false);
        Util.iSenableButton(getActivity(), btnSave, false);


        tableLayoutView = (TableLayout) rootView.findViewById(R.id.table_view_added);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        savedPrice = dbHelper.getAllSetPrice();
        if (savedPrice.size() > 0) {
            llLable.setVisibility(View.VISIBLE);
            makeTableforEnterData(savedPrice);
        } else {
            llLable.setVisibility(View.GONE);
            llLable.setVisibility(View.GONE);
        }

        mySpinner_cb = (Spinner) rootView.findViewById(R.id.spinner_type_cb);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.sp_item, Constants.type);
        mySpinner_cb.setAdapter(adapter);
        mySpinner_cb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mtype = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mySpinner_me = (Spinner) rootView.findViewById(R.id.spinner_me);
        ArrayAdapter<String> adapterMe = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.sp_item, Constants.timing);
        mySpinner_me.setAdapter(adapterMe);
        mySpinner_me.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTiming = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fatInterval.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String fatIn = fatInterval.getText().toString().trim();
                if (!Util.isEmpty(fatIn)) {
                    if (fatIn.length() >= 2) {
                        Double intrVal = Double.parseDouble(fatIn);
                        if (intrVal > 10) {
                            errorMessage = Constants.ERROR_INTERVAL_INVALID;
                            errorMessage(errorMessage);
                            Util.setBlankAndIsEnable(snfInterval, false);
                            Util.setBlankAndIsEnable(startPrice, false);
                            Util.iSenableButton(getActivity(), btnSave, false);
                        } else {
                            Util.setBlankAndIsEnable(snfInterval, true);
                            Util.setBlankAndIsEnable(startPrice, false);
                            Util.iSenableButton(getActivity(), btnSave, false);
                        }
                    }
                } else {
                    Util.setBlankAndIsEnable(snfInterval, false);
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





        // interval.setText("0.");
        //Selection.setSelection(interval.getText(), interval.getText().length());
        snfInterval.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String intervalD = snfInterval.getText().toString().trim();
                if (!Util.isEmpty(intervalD)) {
                    if (intervalD.length() >= 2) {
                        Double intrVal = Double.parseDouble(intervalD);
                        if (intrVal > 10) {
                            errorMessage = Constants.ERROR_INTERVAL_INVALID;
                            errorMessage(errorMessage);
                            Util.setBlankAndIsEnable(startPrice, false);
                            Util.iSenableButton(getActivity(), btnSave, false);
                        } else {
                            Util.setBlankAndIsEnable(startPrice, true);
                            Util.iSenableButton(getActivity(), btnSave, false);
                        }
                    }
                } else {
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
               /* if (!s.toString().startsWith("0.")) {
                    interval.setText("0.");
                    Selection.setSelection(interval.getText(), interval.getText().length());
                }

                String intervalD = interval.getText().toString();
                if (intervalD.length() > 2) {
                    Double lP = Double.parseDouble(intervalD);
                    if (lP < 0.1) {
                        errorMessage = Constants.ERROR_INTERVAL_BLANK;
                        errorMessage(errorMessage);
                        Util.iSenableButton(getActivity(), btnSave, false);
                    } else {
                        Util.iSenableButton(getActivity(), btnSave, true);
                    }
                }else {
                    Util.iSenableButton(getActivity(), btnSave, false);
                }*/

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


// Low Fat
        lowFat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fat = lowFat.getText().toString().trim();
                if (!Util.isEmpty(fat)) {
                    Double lFat = Double.parseDouble(fat);
                    if (lFat > 11) {

                        Util.setBlankAndIsEnable(highFat, false);
                        Util.setBlankAndIsEnable(lowSnf, false);
                        Util.setBlankAndIsEnable(highSnf, false);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);
                        errorMessage = Constants.ERROR_FAT;
                        errorMessage(errorMessage);

                    } else {
                        Util.setBlankAndIsEnable(highFat, true);
                        Util.setBlankAndIsEnable(lowSnf, false);
                        Util.setBlankAndIsEnable(highSnf, false);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);
                    }
                } else {
                    Util.setBlankAndIsEnable(highFat, false);
                    Util.setBlankAndIsEnable(lowSnf, false);
                    Util.setBlankAndIsEnable(highSnf, false);
                    Util.setBlankAndIsEnable(fatInterval, false);
                    Util.setBlankAndIsEnable(snfInterval, false);
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // High Fat
        highFat.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fat = highFat.getText().toString().trim();
                if (!Util.isEmpty(fat)) {
                    Double lFat = Double.parseDouble(fat);
                    if (lFat > 11) {
                        errorMessage = Constants.ERROR_FAT;
                        errorMessage(errorMessage);

                        Util.setBlankAndIsEnable(lowSnf, false);
                        Util.setBlankAndIsEnable(highSnf, false);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);

                    } else {
                        Util.setBlankAndIsEnable(lowSnf, true);
                        Util.setBlankAndIsEnable(highSnf, false);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);
                    }
                } else {
                    Util.setBlankAndIsEnable(lowSnf, false);
                    Util.setBlankAndIsEnable(highSnf, false);
                    Util.setBlankAndIsEnable(fatInterval, false);
                    Util.setBlankAndIsEnable(snfInterval, false);
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Low SNF
        lowSnf.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String snf = lowSnf.getText().toString().trim();
                if (!Util.isEmpty(snf)) {
                    Double lFat = Double.parseDouble(snf);
                    if (lFat > 13) {
                        errorMessage = Constants.ERROR_SNF;
                        errorMessage(errorMessage);

                        Util.setBlankAndIsEnable(highSnf, false);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);

                    } else {
                        Util.setBlankAndIsEnable(highSnf, true);
                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);
                    }
                } else {
                    Util.setBlankAndIsEnable(highSnf, false);
                    Util.setBlankAndIsEnable(fatInterval, false);
                    Util.setBlankAndIsEnable(snfInterval, false);
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // High SNF
        highSnf.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String snf = highSnf.getText().toString().trim();
                if (!Util.isEmpty(snf)) {
                    Double dSnf = Double.parseDouble(snf);
                    if (dSnf > 13) {
                        errorMessage = Constants.ERROR_SNF;
                        errorMessage(errorMessage);

                        Util.setBlankAndIsEnable(fatInterval, false);
                        Util.setBlankAndIsEnable(snfInterval, false);
                        Util.setBlankAndIsEnable(startPrice, false);
                        Util.iSenableButton(getActivity(), btnSave, false);

                    } else {
                        String lFat = lowFat.getText().toString().trim();
                        String hFat = highFat.getText().toString().trim();
                        String lsnf = lowSnf.getText().toString().trim();
                        String hsnf = highSnf.getText().toString().trim();

                        SetPriceEntry spe = new SetPriceEntry();
                        spe.setLowFat(Double.parseDouble(lFat));
                        spe.setHighFat(Double.parseDouble(hFat));
                        spe.setLowSnf(Double.parseDouble(lsnf));
                        spe.setHighSnf(Double.parseDouble(hsnf));
                        spe.setType(mtype);
                        spe.setTime(mTiming);

                        boolean isDublicate = dbHelper.isDublicateRecort(spe);
                        if (!isDublicate) {
                            Util.setBlankAndIsEnable(fatInterval, true);
                            Util.setBlankAndIsEnable(snfInterval, false);
                            Util.setBlankAndIsEnable(startPrice, false);
                            Util.iSenableButton(getActivity(), btnSave, false);
                        } else {
                            Util.setBlankAndIsEnable(fatInterval, false);
                            Util.setBlankAndIsEnable(snfInterval, false);
                            Util.setBlankAndIsEnable(startPrice, false);
                            Util.iSenableButton(getActivity(), btnSave, false);
                            errorMessage = Constants.ERROR_SET_PRICE_DUBLICATION;
                            errorMessage(errorMessage);
                        }
                    }
                } else {
                    Util.setBlankAndIsEnable(fatInterval, false);
                    Util.setBlankAndIsEnable(snfInterval, false);
                    Util.setBlankAndIsEnable(startPrice, false);
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // Set Price
        startPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String price = startPrice.getText().toString().trim();
                if (!Util.isEmpty(price)) {
                    Double priceD = Double.parseDouble(price);
                    if (priceD < 1) {
                        errorMessage = Constants.ERROR_PRICE;
                        errorMessage(errorMessage);
                        Util.iSenableButton(getActivity(), btnSave, false);
                    } else {
                        Util.iSenableButton(getActivity(), btnSave, true);
                    }
                } else {
                    Util.iSenableButton(getActivity(), btnSave, false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return rootView;
    }

    private void makeTableforEnterData(List<SetPriceEntry> priceList) {

        if (priceList.size() > 0) {
            llLable.setVisibility(View.VISIBLE);
        } else {
            llLable.setVisibility(View.GONE);
        }
        //
        for (int i = 0; i < priceList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            LinearLayout row = (LinearLayout) inflater.inflate(R.layout.row_price, null);
            if (i % 2 == 0) {
                row.setBackgroundColor(getResources().getColor(R.color.back_theme));
            } else {
                row.setBackgroundColor(getResources().getColor(R.color.con_theme));
            }
            final SetPriceEntry spe = priceList.get(i);

            final TextView lfat = (TextView) row.findViewById(R.id.lv1_one);
            lfat.setText(String.format("%.1f", spe.getLowFat()));

            final TextView hfat = (TextView) row.findViewById(R.id.lv1_two);
            hfat.setText(String.format("%.1f", spe.getHighFat()));

            final TextView lsnf = (TextView) row.findViewById(R.id.lv1_three);
            lsnf.setText(String.format("%.1f", spe.getLowSnf()));

            final TextView hsnf = (TextView) row.findViewById(R.id.lv1_four);
            hsnf.setText(String.format("%.1f", spe.getHighSnf()));

            final TextView fatInterval = (TextView) row.findViewById(R.id.lv1_six);
            fatInterval.setText(String.format("%.2f", spe.getFatInterval()));

            final TextView snfInterval = (TextView) row.findViewById(R.id.lv1_seven);
            snfInterval.setText(String.format("%.2f", spe.getSnfInterval()));

            final TextView sprice = (TextView) row.findViewById(R.id.lv1_five);
            sprice.setText(String.format("%.2f", spe.getStartPrice()));

            final TextView typeTime = (TextView) row.findViewById(R.id.lv1_type_time);
            typeTime.setText(getTypeTimeToSelection(spe.getType(), spe.getTime()));

            tableLayoutView.addView(row);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordId = spe.getId();
                    Intent priceIntent = new Intent(getActivity(), PriceTableActivity.class);
                    priceIntent.putExtra(Constants.RECORD_ID_TABLE, recordId);
                    startActivity(priceIntent);
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    launchMessageDialog(spe, lfat, hfat, lsnf, hsnf, sprice, fatInterval,snfInterval);
                    return true;
                }
            });
        }
    }

    public void launchMessageDialog(final SetPriceEntry spe, final TextView lfat, final TextView hfat, final TextView lsnf, final TextView hsnf, final TextView sprice, final TextView fatInt,final TextView snfInt) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_update_delete_price_set);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setCancelable(true);

        TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText("Do you want 'Update / Delete' this price set?");

        Button btnUpdate = (Button) dialog.findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recordId = spe.getId();
                lowFat.setText(lfat.getText());
                highFat.setText(hfat.getText());
                lowSnf.setText(lsnf.getText());
                highSnf.setText(hsnf.getText());
                fatInterval.setText(fatInt.getText());
                snfInterval.setText(snfInt.getText());
                startPrice.setText(sprice.getText());
                btnSave.setText("Update");

                mtype = spe.getType();
                mTiming = spe.getTime();

                mySpinner_cb.setSelection(mtype);
                mySpinner_me.setSelection(mTiming);
                Util.iSenableButton(getActivity(), btnSave, true);
                lowFat.requestFocus();

                dialog.dismiss();
            }
        });
        Button btnDelete = (Button) dialog.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deletePriceWithId(spe);
                savedPrice = dbHelper.getAllSetPrice();
                if (savedPrice.size() > 0) {
                    tableLayoutView.removeAllViews();
                    makeTableforEnterData(savedPrice);
                }
                Toast.makeText(getActivity(), "Selected price set has been deleted successfully", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_save:
                if (btnSave.getText().toString().equalsIgnoreCase("Save")) {
                    Util.hideKeyboard(getActivity());
                    saveEntry(0);
                } else {
                    Util.hideKeyboard(getActivity());
                    saveEntry(recordId);
                }
                break;
            case R.id.btn_cancel:
                Util.hideKeyboard(getActivity());
                reSetField();
                break;

            default:
                break;
        }
    }

    private void saveEntry(int id) {
        if (feildValidation()) {
            saveData(id);
        } else {
            errorMessage = Constants.FIELD_MAINDATORY;
            errorMessage(errorMessage);
        }
    }

    private void saveData(int updateId) {

        SetPriceEntry spe = new SetPriceEntry();
        spe.setLowFat(Double.parseDouble(lowFat.getText().toString()));
        spe.setHighFat(Double.parseDouble(highFat.getText().toString()));
        spe.setLowSnf(Double.parseDouble(lowSnf.getText().toString()));
        spe.setHighSnf(Double.parseDouble(highSnf.getText().toString()));
        spe.setStartPrice(Double.parseDouble(startPrice.getText().toString()));
        spe.setFatInterval(Double.parseDouble(fatInterval.getText().toString()));
        spe.setSnfInterval(Double.parseDouble(snfInterval.getText().toString()));

        spe.setType(mtype);
        spe.setTime(mTiming);

        if (updateId == 0) {
            dbHelper.createSetPriceEntry(spe);
        } else {
            spe.setId(recordId);
            dbHelper.updatePriceWithId(spe);
        }
        savedPrice = dbHelper.getAllSetPrice();
        if (savedPrice.size() > 0) {
            tableLayoutView.removeAllViews();
            makeTableforEnterData(savedPrice);
        }
        reSetField();
    }


    private boolean feildValidation() {
        boolean rtn = true;
        if (Util.isEmpty(lowFat.getText().toString())) {
            rtn = false;
        } else if (Util.isEmpty(highFat.getText().toString())) {
            rtn = false;
        } else if (Util.isEmpty(lowSnf.getText().toString())) {
            rtn = false;
        } else if (Util.isEmpty(highSnf.getText().toString())) {
            rtn = false;
        } else if (Util.isEmpty(startPrice.getText().toString())) {
            rtn = false;
        } else if (Util.isEmpty(fatInterval.getText().toString())) {
            rtn = false;
        }else if (Util.isEmpty(snfInterval.getText().toString())) {
            rtn = false;
        }
        return rtn;
    }

    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    private void reSetField() {

        Util.setBlankAndIsEnable(lowFat, true);
        Util.setBlankAndIsEnable(highFat, false);
        Util.setBlankAndIsEnable(lowSnf, false);
        Util.setBlankAndIsEnable(highSnf, false);
        Util.setBlankAndIsEnable(startPrice, false);
        Util.setBlankAndIsEnable(fatInterval, false);
        Util.setBlankAndIsEnable(snfInterval, false);
        Util.iSenableButton(getActivity(), btnSave, false);
        btnSave.setText("Save");
        recordId = 0;
        mtype = 0;
        mTiming = 0;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public static String getTypeTimeToSelection(int type, int time) {
        String txtTimeTye = "";
        if (type == 0 && time == 0) {
            txtTimeTye = "C-M";
        } else if (type == 0 && time == 1) {
            txtTimeTye = "C-E";
        } else if (type == 1 && time == 0) {
            txtTimeTye = "B-M";
        } else if (type == 1 && time == 1) {
            txtTimeTye = "B-E";
        }
        return txtTimeTye;
    }
}
