package com.example.ncovi_app.Model;

public class Report {
    private String dateTime, address, detail;

    public Report (){}

    public Report(String dateTime, String address, String detail) {
        this.dateTime = dateTime;
        this.address = address;
        this.detail = detail;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
