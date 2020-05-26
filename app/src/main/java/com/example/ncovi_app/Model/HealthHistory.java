package com.example.ncovi_app.Model;

public class HealthHistory {
    private String date, time, status, info;

    public HealthHistory (){}

    public HealthHistory(String date, String time, String status, String info) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
