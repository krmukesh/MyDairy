package com.mobilophilia.mydairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Util;

import io.fabric.sdk.android.Fabric;

/**
 * Created by yogen on 12-07-2017.
 */

public class SplashView extends Activity {

    //http://www.androidhive.info/2015/04/android-getting-started-with-material-design/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SP_MY_DAIRY_MK, MODE_PRIVATE);
                String token = sharedPreferences.getString(Constants.SP_AGENT_TOKEN_KEY, null);

                if (!Util.isEmpty(token)) {
                    Intent intent = new Intent(SplashView.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashView.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
}
