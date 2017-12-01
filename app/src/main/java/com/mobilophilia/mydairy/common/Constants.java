package com.mobilophilia.mydairy.common;

/**
 * Created by Hanji on 7/21/2017.
 */

public class Constants {

    public static final String BASE_URL = "http://dairy.cosmeatiles.com/";
    public static final String LOGIN = BASE_URL + "Api/login";
    public static final String ENTRY = BASE_URL + "Api/entry";
    public static final String EXPENSES = BASE_URL + "Api/addexpenses";
    public static final String REPORT_FILE = BASE_URL + "Api/expenses";
    public static final String SYNC = BASE_URL + "Api/syncData";



    public static final int TIMER = 15 * 60;


    // Save data in SharedPreferences Keys
    public static final String SP_MY_DAIRY_MK = "myDairyMk";
    public static final String SP_ID_KEY = "id";
    public static final String SP_AGENT_TOKEN_KEY = "agentToken";
    public static final String SP_AGENT_ID_KEY = "agentId";
    public static final String SP_AGENT_NAME_KEY = "agentName";
    public static final String SP_AGENT_PHONE_KEY = "agentPhone";
    public static final String SP_AGENT_EMAIL_KEY = "agentEmail";
    public static final String SP_AGENT_ADDRESS_KEY = "agentAddress";
    public static final String SP_SYNC_TIME_KEY = "syncTime";
    public static final String SP_IS_SYNC_KEY = "isSyncAble";

    public static final String SP_BEFORE_SYNC_ENTRY = "beboreSyncEntry";


    public static final String PROGRESS_DIALOG_MSG = "Validating agent.....";
    public static final String PROGRESS_DIALOG_ENTRY = "Adding your entry.....";
    public static final String PROGRESS_DIALOG_EXPENSES = "Submitting your expense.....";

    public static final int SERVICE_STATUS = 1;
    public static final int PERMISSION_CREATE_APP_FOLDER = 88;

    public static final String[] type = new String[]{"Cow", "Buffalo"};
    public static final String[] expense_type = new String[]{"Medical", "Pashu Khadya", "Advance Payment"};
    public static final String[] timing = new String[]{"Morning", "Evening"};
    public static final String RECORD_ID_TABLE = "recordId";
    public static final String RECORD_ID = "ID";


    public static final String ERROR_VALIDATION_PHONE = "Phone number can not valid.";
    public static final String ERROR_VALIDATION_CODE = "Code must be greater than '0'";
    public static final String ERROR_VALIDATION_END = "End code must be greater than start code";
    public static final String ERROR_VALIDATION_NAME = "Name must be 8 characters";
    public static final String ERROR_VALIDATION_NAME_ii = "Name can not be blank";

    public static final String ERROR_VALIDATION_AGENT_ID = "Agent Id must be 8 characters";
    public static final String ERROR_VALIDATION_PASSWORD = "Password must be 8 characters";

    public static final String ERROR_VALIDATION_CLR_BLANK = "CLR can not be blank";
    public static final String ERROR_VALIDATION_FAT_Z = "FAT must be greater than '0'";
    public static final String ERROR_VALIDATION_CLR = "CLR must be greater than '0'";
    public static final String ERROR_VALIDATION_LTR_Z = "LTR must be greater than '0'";
    public static final String ERROR_VALIDATION_FAT = "FAT can not be blank";
    public static final String ERROR_VALIDATION_LTR = "LTR can not be blank";
    public static final String ERROR_CODE_INVALID = "Please enter valid code";
    public static final String ERROR_ENTRY_ADDED = "Already added entry for this code";
    public static final String ERROR_CODE_INVALID_BLANK = "Code can not be blank";
    public static final String ERROR_EXPENSES = "ExpenseList must be greater than '0'";
    public static final String ERROR_FAT = "Fat must be greater than '0' And it's Maximum limit is '11'";
    public static final String ERROR_FAT_BLANK = "FAT can not be blank";
    public static final String ERROR_INTERVAL_BLANK = "Interval must be greater than '0'";
    public static final String ERROR_INTERVAL_INVALID = "Interval is invalid";
    public static final String ERROR_FAT_LOW_BLANK = "Low Fat can not be blank";

    public static final String ERROR_EXPENSE_BLANK = "Expense can not be blank";
    public static final String ERROR_EXPENSE_BLANK_ZERO = "Expense can not be '0'";

    public static final String ERROR_FAT_HIGH_BLANK = "High Fat can not be blank";
    public static final String ERROR_SNF = "Maximum limit of SNF is '13'.";
    public static final String ERROR_SET_PRICE_DUBLICATION = "This price set is already exists";
    public static final String ERROR_SNF_LOW_BLANK = "Low SNF can not be blank";
    public static final String ERROR_SNF_HIGH_BLANK = "High SNF can not be blank";
    public static final String ERROR_PRICE = "Price should be greater than '0'";
    public static final String ERROR_PRICE_BLANK = "Price can not be blank";
    public static final String FIELD_MAINDATORY = "All field are *maindatory";

    public static final String NO_SET = "There is no price set for enter value";
    public static final String SELECT_DATE = "Please select date.";

    public static final String NO_RECORD = "Did not found any record for selected 'DATE'";

    public static final String DIALOG_MSG_DELETE_NAME = "Do you want to delete this name";
    public static final String DIALOG_MSG_DELETE_ENTRY = "Do you want to delete this name";
    public static final String DIALOG_MSG_DELETE_EXPENSE = "Do you want to delete this expense";

    public static final String TOAST_MSG_SUCCES_NAME_DELETE = "Selected name has been deleted successfully";
    public static final String TOAST_MSG_SUCCES_ENTRY_DELETE = "Selected entry has been deleted successfully";
    public static final String TOAST_MSG_SUCCES_EXPENSE_UPDATE = "Selected expense has been updated successfully";
    public static final String TOAST_MSG_SUCCES_EXPENSE_DELETE = "Selected expense has been deleted successfully";
    public static final String DIALOG_BUTTON_TEXT_YES = "YES";
    public static final String DIALOG_BUTTON_TEXT_NO = "NO";

    public static final String TOAST_MSG_SUCCES_SYNC = "Record has been synced successfully";

    public static final String DIALOG_ENTRY_SYNC = "After sync you can not update or delete entry";


}
