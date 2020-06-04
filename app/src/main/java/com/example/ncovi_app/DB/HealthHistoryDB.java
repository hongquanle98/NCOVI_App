package com.example.ncovi_app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.ncovi_app.Model.HealthHistory;

public class HealthHistoryDB {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public HealthHistoryDB(Context context) {
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

    public Cursor getAllData() {
        // Biến cot là khai báo danh sách các cột cần lấy.
        String[] cot = {DBHelper.Date_COL,
                DBHelper.Time_COL,
                DBHelper.Status_COL,
                DBHelper.Info_COL
        };
        Cursor cursor = null;
        cursor = database.query(DBHelper.
                Health_History_TABLE, cot, null, null, null, null, "DATE("+DBHelper.Date_COL +") DESC"+", "+DBHelper.Time_COL +" DESC");
        return cursor;
    }

    public long add(HealthHistory healthHistory) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.Date_COL, healthHistory.getDate());
        values.put(DBHelper.Time_COL, healthHistory.getTime());
        values.put(DBHelper.Status_COL, healthHistory.getStatus());
        values.put(DBHelper.Info_COL, healthHistory.getInfo());
        return database.insert(DBHelper.
                Health_History_TABLE, null, values);
    }

    public void delete(HealthHistory healthHistory) {
        database.delete(DBHelper
                .Health_History_TABLE, DBHelper
                .Date_COL + " = " + "'" +
                healthHistory.getDate() + "' AND " + DBHelper.Time_COL + " = '" +healthHistory.getTime()+"'", null);
    }


//    public void edit(HealthHistory healthHistory) {
//        ContentValues values = new ContentValues();
//        //values.put(DBHelper.Date_COL, healthHistory.getDate());
//        values.put(DBHelper.Time_COL, healthHistory.getTime());
//        values.put(DBHelper.Status_COL, healthHistory.getStatus());
//        values.put(DBHelper.Info_COL, healthHistory.getInfo());
//        database.update(DBHelper
//                        .Health_History_TABLE, values,
//                DBHelper.Date_COL + " = '"
//                        + healthHistory.getDate()+"'", null);
//    }
}
