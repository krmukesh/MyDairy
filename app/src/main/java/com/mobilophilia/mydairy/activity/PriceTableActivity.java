package com.mobilophilia.mydairy.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.SetPriceEntry;
import com.mobilophilia.mydairy.fragment.SetPriceFragment;
import com.mobilophilia.mydairy.timer.TimeoutActivity;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yogen on 19-07-2017.
 */

public class PriceTableActivity extends TimeoutActivity {

    private TableLayout tableLayoutGenrated;
    private DBHelper dbHelper;
    private double startPirce;
    private double fatInterval;
    private double snfInterval;
    private double lFat;
    private double hFat;
    private double lSnf;
    private double hHsnf;
    private String typeTiming;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_price_table);
        tableLayoutGenrated = (TableLayout) findViewById(R.id.table_genrated_table);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int recordId = extras.getInt(Constants.RECORD_ID_TABLE);
            dbHelper = new DBHelper(PriceTableActivity.this);
            SetPriceEntry spe = dbHelper.getSetPriceFromId(recordId);

            startPirce = spe.getStartPrice();
            fatInterval = spe.getFatInterval();
            snfInterval = spe.getSnfInterval();
            lFat = spe.getLowFat();
            hFat = spe.getHighFat();
            lSnf = spe.getLowSnf();
            hHsnf = spe.getHighSnf();
            typeTiming = SetPriceFragment.getTypeTimeToSelection(spe.getType(), spe.getTime());

            ((TextView) findViewById(R.id.tv_lfat)).setText("" + lFat);
            ((TextView) findViewById(R.id.tv_hfat)).setText("" + hFat);
            ((TextView) findViewById(R.id.tv_lsnf)).setText("" + lSnf);
            ((TextView) findViewById(R.id.tv_hsnf)).setText("" + hHsnf);
            ((TextView) findViewById(R.id.tv_price)).setText("" + String.format("%.2f", startPirce));
            ((TextView) findViewById(R.id.tv_interval_fat)).setText("" + String.format("%.2f", fatInterval));
            ((TextView) findViewById(R.id.tv_interval_snf)).setText("" + String.format("%.2f", snfInterval));
            ((TextView) findViewById(R.id.tv_type_timing)).setText(typeTiming);
            init();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    public void init() {
        TextView cell;
        int colomCount, rowCount;
        double ds = (hHsnf - lSnf);
        double df = (hFat - lFat);
        double iC = ds / 0.1;
        double jR = df / 0.1;
        colomCount = (int) iC + 2;
        rowCount = (int) jR + 2;

        double price;
        for (int i = 0; i < rowCount; i++) {
            TableRow row = new TableRow(this);
            price = startPirce;
            for (int j = 0; j < colomCount; j++) {
                cell = new TextView(this);
                cell.setPadding(10, 10, 10, 10);
                cell.setBackgroundResource(R.drawable.border_row);

                if (i == 0 && j == 0) {
                    cell.setText("F/S");
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setTextColor(getResources().getColor(R.color.colorAccent));
                } else if (i == 0 && j > 0) {
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setTextColor(getResources().getColor(R.color.gold));
                    String ss = String.format("%.1f", lSnf);
                    cell.setText("" + ss);
                    lSnf = lSnf + 0.1d;
                } else if (i > 0 && j == 0) {
                    cell.setTypeface(null, Typeface.BOLD);
                    cell.setTextColor(getResources().getColor(R.color.color_col));
                    String ss = String.format("%.1f", lFat);
                    cell.setText("" + ss);
                    lFat = lFat + 0.1d;
                } else if (i == 1 && j == 1) {
                    cell.setTextColor(getResources().getColor(R.color.white));
                    String ss = String.format("%.2f", price);
                    cell.setText("" + ss);
                    price = price + snfInterval;
                } else {
                    cell.setTextColor(getResources().getColor(R.color.white));
                    String ss = String.format("%.2f", price);
                    cell.setText("" + ss);
                    price = price + snfInterval;
                }
                row.addView(cell);
            }
            tableLayoutGenrated.addView(row);
            if (i != 0) {
                startPirce = startPirce + fatInterval;
            }
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