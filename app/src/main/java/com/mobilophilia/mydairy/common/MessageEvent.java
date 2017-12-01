package com.mobilophilia.mydairy.common;

/**
 * Created by Hanji on 7/21/2017.
 */

public class MessageEvent {


    public static final int NETWORK_TIME_OUT = 700;
    public static final int SERVER_ERROR_OCCURRED = 701;

    public static final int LOG_IN_SUCCESS = 1000;
    public static final int LOG_IN_FAILURE = 1001;

    public static final int ENTRY_SUCCESS = 2000;
    public static final int ENTRY_FAILURE = 2001;

    public static final int EXPENSES_SUCCESS = 3000;
    public static final int EXPENSES_FAILURE = 3001;

    public static final int DOWNLOAD_REPORT_SUCCESS = 4000;
    public static final int DOWNLOAD_REPORT_FAILURE = 4001;
    public static final int DOWNLOAD_REPORT_PROGRESS = 4002;
    public static final int DOWNLOAD_REPORT_PROGRESS_VALUE = 4003;

    public static final int SYNC_ICON_ON = 5000;
    public static final int SYNC_ICON_OFF = 5001;

    public static final int SYNC_SUCCESS = 6000;
    public static final int SYNC_FAILURE = 6001;
    public static final int SYNC_SUCCESS_ENTRY = 6002;


    private boolean STATUS;
    private int TYPE;
    private Object VALUE;
    private String MESSAGE;
    private int INTVALUE;


    public MessageEvent(int type) {
        this(type, true, null, null,0);
    }


    public MessageEvent(int type,int value) {
        this(type, true, null, null,value);
    }

    public MessageEvent(int type, boolean status) {
        this(type, status, null, null,0);
    }

    public MessageEvent(int type, String MESSAGE) {
        this(type, true, null, MESSAGE,0);
    }


    public MessageEvent(int type, boolean status, Object value, String message,int intValue) {
        TYPE = type;
        STATUS = status;
        VALUE = value;
        MESSAGE = message;
        INTVALUE = intValue;
    }


    public boolean getStatus() {
        return STATUS;
    }


    public int getType() {
        return TYPE;
    }

    public int getIntValue() {
        return INTVALUE;
    }

    public String getMessage() {
        return MESSAGE;
    }

    public Object getValue() {
        return VALUE;
    }


}