package com.mobilophilia.mydairy.database;

/**
 * Created by Hanji on 7/21/2017.
 */

public class SetPriceEntry {

    private int id;
    private Double lowFat;
    private Double highFat;
    private Double lowSnf;
    private Double highSnf;
    private Double StartPrice;
    private Double snfInterval;
    private String createdAt;
    private int type;//0-Cow,1-Buffalo
    private int time;// 0-Morning,1-Evening
    private double fatInterval;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLowFat() {
        return lowFat;
    }

    public void setLowFat(Double lowFat) {
        this.lowFat = lowFat;
    }

    public Double getHighFat() {
        return highFat;
    }

    public void setHighFat(Double highFat) {
        this.highFat = highFat;
    }

    public Double getLowSnf() {
        return lowSnf;
    }

    public void setLowSnf(Double lowSnf) {
        this.lowSnf = lowSnf;
    }

    public Double getHighSnf() {
        return highSnf;
    }

    public void setHighSnf(Double highSnf) {
        this.highSnf = highSnf;
    }

    public Double getStartPrice() {
        return StartPrice;
    }

    public void setStartPrice(Double startPrice) {
        StartPrice = startPrice;
    }

    public Double getSnfInterval() {
        return snfInterval;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setSnfInterval(Double interval) {
        this.snfInterval = interval;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setFatInterval(double fatInterval) {
        this.fatInterval = fatInterval;
    }

    public double getFatInterval() {
        return fatInterval;
    }
}
