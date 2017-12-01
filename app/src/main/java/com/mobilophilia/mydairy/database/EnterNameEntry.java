package com.mobilophilia.mydairy.database;

/**
 * Created by Hanji on 7/21/2017.
 */

public class EnterNameEntry {


    private int id;
    private int nameCode;
    private String enterName;
    private int type;
    private String phoneNo;
    private String createdAt;
    private int contentType;
    private String headerText;
    private boolean entry;


    // constructors
    public EnterNameEntry() {
    }


    public EnterNameEntry(int id, int nameCode, String enterName, int type, String phoneNo) {
        this.id = id;
        this.nameCode = nameCode;
        this.enterName = enterName;
        this.type = type;
        this.phoneNo = phoneNo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameCode() {
        return nameCode;
    }

    public void setNameCode(int nameCode) {
        this.nameCode = nameCode;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public void setEntry(boolean entry) {
        this.entry = entry;
    }

    public boolean getEntry() {
        return entry;
    }
}
