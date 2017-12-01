package com.mobilophilia.mydairy.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.Log;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hanji on 7/20/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DBHelper";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "myDairyAppMk";
    // Table Enter Names
    private static final String TABLE_ENTER_NANE = "tbEnterName";
    private static final String TABLE_SET_PRICE = "tbSetPrice";
    private static final String TABLE_MY_ENTRY = "tbMyEntry";
    private static final String TABLE_EXPENSES = "tbMyExpense";
    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_AGENT_ID = "agentId";
    private static final String KEY_CREATED_AT = "created_at";
    // NOTES Table - column Names
    private static final String KEY_ENTER_CODE = "nameCode";
    private static final String KEY_ENTER_NAME = "enterName";
    private static final String KEY_ENTER_TYPE = "type";
    private static final String KEY_ENTER_PHONE = "phoneNo";
    // NOTES Table - column SetPrice
    private static final String KEY_LOW_FAT = "lowFat";
    private static final String KEY_HIGH_FAT = "highFat";
    private static final String KEY_LOW_SNF = "lowSnf";
    private static final String KEY_HIGH_SNF = "highSnf";
    private static final String KEY_START_PRICE = "startPrice";
    private static final String KEY_FAT_INTERVAL = "fatInterval";
    private static final String KEY_SNF_INTERVAL = "snfInterval";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TIME = "time";
    //Entry
    private static final String KEY_CLR = "entryClr";
    private static final String KEY_FAT = "entryFat";
    private static final String KEY_SNF = "entrySnf";
    private static final String KEY_LTR = "entryLtr";
    private static final String KEY_PRICE = "entryPrice";
    private static final String KEY_TOTAL = "entryTotal";

    private Context context;

    //ExpenseList
    private static final String KEY_EXPENSE = "expense";
    private static final String KEY_EXPENSE_TYPE = "expenseType";
    // Table Create Statements
    private static final String CREATE_TABLE_ENTER_NAME = "CREATE TABLE "
            + TABLE_ENTER_NANE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AGENT_ID + " TEXT,"
            + KEY_ENTER_CODE + " INTEGER,"
            + KEY_ENTER_NAME + " TEXT,"
            + KEY_ENTER_TYPE + " INTEGER,"
            + KEY_ENTER_PHONE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    // Table Create Statements setprice
    private static final String CREATE_TABLE_SET_PRICE = "CREATE TABLE "
            + TABLE_SET_PRICE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AGENT_ID + " TEXT,"
            + KEY_LOW_FAT + " REAL,"
            + KEY_HIGH_FAT + " REAL,"
            + KEY_LOW_SNF + " REAL,"
            + KEY_HIGH_SNF + " REAL,"
            + KEY_START_PRICE + " REAL,"
            + KEY_FAT_INTERVAL + " REAL,"
            + KEY_SNF_INTERVAL + " REAL,"
            + KEY_TYPE + " INTEGER,"
            + KEY_TIME + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    // Table Create Statements My entry
    private static final String CREATE_TABLE_MY_ENTRY = "CREATE TABLE "
            + TABLE_MY_ENTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AGENT_ID + " TEXT,"
            + KEY_ENTER_CODE + " INTEGER,"
            + KEY_CLR + " REAL,"
            + KEY_FAT + " REAL,"
            + KEY_SNF + " REAL,"
            + KEY_LTR + " REAL,"
            + KEY_PRICE + " REAL,"
            + KEY_TOTAL + " REAL,"
            + KEY_TIME + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    // Table Create Statements My entry
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE "
            + TABLE_EXPENSES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AGENT_ID + " TEXT,"
            + KEY_ENTER_CODE + " INTEGER,"
            + KEY_EXPENSE_TYPE + " INTEGER,"
            + KEY_EXPENSE + " REAL,"
            + KEY_CREATED_AT + " DATETIME" + ")";
    private String mAgentID = "";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        if (!Util.isEmpty(id)) {
            mAgentID = id;
        }
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_ENTER_NAME);
        db.execSQL(CREATE_TABLE_SET_PRICE);
        db.execSQL(CREATE_TABLE_MY_ENTRY);
        db.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ENTER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SET_PRICE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MY_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_EXPENSES);
        // create new tables
        onCreate(db);
    }

    public void deletedatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * Creating a EnterNameEntry
     */

    public long createEnterNameEntry(EnterNameEntry enterNameEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_ENTER_CODE, enterNameEntry.getNameCode());
        values.put(KEY_ENTER_NAME, enterNameEntry.getEnterName());
        values.put(KEY_ENTER_TYPE, enterNameEntry.getType());
        values.put(KEY_ENTER_PHONE, enterNameEntry.getPhoneNo());
        values.put(KEY_CREATED_AT, timeStamp());
        // insert row
        long name_id = db.insert(TABLE_ENTER_NANE, null, values);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return name_id;
    }


    /**
     * get single EnteredName
     */

    public EnterNameEntry getEnteredName(int enterCode) {
        boolean isENtry = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ENTER_NANE, null, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, "" + enterCode}, null, null, null);
        EnterNameEntry ene = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            ene = new EnterNameEntry();
            ene.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            ene.setNameCode((c.getInt(c.getColumnIndex(KEY_ENTER_CODE))));
            ene.setEnterName((c.getString(c.getColumnIndex(KEY_ENTER_NAME))));
            ene.setType((c.getInt(c.getColumnIndex(KEY_ENTER_TYPE))));
            ene.setPhoneNo((c.getString(c.getColumnIndex(KEY_ENTER_PHONE))));
            ene.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

            String code = "" + enterCode;
            String time = "" + Util.getTime();
            String startTime = "" + timeStampFromDateEntry(" 00:00:00");
            String endTime = timeStampFromDateEntry(" 99:99:99");

            Cursor cEntry = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ? AND " + KEY_TIME + " = ? AND " + KEY_CREATED_AT + " >=? AND " + KEY_CREATED_AT + " <=?", new String[]{mAgentID, code, time, startTime, endTime}, null, null, KEY_CREATED_AT + " DESC", null);
            if (cEntry != null && cEntry.getCount() > 0) {
                isENtry = true;
            }
            ene.setEntry(isENtry);
        }


        closeDB();
        return ene;
    }

    /**
     * getting all EnteredName
     */

    public List<EnterNameEntry> getEnteredNameList() {
        List<EnterNameEntry> arrayListName = new ArrayList<EnterNameEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ENTER_NANE, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, null);
        String dateHeader = "";
        String time = "";
        if (c.moveToFirst()) {
            do {
                String timeStamp = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                String dd = getDateTimeFromTimeStamp(timeStamp);
                String pp = getDate(dd);
                Date lastModDate = new Date(pp);
                try {
                    String[] str = Util.formatToYesterdayOrToday(lastModDate);
                    if (!dateHeader.equalsIgnoreCase(str[0])) {
                        dateHeader = str[0];
                        EnterNameEntry bean = new EnterNameEntry();
                        bean.setContentType(0);
                        bean.setHeaderText(dateHeader);
                        arrayListName.add(bean);
                    }
                    time = str[1];
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                EnterNameEntry ene = new EnterNameEntry();
                ene.setContentType(1);
                ene.setHeaderText(time);// In this Time
                ene.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ene.setNameCode((c.getInt(c.getColumnIndex(KEY_ENTER_CODE))));
                ene.setEnterName((c.getString(c.getColumnIndex(KEY_ENTER_NAME))));
                ene.setType((c.getInt(c.getColumnIndex(KEY_ENTER_TYPE))));
                ene.setPhoneNo((c.getString(c.getColumnIndex(KEY_ENTER_PHONE))));
                ene.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayListName.add(ene);

            } while (c.moveToNext());
        }
        return arrayListName;
    }

    public List<EnterNameEntry> getEnteredNameListForReport() {
        List<EnterNameEntry> arrayListName = new ArrayList<EnterNameEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ENTER_NANE, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, null);

        if (c.moveToFirst()) {
            do {
                EnterNameEntry ene = new EnterNameEntry();
                ene.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                ene.setNameCode((c.getInt(c.getColumnIndex(KEY_ENTER_CODE))));
                ene.setEnterName((c.getString(c.getColumnIndex(KEY_ENTER_NAME))));
                ene.setType((c.getInt(c.getColumnIndex(KEY_ENTER_TYPE))));
                ene.setPhoneNo((c.getString(c.getColumnIndex(KEY_ENTER_PHONE))));
                ene.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayListName.add(ene);

            } while (c.moveToNext());
        }
        return arrayListName;
    }


    public int updateEnteredName(EnterNameEntry ene) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTER_CODE, ene.getNameCode());
        values.put(KEY_ENTER_NAME, ene.getEnterName());
        values.put(KEY_ENTER_TYPE, ene.getType());
        values.put(KEY_ENTER_PHONE, ene.getPhoneNo());

        // updating row
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return db.update(TABLE_ENTER_NANE, values, KEY_ENTER_CODE + " = ?", new String[]{String.valueOf(ene.getNameCode())});

    }

    /**
     * getting Entered Name
     */

    public int getEnteredNameCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ENTER_NANE + " WHERE " + KEY_AGENT_ID + " = " + mAgentID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }


    /**
     * Deleting a EnteredName
     */

    public void deleteEnteredName(int enteredCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTER_NANE, KEY_ID + " = ?", new String[]{String.valueOf(enteredCode)});
    }

    public int getLastEnterCode() {
        int enterCode = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_CODE}, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_ENTER_CODE + " DESC", "1");
        if (cursor.moveToFirst()) {
            String code = cursor.getString(cursor.getColumnIndex(KEY_ENTER_CODE));
            enterCode = Integer.parseInt(code.trim());
        }
        cursor.close();
        return enterCode;
    }

    /**
     * Creating a setpriceEntry
     */

    public long createSetPriceEntry(SetPriceEntry setPriceEntry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_LOW_FAT, setPriceEntry.getLowFat());
        values.put(KEY_HIGH_FAT, setPriceEntry.getHighFat());
        values.put(KEY_LOW_SNF, setPriceEntry.getLowSnf());
        values.put(KEY_HIGH_SNF, setPriceEntry.getHighSnf());
        values.put(KEY_START_PRICE, setPriceEntry.getStartPrice());
        values.put(KEY_FAT_INTERVAL, setPriceEntry.getFatInterval());
        values.put(KEY_SNF_INTERVAL, setPriceEntry.getSnfInterval());
        values.put(KEY_TYPE, setPriceEntry.getType());
        values.put(KEY_TIME, setPriceEntry.getTime());
        values.put(KEY_CREATED_AT, timeStamp());
        // insert row
        long name_id = db.insert(TABLE_SET_PRICE, null, values);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return name_id;
    }

    public List<SetPriceEntry> getAllSetPrice() {
        List<SetPriceEntry> arrayListprice = new ArrayList<SetPriceEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SetPriceEntry sep = new SetPriceEntry();
                sep.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                sep.setLowFat((c.getDouble(c.getColumnIndex(KEY_LOW_FAT))));
                sep.setHighFat((c.getDouble(c.getColumnIndex(KEY_HIGH_FAT))));
                sep.setLowSnf((c.getDouble(c.getColumnIndex(KEY_LOW_SNF))));
                sep.setHighSnf((c.getDouble(c.getColumnIndex(KEY_HIGH_SNF))));
                sep.setStartPrice((c.getDouble(c.getColumnIndex(KEY_START_PRICE))));
                sep.setFatInterval((c.getDouble(c.getColumnIndex(KEY_FAT_INTERVAL))));
                sep.setSnfInterval((c.getDouble(c.getColumnIndex(KEY_SNF_INTERVAL))));
                sep.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                sep.setTime((c.getInt(c.getColumnIndex(KEY_TIME))));
                sep.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                arrayListprice.add(sep);
            } while (c.moveToNext());
        }
        return arrayListprice;
    }

    public SetPriceEntry getSetPriceFromId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_ID + " = ?", new String[]{mAgentID, "" + id}, null, null, null);
        SetPriceEntry sep = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            sep = new SetPriceEntry();
            sep.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            sep.setLowFat((c.getDouble(c.getColumnIndex(KEY_LOW_FAT))));
            sep.setHighFat((c.getDouble(c.getColumnIndex(KEY_HIGH_FAT))));
            sep.setLowSnf((c.getDouble(c.getColumnIndex(KEY_LOW_SNF))));
            sep.setHighSnf((c.getDouble(c.getColumnIndex(KEY_HIGH_SNF))));
            sep.setStartPrice((c.getDouble(c.getColumnIndex(KEY_START_PRICE))));
            sep.setFatInterval((c.getDouble(c.getColumnIndex(KEY_FAT_INTERVAL))));
            sep.setSnfInterval((c.getDouble(c.getColumnIndex(KEY_SNF_INTERVAL))));
            sep.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
            sep.setTime((c.getInt(c.getColumnIndex(KEY_TIME))));
            sep.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }
        closeDB();
        return sep;
    }

    public boolean isDublicateRecort(SetPriceEntry spe) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean rtn = false;

        String lFat = String.valueOf(spe.getLowFat());
        String hFat = String.valueOf(spe.getHighFat());
        String lSnf = String.valueOf(spe.getLowSnf());
        String hSnf = String.valueOf(spe.getHighSnf());
        String type = String.valueOf(spe.getType());
        String time = String.valueOf(spe.getTime());


        Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_LOW_FAT + " = ? AND " + KEY_HIGH_FAT + " = ? AND " + KEY_LOW_SNF + " = ? AND " + KEY_HIGH_SNF + " = ? AND " + KEY_TYPE + " = ? AND " + KEY_TIME + " = ?", new String[]{mAgentID, String.valueOf(spe.getLowFat()), String.valueOf(spe.getHighFat()), String.valueOf(spe.getLowSnf()), String.valueOf(spe.getHighSnf()), String.valueOf(spe.getType()), String.valueOf(spe.getTime())}, null, null, null);

        // Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_LOW_FAT + " <= ? AND " +KEY_HIGH_FAT + " >= ? AND "+KEY_HIGH_FAT + " >= ? AND "+ KEY_TYPE + " = ? AND " + KEY_TIME + " = ?", new String[]{mAgentID, lFat,lFat,hFat,type,time}, null, null, null);

        if (c != null && c.getCount() > 0) {
            rtn = true;
        }
        closeDB();
        return rtn;
    }


    public int updatePriceWithId(SetPriceEntry spe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOW_FAT, spe.getLowFat());
        values.put(KEY_HIGH_FAT, spe.getHighFat());
        values.put(KEY_LOW_SNF, spe.getLowSnf());
        values.put(KEY_HIGH_SNF, spe.getHighSnf());
        values.put(KEY_START_PRICE, spe.getStartPrice());
        values.put(KEY_FAT_INTERVAL, spe.getFatInterval());
        values.put(KEY_SNF_INTERVAL, spe.getSnfInterval());
        values.put(KEY_TYPE, spe.getType());
        values.put(KEY_TIME, spe.getTime());

        // updating row
        int up = db.update(TABLE_SET_PRICE, values, KEY_ID + " = ?", new String[]{String.valueOf(spe.getId())});
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return up;
    }


    public void deletePriceWithId(SetPriceEntry spe) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SET_PRICE, KEY_ID + " = ?", new String[]{String.valueOf(spe.getId())});

    }


    public List<SetPriceEntry> getSetPriceTypeAndTime(int type, int time, String fat, String snf) {
        List<SetPriceEntry> arrayListprice = new ArrayList<SetPriceEntry>();
        String milkType = "" + type;
        String milkyTime = "" + time;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_TYPE + " = ? AND " + KEY_TIME + " = ? AND " + KEY_LOW_FAT + " <= ? AND " + KEY_HIGH_FAT + " >= ? AND " + KEY_LOW_SNF + " <= ? AND " + KEY_HIGH_SNF + " >= ?", new String[]{mAgentID, milkType, milkyTime, fat, fat, snf, snf}, null, null, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SetPriceEntry sep = new SetPriceEntry();
                sep.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                sep.setLowFat((c.getDouble(c.getColumnIndex(KEY_LOW_FAT))));
                sep.setHighFat((c.getDouble(c.getColumnIndex(KEY_HIGH_FAT))));
                sep.setLowSnf((c.getDouble(c.getColumnIndex(KEY_LOW_SNF))));
                sep.setHighSnf((c.getDouble(c.getColumnIndex(KEY_HIGH_SNF))));
                sep.setStartPrice((c.getDouble(c.getColumnIndex(KEY_START_PRICE))));
                sep.setFatInterval((c.getDouble(c.getColumnIndex(KEY_FAT_INTERVAL))));
                sep.setSnfInterval((c.getDouble(c.getColumnIndex(KEY_SNF_INTERVAL))));
                sep.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                sep.setTime((c.getInt(c.getColumnIndex(KEY_TIME))));
                sep.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                arrayListprice.add(sep);
            } while (c.moveToNext());
        }
        return arrayListprice;
    }


    public List<SetPriceEntry> getPriceSetForGivenSNF(int type, int time, String snf) {
        List<SetPriceEntry> arrayListprice = new ArrayList<SetPriceEntry>();
        String milkType = "" + type;
        String milkyTime = "" + time;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_TYPE + " = ? AND " + KEY_TIME + " = ? AND " + KEY_LOW_SNF + " <= ? AND " + KEY_HIGH_SNF + " >= ?", new String[]{mAgentID, milkType, milkyTime, snf, snf}, null, null, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SetPriceEntry sep = new SetPriceEntry();
                sep.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                sep.setLowFat((c.getDouble(c.getColumnIndex(KEY_LOW_FAT))));
                sep.setHighFat((c.getDouble(c.getColumnIndex(KEY_HIGH_FAT))));
                sep.setLowSnf((c.getDouble(c.getColumnIndex(KEY_LOW_SNF))));
                sep.setHighSnf((c.getDouble(c.getColumnIndex(KEY_HIGH_SNF))));
                sep.setStartPrice((c.getDouble(c.getColumnIndex(KEY_START_PRICE))));
                sep.setFatInterval((c.getDouble(c.getColumnIndex(KEY_FAT_INTERVAL))));
                sep.setSnfInterval((c.getDouble(c.getColumnIndex(KEY_SNF_INTERVAL))));
                sep.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                sep.setTime((c.getInt(c.getColumnIndex(KEY_TIME))));
                sep.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                // adding to todo list
                arrayListprice.add(sep);
            } while (c.moveToNext());
        }
        return arrayListprice;
    }

    public List<MyEntries> getAllEntries() {
        List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        boolean isAdded = false;

        if (c.moveToFirst()) {
            do {
                Double grandTotla = 0d;
                MyEntries myEBean = new MyEntries();

                int code = c.getInt(c.getColumnIndex(KEY_ENTER_CODE));
                int id = c.getInt(c.getColumnIndex(KEY_ID));
                String codeStr = "" + code;
                isAdded = true;

                for (int i = 0; i < arrayMyEntries.size(); i++) {

                    if (arrayMyEntries.get(i).getCode() == code) {
                        isAdded = false;
                        break;
                    }
                }

                if (isAdded) {

                    String name = "";
                    Cursor nCursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_NAME}, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, codeStr}, null, null, null);
                    if (nCursor.moveToFirst()) {
                        do {
                            name = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_NAME));
                        } while (nCursor.moveToNext());
                    }
                    Cursor pCursor = db.query(TABLE_MY_ENTRY, new String[]{KEY_TOTAL}, KEY_ENTER_CODE + " = ?", new String[]{codeStr}, null, null, null);
                    if (pCursor.moveToFirst()) {
                        do {
                            grandTotla = grandTotla + pCursor.getDouble(pCursor.getColumnIndex(KEY_TOTAL));
                        } while (pCursor.moveToNext());
                    }
                    myEBean.setId(id);
                    myEBean.setCode(code);
                    myEBean.setName(name);
                    myEBean.setTotal(grandTotla);
                    // adding to todo list
                    arrayMyEntries.add(myEBean);

                }


            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }


    public List<MyEntries> getAllEntriesForReportByDate(String startCode, String endCode, String startDate, String endDate, boolean isSingleDate) {
        List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (isSingleDate) {
            //c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " LIKE ?", new String[]{mAgentID, "%" + startDate + "%"}, null, null, KEY_CREATED_AT + " DESC", null);
            c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " >=?", new String[]{mAgentID, startDate}, null, null, KEY_CREATED_AT + " DESC", null);
        } else {
            c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " >=? AND " + KEY_ENTER_CODE + " <=? AND " + KEY_CREATED_AT + " >=? AND " + KEY_CREATED_AT + " <=?", new String[]{mAgentID, startCode, endCode, startDate, endDate}, null, null, KEY_CREATED_AT + " DESC", null);
            //String qury = "select * from " + TABLE_MY_ENTRY + " where agentId = '" + mAgentID + "' AND " + "nameCode BETWEEN '" + startCode + "' AND '" + endCode + "' AND " + "created_at BETWEEN '" + startDate + " 00:00:00" + "' AND '" + endDate + " 99:99:99" + "' ORDER BY created_at DESC";
            //c = db.rawQuery(qury, null);
        }

        Double avgFat = 0d;
        Double avgSnf = 0d;
        Double avgPrice = 0d;
        Double totalLtr = 0d;
        Double grandTotla = 0d;

        if (c.moveToFirst()) {
            do {
                MyEntries myEBean = new MyEntries();

                int code = c.getInt(c.getColumnIndex(KEY_ENTER_CODE));
                int id = c.getInt(c.getColumnIndex(KEY_ID));
                String codeStr = "" + code;

                String name = "";
                int milkType = 0;
                Cursor nCursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_NAME, KEY_ENTER_TYPE}, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, codeStr}, null, null, null);
                if (nCursor.moveToFirst()) {
                    do {
                        name = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_NAME));
                        milkType = nCursor.getInt(nCursor.getColumnIndex(KEY_ENTER_TYPE));
                    } while (nCursor.moveToNext());
                }

                avgFat = avgFat + c.getDouble(c.getColumnIndex(KEY_FAT));
                avgSnf = avgSnf + c.getDouble(c.getColumnIndex(KEY_SNF));
                avgPrice = avgPrice + c.getDouble(c.getColumnIndex(KEY_PRICE));
                totalLtr = totalLtr + c.getDouble(c.getColumnIndex(KEY_LTR));
                grandTotla = grandTotla + c.getDouble(c.getColumnIndex(KEY_TOTAL));

                myEBean.avgFat = avgFat;
                myEBean.avgSnf = avgSnf;
                myEBean.avgPrice = avgPrice;
                myEBean.totalLtr = totalLtr;
                myEBean.grandPrice = grandTotla;

                myEBean.setId(id);
                myEBean.setCode(code);
                myEBean.setName(name);
                myEBean.setMilkType(milkType);
                myEBean.setFat(c.getDouble(c.getColumnIndex(KEY_FAT)));
                myEBean.setSnf(c.getDouble(c.getColumnIndex(KEY_SNF)));
                myEBean.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                myEBean.setLtr(c.getDouble(c.getColumnIndex(KEY_LTR)));
                myEBean.setTotal(c.getDouble(c.getColumnIndex(KEY_TOTAL)));
                myEBean.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayMyEntries.add(myEBean);
            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }


    public MyEntries getEntryById(int id) {
        String ids = "" + id;
        MyEntries myEBean = null;
        String name = "";
        String phone = "";
        int milkType = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MY_ENTRY, null, KEY_ID + " = ?", new String[]{ids}, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                myEBean = new MyEntries();

                int code = c.getInt(c.getColumnIndex(KEY_ENTER_CODE));
                String codeStr = "" + code;


                Cursor nCursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_NAME, KEY_ENTER_PHONE, KEY_ENTER_TYPE}, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, codeStr}, null, null, null);
                if (nCursor.moveToFirst()) {
                    do {
                        name = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_NAME));
                        phone = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_PHONE));
                        milkType = nCursor.getInt(nCursor.getColumnIndex(KEY_ENTER_TYPE));
                    } while (nCursor.moveToNext());
                }

                myEBean.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                myEBean.setCode(code);
                myEBean.setName(name);
                myEBean.setPhone(phone);
                myEBean.setMilkType(milkType);
                myEBean.setFat(c.getDouble(c.getColumnIndex(KEY_FAT)));
                myEBean.setClr(c.getDouble(c.getColumnIndex(KEY_CLR)));
                myEBean.setSnf(c.getDouble(c.getColumnIndex(KEY_SNF)));
                myEBean.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                myEBean.setLtr(c.getDouble(c.getColumnIndex(KEY_LTR)));
                myEBean.setMorOrEveTime(c.getInt(c.getColumnIndex(KEY_TIME)));
                myEBean.setTotal(c.getDouble(c.getColumnIndex(KEY_TOTAL)));

            } while (c.moveToNext());
        }
        return myEBean;
    }


    public List<MyEntries> getListOfEntriesByCode(int code) {
        List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, "" + code}, null, null, KEY_CREATED_AT + " DESC", null);
        String dateHeader = "";
        String time = "";
        if (c.moveToFirst()) {
            do {
                String timeStamp = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                String dd = getDateTimeFromTimeStamp(timeStamp);
                String pp = getDate(dd);
                Date lastModDate = new Date(pp);
                try {
                    String[] str = Util.formatToYesterdayOrToday(lastModDate);

                    if (!dateHeader.equalsIgnoreCase(str[0])) {
                        dateHeader = str[0];
                        MyEntries bean = new MyEntries();
                        bean.setContentType(0);
                        bean.setHeaderText(dateHeader);
                        arrayMyEntries.add(bean);
                    }
                    time = str[1];
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                MyEntries myEBean = new MyEntries();
                myEBean.setContentType(1);
                myEBean.setHeaderText(time);
                myEBean.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                myEBean.setCode(c.getInt(c.getColumnIndex(KEY_ENTER_CODE)));
                myEBean.setClr(c.getDouble(c.getColumnIndex(KEY_CLR)));
                myEBean.setFat(c.getDouble(c.getColumnIndex(KEY_FAT)));
                myEBean.setSnf(c.getDouble(c.getColumnIndex(KEY_SNF)));
                myEBean.setLtr(c.getDouble(c.getColumnIndex(KEY_LTR)));
                myEBean.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                myEBean.setTotal(c.getDouble(c.getColumnIndex(KEY_TOTAL)));

                arrayMyEntries.add(myEBean);

            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }


    public long createEntry(MyEntries myEntries) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_ENTER_CODE, myEntries.getCode());
        values.put(KEY_CLR, myEntries.getClr());
        values.put(KEY_FAT, myEntries.getFat());
        values.put(KEY_SNF, myEntries.getSnf());
        values.put(KEY_LTR, myEntries.getLtr());
        values.put(KEY_PRICE, myEntries.getPrice());
        values.put(KEY_TOTAL, myEntries.getTotal());
        values.put(KEY_TIME, myEntries.getMorOrEveTime());
        values.put(KEY_CREATED_AT, timeStamp());
        long name_id = db.insert(TABLE_MY_ENTRY, null, values);
        setCheckEntry(true);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return name_id;
    }

    public int updateEntryWithId(MyEntries myEntries) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_ENTER_CODE, myEntries.getCode());
        values.put(KEY_CLR, myEntries.getClr());
        values.put(KEY_FAT, myEntries.getFat());
        values.put(KEY_SNF, myEntries.getSnf());
        values.put(KEY_LTR, myEntries.getLtr());
        values.put(KEY_PRICE, myEntries.getPrice());
        values.put(KEY_TOTAL, myEntries.getTotal());
        values.put(KEY_TIME, myEntries.getMorOrEveTime());
        values.put(KEY_CREATED_AT, timeStamp());

        // updating row
        int up = db.update(TABLE_MY_ENTRY, values, KEY_ID + " = ?", new String[]{String.valueOf(myEntries.getId())});
        setCheckEntry(true);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return up;
    }

    public void deleteEntry(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MY_ENTRY, KEY_ID + " = ?", new String[]{String.valueOf(id)});

    }


    public long createExpense(ExpenseBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_ENTER_CODE, bean.getCode());
        values.put(KEY_EXPENSE_TYPE, bean.getExpenseType());
        values.put(KEY_EXPENSE, bean.getExpense());
        values.put(KEY_CREATED_AT, timeStamp());
        // insert row
        long name_id = db.insert(TABLE_EXPENSES, null, values);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return name_id;
    }


    public int updateExpense(ExpenseBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AGENT_ID, mAgentID);
        values.put(KEY_ENTER_CODE, bean.getCode());
        values.put(KEY_EXPENSE_TYPE, bean.getExpenseType());
        values.put(KEY_EXPENSE, bean.getExpense());
        values.put(KEY_CREATED_AT, timeStamp());
        // updating row
        EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_ON));
        return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?", new String[]{String.valueOf(bean.getId())});

    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }


    public List<ExpenseBean> getAllExpenses() {
        List<ExpenseBean> arrayMyEntries = new ArrayList<ExpenseBean>();
        SQLiteDatabase db = this.getReadableDatabase();
        String dateHeader = "";
        Cursor c = db.query(TABLE_EXPENSES, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        if (c.moveToFirst()) {
            do {
                ExpenseBean myEBean = new ExpenseBean();

                int code = c.getInt(c.getColumnIndex(KEY_ENTER_CODE));
                int id = c.getInt(c.getColumnIndex(KEY_ID));
                String codeStr = "" + code;


                String name = "";
                Cursor nCursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_NAME}, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, codeStr}, null, null, null);
                if (nCursor.moveToFirst()) {
                    do {
                        name = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_NAME));
                    } while (nCursor.moveToNext());
                }

                Double expense = c.getDouble(c.getColumnIndex(KEY_EXPENSE));
                String timeStamp = c.getString(c.getColumnIndex(KEY_CREATED_AT));
                // String dd = getDate(timeStamp);
                String dd = getDateTimeFromTimeStamp(timeStamp);
                String pp = getDate(dd);
                Date lastModDate = new Date(pp);

                String time = "";
                try {
                    String[] str = Util.formatToYesterdayOrToday(lastModDate);
                    if (!dateHeader.equalsIgnoreCase(str[0])) {
                        dateHeader = str[0];
                        ExpenseBean myEBeanDate = new ExpenseBean();
                        myEBeanDate.setType(0);
                        myEBeanDate.setHeaderText(dateHeader);
                        arrayMyEntries.add(myEBeanDate);
                    }
                    time = str[1];
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myEBean.setType(1);
                myEBean.setId(id);
                myEBean.setCode(code);
                myEBean.setName(name);
                myEBean.setExpense(expense);
                myEBean.setHeaderText("" + time);
                arrayMyEntries.add(myEBean);

            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }


    public ExpenseBean getExpensesById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ExpenseBean myEBean = null;
        String name = "";
        String phone = "";
        Cursor c = db.query(TABLE_EXPENSES, null, KEY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (c.moveToFirst()) {
            do {

                int code = c.getInt(c.getColumnIndex(KEY_ENTER_CODE));

                Cursor nCursor = db.query(TABLE_ENTER_NANE, new String[]{KEY_ENTER_NAME, KEY_ENTER_PHONE}, KEY_AGENT_ID + " = ? AND " + KEY_ENTER_CODE + " = ?", new String[]{mAgentID, String.valueOf(code)}, null, null, null);

                if (nCursor.moveToFirst()) {
                    do {
                        name = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_NAME));
                        phone = nCursor.getString(nCursor.getColumnIndex(KEY_ENTER_PHONE));
                    } while (nCursor.moveToNext());
                }

                myEBean = new ExpenseBean();
                myEBean.setName(name);
                myEBean.setPhone(phone);
                myEBean.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                myEBean.setCode(c.getInt(c.getColumnIndex(KEY_ENTER_CODE)));
                myEBean.setExpense(c.getDouble(c.getColumnIndex(KEY_EXPENSE)));
                myEBean.setExpenseType(c.getInt(c.getColumnIndex(KEY_EXPENSE_TYPE)));
                myEBean.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            } while (c.moveToNext());
        }
        return myEBean;
    }


    ///   *******************************


    public List<EnterNameEntry> addedNameList(String timeStamp) {
        List<EnterNameEntry> arrayListName = new ArrayList<EnterNameEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (timeStamp.equalsIgnoreCase("0")) {
            c = db.query(TABLE_ENTER_NANE, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        } else {
            c = db.query(TABLE_ENTER_NANE, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " > ?", new String[]{mAgentID, timeStamp}, null, null, KEY_CREATED_AT + " DESC", null);
        }
        if (c.moveToFirst()) {
            do {
                EnterNameEntry ene = new EnterNameEntry();
                ene.setNameCode((c.getInt(c.getColumnIndex(KEY_ENTER_CODE))));
                ene.setEnterName((c.getString(c.getColumnIndex(KEY_ENTER_NAME))));
                ene.setType((c.getInt(c.getColumnIndex(KEY_ENTER_TYPE))));
                ene.setPhoneNo((c.getString(c.getColumnIndex(KEY_ENTER_PHONE))));
                ene.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayListName.add(ene);
            } while (c.moveToNext());
        }
        return arrayListName;
    }


    public List<SetPriceEntry> addedSetPrice(String timeStamp) {
        List<SetPriceEntry> arrayListprice = new ArrayList<SetPriceEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (timeStamp.equalsIgnoreCase("0")) {
            c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        } else {
            c = db.query(TABLE_SET_PRICE, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " > ?", new String[]{mAgentID, timeStamp}, null, null, KEY_CREATED_AT + " DESC", null);
        }
        if (c.moveToFirst()) {
            do {
                SetPriceEntry sep = new SetPriceEntry();
                sep.setLowFat((c.getDouble(c.getColumnIndex(KEY_LOW_FAT))));
                sep.setHighFat((c.getDouble(c.getColumnIndex(KEY_HIGH_FAT))));
                sep.setLowSnf((c.getDouble(c.getColumnIndex(KEY_LOW_SNF))));
                sep.setHighSnf((c.getDouble(c.getColumnIndex(KEY_HIGH_SNF))));
                sep.setStartPrice((c.getDouble(c.getColumnIndex(KEY_START_PRICE))));
                sep.setFatInterval((c.getDouble(c.getColumnIndex(KEY_FAT_INTERVAL))));
                sep.setSnfInterval((c.getDouble(c.getColumnIndex(KEY_SNF_INTERVAL))));
                sep.setType((c.getInt(c.getColumnIndex(KEY_TYPE))));
                sep.setTime((c.getInt(c.getColumnIndex(KEY_TIME))));
                sep.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayListprice.add(sep);
            } while (c.moveToNext());
        }
        return arrayListprice;
    }


    public List<MyEntries> addedEntries(String timeStamp) {
        List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (timeStamp.equalsIgnoreCase("0")) {
            c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        } else {
            c = db.query(TABLE_MY_ENTRY, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " > ?", new String[]{mAgentID, timeStamp}, null, null, KEY_CREATED_AT + " DESC", null);
        }
        if (c.moveToFirst()) {
            do {
                MyEntries myEBean = new MyEntries();
                myEBean.setCode(c.getInt(c.getColumnIndex(KEY_ENTER_CODE)));
                myEBean.setClr(c.getDouble(c.getColumnIndex(KEY_CLR)));
                myEBean.setFat(c.getDouble(c.getColumnIndex(KEY_FAT)));
                myEBean.setSnf(c.getDouble(c.getColumnIndex(KEY_SNF)));
                myEBean.setLtr(c.getDouble(c.getColumnIndex(KEY_LTR)));
                myEBean.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                myEBean.setTotal(c.getDouble(c.getColumnIndex(KEY_TOTAL)));
                myEBean.setMorOrEveTime(c.getInt(c.getColumnIndex(KEY_TIME)));
                myEBean.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayMyEntries.add(myEBean);
            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }

    public List<ExpenseBean> addedExpenses(String timeStamp) {
        List<ExpenseBean> arrayMyEntries = new ArrayList<ExpenseBean>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = null;
        if (timeStamp.equalsIgnoreCase("0")) {
            c = db.query(TABLE_EXPENSES, null, KEY_AGENT_ID + " = ?", new String[]{mAgentID}, null, null, KEY_CREATED_AT + " DESC", null);
        } else {
            c = db.query(TABLE_EXPENSES, null, KEY_AGENT_ID + " = ? AND " + KEY_CREATED_AT + " > ?", new String[]{mAgentID, timeStamp}, null, null, KEY_CREATED_AT + " DESC", null);
        }
        if (c.moveToFirst()) {
            do {
                ExpenseBean myEBean = new ExpenseBean();
                myEBean.setCode(c.getInt(c.getColumnIndex(KEY_ENTER_CODE)));
                myEBean.setExpense(c.getDouble(c.getColumnIndex(KEY_EXPENSE)));
                myEBean.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                arrayMyEntries.add(myEBean);
            } while (c.moveToNext());
        }
        return arrayMyEntries;
    }

    ///   *******************************


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String getDateOnly() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate(String timeStamp) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(timeStamp);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "" + date;
    }


    public JSONObject synchDataServer(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String timeStamp = sharedPreferences.getString(Constants.SP_SYNC_TIME_KEY, "0");

        JSONObject dataObj = new JSONObject();
        JSONArray nameArray = new JSONArray();
        JSONArray priceArray = new JSONArray();
        JSONArray entryArray = new JSONArray();
        JSONArray expArray = new JSONArray();

        try {
            for (int i = 0; i < addedNameList(timeStamp).size(); i++) {
                JSONObject nameObj = new JSONObject();
                EnterNameEntry name = addedNameList(timeStamp).get(i);
                nameObj.put("code", String.valueOf(name.getNameCode()));
                nameObj.put("name", name.getEnterName());
                nameObj.put("type", String.valueOf(name.getType()));
                nameObj.put("phone", name.getPhoneNo());
                nameObj.put("created_at", name.getCreatedAt());
                nameArray.put(i, nameObj);
            }

            for (int i = 0; i < addedSetPrice(timeStamp).size(); i++) {
                JSONObject priceObj = new JSONObject();
                SetPriceEntry price = addedSetPrice(timeStamp).get(i);
                priceObj.put("lfat", String.valueOf(price.getLowFat()));
                priceObj.put("hfat", String.valueOf(price.getHighFat()));
                priceObj.put("lsnf", String.valueOf(price.getLowSnf()));
                priceObj.put("hsnf", String.valueOf(price.getHighSnf()));
                priceObj.put("start_price", String.valueOf(price.getStartPrice()));

               /* priceObj.put("fat_intetval", price.getFatInterval());
                priceObj.put("snf_intetval", price.getSnfInterval());*/

                priceObj.put("intetval", String.valueOf(price.getFatInterval()));
                priceObj.put("type", String.valueOf(price.getType()));
                priceObj.put("time", String.valueOf(price.getTime()));
                priceObj.put("created_at", price.getCreatedAt());
                priceArray.put(i, priceObj);
            }


            for (int i = 0; i < addedEntries(timeStamp).size(); i++) {
                JSONObject entryObj = new JSONObject();
                MyEntries entry = addedEntries(timeStamp).get(i);
                entryObj.put("code", String.valueOf(entry.getCode()));
                entryObj.put("crl", String.valueOf(entry.getClr()));
                entryObj.put("fat", String.valueOf(entry.getFat()));
                entryObj.put("snf", String.valueOf(entry.getSnf()));
                entryObj.put("ltr", String.valueOf(entry.getLtr()));
                entryObj.put("price", String.valueOf(entry.getPrice()));
                entryObj.put("total", String.valueOf(entry.getTotal()));
                entryObj.put("more", String.valueOf(entry.getMorOrEveTime()));
                entryObj.put("created_at", entry.getCreatedAt());
                entryArray.put(i, entryObj);
            }


            for (int i = 0; i < addedExpenses(timeStamp).size(); i++) {
                JSONObject expObj = new JSONObject();
                ExpenseBean entry = addedExpenses(timeStamp).get(i);
                expObj.put("code", String.valueOf(entry.getCode()));
                expObj.put("expense", String.valueOf(entry.getExpense()));
                expObj.put("created_at", entry.getCreatedAt());
                expArray.put(i, expObj);
            }

            dataObj.put("customer", nameArray);
            dataObj.put("price", priceArray);
            dataObj.put("entry", entryArray);
            dataObj.put("expense", expArray);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         EventBus.getDefault().post(new MessageEvent(MessageEvent.SYNC_ICON_OFF));
        return dataObj;
    }


    public static String getDateTimeFromTimeStamp(String tstamp) {
        String dateTime = "";
        try {
            long timestamp = Long.parseLong(tstamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date currentTimeZone = (Date) calendar.getTime();
            dateTime = sdf.format(currentTimeZone);
        } catch (Exception e) {

        }
        return dateTime;
    }

    public static String timeStamp() {
        String timeStamp = getDateTime();
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            date = (Date) formatter.parse(timeStamp);
            timeStamp = "" + date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    public static String timeStampFromDateEntry(String time) {
        String timeStamp = getDateOnly() + time;
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            date = (Date) formatter.parse(timeStamp);
            timeStamp = "" + date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    public static String timeStampFromDate(String timeStamp) {
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            date = (Date) formatter.parse(timeStamp);
            timeStamp = "" + date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }


    public void setCheckEntry(boolean isSync) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.SP_BEFORE_SYNC_ENTRY, isSync);
        editor.apply();
    }

    public boolean getCheckEntry() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        boolean isSync = sharedPreferences.getBoolean(Constants.SP_BEFORE_SYNC_ENTRY, false);
        return isSync;
    }


    public boolean getSyncFlag() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        boolean isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        return isSync;
    }

    private String getAgentId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        return id;
    }

    private boolean isSync() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_MY_DAIRY_MK, context.MODE_PRIVATE);
        boolean isSync = sharedPreferences.getBoolean(Constants.SP_IS_SYNC_KEY, false);
        return isSync;
    }

}