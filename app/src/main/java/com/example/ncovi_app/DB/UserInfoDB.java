package com.example.ncovi_app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.ncovi_app.Model.UserInfo;

public class UserInfoDB {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public UserInfoDB(Context context) {
        dbHelper = new DBHelper(context);
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            database = dbHelper.getReadableDatabase();
        }

    }

    public void close() {
        dbHelper.close();
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + dbHelper.User_Info_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor getAllData() {
        // Biến cot là khai báo danh sách các cột cần lấy.
        String[] cot = {DBHelper.Full_Name_COL,
                DBHelper.ID_Number_COL,
                DBHelper.BHXH_Number_COL,
                DBHelper.Birth_Day_COL,
                DBHelper.Sex_COL,
                DBHelper.Nationality_COL,
                DBHelper.City_COL,
                DBHelper.District_COL,
                DBHelper.Ward_COL,
                DBHelper.Street_COL,
                DBHelper.Phone_COL,
                DBHelper.Email_COL
        };

        Cursor cursor = null;
        cursor = database.query(DBHelper.
                        User_Info_TABLE, cot, null, null, null, null, null);
        //String selectQuery = "SELECT * FROM " + "UserInfo";
        //cursor = database.rawQuery(selectQuery, null);
        //cursor = database.rawQuery("select * from DBHelper.User_Info_TABLE",null);
        return cursor;
    }

    public long add(UserInfo userInfo) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.Full_Name_COL, userInfo.getFullName());
        values.put(DBHelper.ID_Number_COL, userInfo.getiDNumber());
        values.put(DBHelper.BHXH_Number_COL, userInfo.getbHXHNumber());
        values.put(DBHelper.Birth_Day_COL, userInfo.getBirthDay());
        values.put(DBHelper.Sex_COL, userInfo.getSex());
        values.put(DBHelper.Nationality_COL, userInfo.getNationality());
        values.put(DBHelper.City_COL, userInfo.getCity());
        values.put(DBHelper.District_COL, userInfo.getDistrict());
        values.put(DBHelper.Ward_COL, userInfo.getWard());
        values.put(DBHelper.Street_COL, userInfo.getStreet());
        values.put(DBHelper.Phone_COL, userInfo.getPhone());
        values.put(DBHelper.Email_COL, userInfo.getEmail());
        return database.insert(DBHelper.
                User_Info_TABLE, null, values);
    }

//    public void delete(int soPhieu) {
//        database.delete(DBHelper
//                .TEN_BANG_PHAN_CONG, DBHelper
//                .COT_SO_PHIEU + " = " + "'" +
//                soPhieu + "'", null);
//    }


    public void edit(UserInfo userInfo) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.Full_Name_COL, userInfo.getFullName());
        values.put(DBHelper.ID_Number_COL, userInfo.getiDNumber());
        values.put(DBHelper.BHXH_Number_COL, userInfo.getbHXHNumber());
        values.put(DBHelper.Birth_Day_COL, userInfo.getBirthDay());
        values.put(DBHelper.Sex_COL, userInfo.getSex());
        values.put(DBHelper.Nationality_COL, userInfo.getNationality());
        values.put(DBHelper.City_COL, userInfo.getCity());
        values.put(DBHelper.District_COL, userInfo.getDistrict());
        values.put(DBHelper.Ward_COL, userInfo.getWard());
        values.put(DBHelper.Street_COL, userInfo.getStreet());
        values.put(DBHelper.Phone_COL, userInfo.getPhone());
        values.put(DBHelper.Email_COL, userInfo.getEmail());
        database.update(DBHelper
                        .User_Info_TABLE, values,
                1 + " = "
                        + 1, null);
    }


}
