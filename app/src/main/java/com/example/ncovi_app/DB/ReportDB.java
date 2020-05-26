package com.example.ncovi_app.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.ncovi_app.Model.Report;

public class ReportDB {
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public ReportDB(Context context) {
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
        String[] cot = {DBHelper.Date_Time_COL,
                DBHelper.Address_COL,
                DBHelper.Detail_COL
        };
        Cursor cursor = null;
        cursor = database.query(DBHelper.
                Report_TABLE, cot, null, null, null, null, null);
        return cursor;
    }

    public long add(Report report) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.Date_Time_COL, report.getDateTime());
        values.put(DBHelper.Address_COL, report.getAddress());
        values.put(DBHelper.Detail_COL, report.getDetail());
        return database.insert(DBHelper.
                Report_TABLE, null, values);
    }

//    public void delete(int soPhieu) {
//        database.delete(DBHelper
//                .TEN_BANG_PHAN_CONG, DBHelper
//                .COT_SO_PHIEU + " = " + "'" +
//                soPhieu + "'", null);
//    }


//    public void edit(Report report) {
//        ContentValues values = new ContentValues();
//        values.put(DBHelper.Address_COL, report.getAddress());
//        values.put(DBHelper.Detail_COL, report.getDetail());
//        database.update(DBHelper
//                        .Report_TABLE, values,
//                DBHelper.Date_Time_COL + " = '"
//                        + report.getDateTime()+"'", null);
//    }
}
