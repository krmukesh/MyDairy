package com.mobilophilia.mydairy.database;

/**
 * Created by Hanji on 8/2/2017.
 */

public class MyEntries {
    private int code;
    private int id;
    private Double clr;
    private Double snf;
    private Double fat;
    private Double ltr;
    private Double price;
    private Double total = 0.0d;
    private String createdAt;
    private String name;

    private int contentType;
    private String headerText;


    public Double avgFat;
    public Double avgSnf;
    public Double avgPrice;

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




    public int getMorOrEveTime() {
        return morOrEveTime;
    }

    public void setMorOrEveTime(int morOrEveTime) {
        this.morOrEveTime = morOrEveTime;
    }

    public Double totalLtr;
    public Double grandPrice;
    private String phone;
    private int milkType;
    private int morOrEveTime; // 0 moring , 1 evening


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLtr() {
        return ltr;
    }

    public void setLtr(Double ltr) {
        this.ltr = ltr;
    }


    public Double getSnf() {
        return snf;
    }

    public void setSnf(Double snf) {
        this.snf = snf;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Double getClr() {
        return clr;
    }

    public void setClr(Double clr) {
        this.clr = clr;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setMilkType(int milkType) {
        this.milkType = milkType;
    }

    public int getMilkType() {
        return milkType;
    }
}
