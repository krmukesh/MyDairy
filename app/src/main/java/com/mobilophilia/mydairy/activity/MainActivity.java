package com.mobilophilia.mydairy.activity;

/**
 * Created by yogen on 12-07-2017.
 */

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.adapter.EntriesListByCodeAdapter;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.drawer.FragmentDrawer;
import com.mobilophilia.mydairy.fragment.MenuDownload;
import com.mobilophilia.mydairy.fragment.EnterName;
import com.mobilophilia.mydairy.fragment.Entry;
import com.mobilophilia.mydairy.fragment.Expense;
import com.mobilophilia.mydairy.fragment.MenuReport;
import com.mobilophilia.mydairy.fragment.ReportFragment;
import com.mobilophilia.mydairy.fragment.SetPriceFragment;
import com.mobilophilia.mydairy.manager.AppManager;
import com.mobilophilia.mydairy.timer.TimeoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends TimeoutActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private List<EnterNameEntry> customerList;

    private Toolbar mToolbar;
    private ImageView dwlCode;
    private FragmentDrawer drawerFragment;
    //private ImageView syncdata;
    private boolean isSync = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.add_toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dwlCode = (ImageView) mToolbar.findViewById(R.id.ic_dwl);
        //syncdata = (ImageView) mToolbar.findViewById(R.id.sync_btn);


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
        getPermission();

        Util.createAppFolder(getAgentId());

        dwlCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerList.size() > 0) {
                    String path = Util.createAppFolder(getAgentId());
                    Util.createCodeListPDF(MainActivity.this, customerList, path);
                }
            }
        });

       /* if (isSync) {
            syncdata.setVisibility(View.VISIBLE);
        } else {
            syncdata.setVisibility(View.GONE);
        }

        syncdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DBHelper(MainActivity.this).synchDataServer(MainActivity.this);
            }
        });*/


    }

    private String getAgentId() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        return id;
    }


    private void updateSyncTime(String time, boolean isSync) {
        SharedPreferences sharedPref = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isSync) {
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, isSync);
        } else {
            editor.putString(Constants.SP_SYNC_TIME_KEY, time);
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, isSync);
        }
        editor.apply();
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new EnterName();
                title = getString(R.string.title_name);
                break;
            case 1:
                fragment = new Entry();
                title = getString(R.string.title_entry);
                break;
            case 2:
                fragment = new Expense();
                title = getString(R.string.title_expenses);
                break;
            case 3:
                fragment = new SetPriceFragment();
                title = getString(R.string.title_price);
                break;
            case 4:
                fragment = new MenuReport();
                title = getString(R.string.title_report);
                break;
            case 5:
                if (new DBHelper(MainActivity.this).getSyncFlag()) {
                    if (new DBHelper(MainActivity.this).getCheckEntry()) {
                        launchMessageDialog();
                    } else {
                        AppManager.getInstance().getLoginManager().synService(MainActivity.this, new DBHelper(MainActivity.this).synchDataServer(MainActivity.this), false);
                    }
                }
                break;
            case 6:
                logoutCleanUp();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
        downLoadBtn(position);
    }


    public void launchMessageDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
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

        final TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText(Constants.DIALOG_ENTRY_SYNC);

        Button btnNo = (Button) dialog.findViewById(R.id.btn_update);
        btnNo.setText(Constants.DIALOG_BUTTON_TEXT_NO);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnYes = (Button) dialog.findViewById(R.id.btn_delete);
        btnYes.setText(Constants.DIALOG_BUTTON_TEXT_YES);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppManager.getInstance().getLoginManager().synService(MainActivity.this, new DBHelper(MainActivity.this).synchDataServer(MainActivity.this), true);

                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void downLoadBtn(int on) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        customerList = new ArrayList<>();
        customerList = dbHelper.getEnteredNameListForReport();
        if (on == 0 && customerList.size() > 0) {
            dwlCode.setVisibility(View.VISIBLE);
        } else {
            dwlCode.setVisibility(View.GONE);
        }
    }

    public void logoutCleanUp() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
        isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        if (isSync) {
            Toast.makeText(getApplicationContext(), getString(R.string.logout_msg), Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.SP_ID_KEY, 0);
            editor.putString(Constants.SP_AGENT_TOKEN_KEY, "");
            editor.putString(Constants.SP_AGENT_ID_KEY, "");
            editor.putString(Constants.SP_AGENT_NAME_KEY, "");
            editor.putString(Constants.SP_AGENT_PHONE_KEY, "");
            editor.putString(Constants.SP_AGENT_EMAIL_KEY, "");
            editor.putString(Constants.SP_AGENT_ADDRESS_KEY, "");
            editor.putString(Constants.SP_SYNC_TIME_KEY, "");
            editor.putBoolean(Constants.SP_IS_SYNC_KEY, false);
            editor.apply();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void autologout() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSION_CREATE_APP_FOLDER);
            }
        }
    }


    @Override
    protected void onTimeout() {
        autologout();
    }

    @Override
    protected long getTimeoutInSeconds() {
        return Constants.TIMER;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.SYNC_ICON_ON:
                updateSyncTime("", true);
                break;
            case MessageEvent.SYNC_ICON_OFF:
                updateSyncTime(DBHelper.timeStamp(), false);
                break;
            case MessageEvent.SYNC_SUCCESS:
                Util.dismissBarDialog();
                Toast.makeText(getApplicationContext(), "" + event.getMessage(), Toast.LENGTH_LONG).show();
                break;
            case MessageEvent.SYNC_SUCCESS_ENTRY:
                Util.dismissBarDialog();
                new DBHelper(MainActivity.this).setCheckEntry(false);
                Toast.makeText(getApplicationContext(), "" + event.getMessage(), Toast.LENGTH_LONG).show();
                break;
            case MessageEvent.SYNC_FAILURE:
                Util.dismissBarDialog();
                Toast.makeText(getApplicationContext(), "" + event.getMessage(), Toast.LENGTH_LONG).show();
                break;
            case MessageEvent.SERVER_ERROR_OCCURRED:
                Util.dismissBarDialog();
                Toast.makeText(getApplicationContext(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CalculatedSNF", "calculatedSNF>= ");
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
}
