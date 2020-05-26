package com.example.ncovi_app.Model;

public class UserInfo {
    private String fullName, iDNumber, bHXHNumber, birthDay, sex, nationality, city, district, ward, street, phone, email;

    public UserInfo (){}

    public UserInfo(String fullName, String iDNumber, String bHXHNumber, String birthDay, String sex, String nationality, String city, String district, String ward, String street, String phone, String email) {
        this.fullName = fullName;
        this.iDNumber = iDNumber;
        this.bHXHNumber = bHXHNumber;
        this.birthDay = birthDay;
        this.sex = sex;
        this.nationality = nationality;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.street = street;
        this.phone = phone;
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getbHXHNumber() {
        return bHXHNumber;
    }

    public void setbHXHNumber(String bHXHNumber) {
        this.bHXHNumber = bHXHNumber;
    }
}
