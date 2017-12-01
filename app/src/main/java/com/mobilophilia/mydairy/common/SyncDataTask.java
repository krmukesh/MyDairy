package com.mobilophilia.mydairy.common;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by mukesh on 31/08/17.
 */


public class SyncDataTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        // Here you can show progress bar or something on the similar lines.
        // Since you are in a UI thread here.
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        // You can track you progress update here
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Here you are in the worker thread and you are not allowed to access UI thread from here.
        // Here you can perform network operations or any heavy operations you want.
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // After completing execution of given task, control will return here.
        // Hence if you want to populate UI elements with fetched data, do it here.
    }
}


