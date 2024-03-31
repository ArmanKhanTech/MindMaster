package com.android.achievix.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class AppLaunchDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "AppLaunchDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "AppLaunchTable";
    private static final String PACKAGE_NAME = "packageName";
    private static final String LAUNCH_COUNT = "launchCount";
    private static final String DATE = "date";

    public AppLaunchDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ( "
                + PACKAGE_NAME + " TEXT, "
                + LAUNCH_COUNT + " INTEGER, "
                + DATE + " TEXT)";
        db.execSQL(query);
    }

    public void incrementLaunchCount(String packageName, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ? AND " + DATE + " = ?", new String[]{packageName, date});

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndexOrThrow(LAUNCH_COUNT));
            ContentValues contentValues = new ContentValues();
            contentValues.put(LAUNCH_COUNT, count + 1);
            db.update(TABLE_NAME, contentValues, PACKAGE_NAME + " = ? AND " + DATE + " = ?", new String[]{packageName, date});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PACKAGE_NAME, packageName);
            contentValues.put(LAUNCH_COUNT, 1);
            contentValues.put(DATE, date);
            db.insert(TABLE_NAME, null, contentValues);
        }
        cursor.close();
    }

    public HashMap<String, Integer> getDailyLaunchCount(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PACKAGE_NAME + ", SUM(" + LAUNCH_COUNT + ") AS total FROM " + TABLE_NAME + " WHERE " + DATE + " = ? GROUP BY " + PACKAGE_NAME, new String[]{date});

        HashMap<String, Integer> result = new HashMap<>();
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            result.put(packageName, count);
        }
        cursor.close();
        return result;
    }

    public int getDailyLaunchCountForSpecificApp(String packageName, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + LAUNCH_COUNT + " FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ? AND " + DATE + " = ?", new String[]{packageName, date});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow(LAUNCH_COUNT));
        }
        cursor.close();
        return count;
    }

    public HashMap<String, Integer> getWeeklyLaunchCount(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PACKAGE_NAME + ", SUM(" + LAUNCH_COUNT + ") AS total FROM " + TABLE_NAME + " WHERE " + DATE + " BETWEEN ? AND ? GROUP BY " + PACKAGE_NAME, new String[]{startDate, endDate});

        HashMap<String, Integer> result = new HashMap<>();
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            result.put(packageName, count);
        }
        cursor.close();
        return result;
    }

    public HashMap<String, Integer> getMonthlyLaunchCount(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PACKAGE_NAME + ", SUM(" + LAUNCH_COUNT + ") AS total FROM " + TABLE_NAME + " WHERE " + DATE + " BETWEEN ? AND ? GROUP BY " + PACKAGE_NAME, new String[]{startDate, endDate});

        HashMap<String, Integer> result = new HashMap<>();
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            result.put(packageName, count);
        }
        cursor.close();
        return result;
    }

    public HashMap<String, Integer> getYearlyLaunchCount(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PACKAGE_NAME + ", SUM(" + LAUNCH_COUNT + ") AS total FROM " + TABLE_NAME + " WHERE " + DATE + " BETWEEN ? AND ? GROUP BY " + PACKAGE_NAME, new String[]{startDate, endDate});

        HashMap<String, Integer> result = new HashMap<>();
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
            int count = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            result.put(packageName, count);
        }
        cursor.close();
        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}