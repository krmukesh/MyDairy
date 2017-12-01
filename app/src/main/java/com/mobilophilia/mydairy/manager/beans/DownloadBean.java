package com.mobilophilia.mydairy.manager.beans;

/**
 * Created by mukesh on 18/08/17.
 */

public class DownloadBean {
    private String fileName;

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    private String downloadTime;
    private String filePath;
    private String headerText;

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }
}
