package com.mobilophilia.mydairy.common;

/**
 * Created by prafull-singh on 30/5/15.
 */
public class Log {
    private static final boolean booleanShowLog = true;
    private static int value = 0;

    public static void e(String Tag, String Message) {
        value = value + 1;
        if (booleanShowLog)
            android.util.Log.e("" + Tag + " ," + value, "" + Message);
    }

    public static void d(String Tag, String Message) {
        value = value + 1;
        if (booleanShowLog)
            android.util.Log.d("" + Tag + " ," + value, "" + Message);
    }
}