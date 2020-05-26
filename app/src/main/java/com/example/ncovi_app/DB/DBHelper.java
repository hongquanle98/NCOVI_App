package com.example.ncovi_app.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "NCOVI_App";
    // bảng UserInfo
    static final String User_Info_TABLE = "UserInfo";
    static final String Full_Name_COL = "_full_name";
    static final String ID_Number_COL = "_id_number";
    static final String BHXH_Number_COL = "_bhxh_number";
    static final String Birth_Day_COL = "_birth_day";
    static final String Sex_COL = "_sex";
    static final String Nationality_COL = "_nationality";
    static final String City_COL = "_city";
    static final String District_COL = "_district";
    static final String Ward_COL = "_ward";
    static final String Street_COL = "_street";
    static final String Phone_COL = "_phone";
    static final String Email_COL = "_email";
    // bảng HealthHistory
    static final String Health_History_TABLE = "HealthHistory";
    static final String Date_COL = "_date";
    static final String Time_COL = "_time";
    static final String Status_COL = "_status";
    static final String Info_COL = "_info";
    // bảng Report
    static final String Report_TABLE = "Report";
    static final String Date_Time_COL = "_date_time";
    static final String Address_COL = "_status";
    static final String Detail_COL = "_info";


    private static final String CREATE_User_Info_TABLE = ""
            + "create table " + User_Info_TABLE + " ( "
            + Full_Name_COL + " text not null ,"
            + ID_Number_COL + " text not null, "
            + BHXH_Number_COL + " text not null, "
            + Birth_Day_COL + " text not null, "
            + Sex_COL + " text not null, "
            + Nationality_COL + " text not null, "
            + City_COL + " text not null, "
            + District_COL + " text not null, "
            + Ward_COL + " text not null, "
            + Street_COL + " text not null, "
            + Phone_COL + " text not null, "
            + Email_COL + " text not null);";
    private static final String CREATE_Health_History_TABLE = ""
            + "create table " + Health_History_TABLE + " ( "
            + Date_COL + " text not null ,"
            + Time_COL + " text not null, "
            + Status_COL + " text not null, "
            + Info_COL + " text not null,"
            + " primary key ("+Date_COL+", "+Time_COL+"));";
    private static final String CREATE_Report_TABLE = ""
            + "create table " + Report_TABLE + " ( "
            + Date_Time_COL + " text primary key not null ,"
            + Address_COL + " text not null, "
            + Detail_COL + " text not null);";


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_User_Info_TABLE);
        db.execSQL(CREATE_Health_History_TABLE);
        db.execSQL(CREATE_Report_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
