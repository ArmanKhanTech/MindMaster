package com.android.achievix.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "BlockDatabase";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BlockData";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PACKAGE_NAME = "packageName";
    private static final String TYPE = "type";
    private static final String APP_LAUNCH = "appLaunch";
    private static final String NOTIFICATION = "notification";
    private static final String SCHEDULE_TYPE = "scheduleType";
    private static final String SCHEDULE_PARAMS = "scheduleParams";
    private static final String SCHEDULE_DAYS = "scheduleDays";
    private static final String PROFILE_NAME = "profileName";
    private static final String PROFILE_STATUS = "profileStatus";
    private static final String TEXT = "text";

    public BlockDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + PACKAGE_NAME + " TEXT, "
                + TYPE + " TEXT, "
                + APP_LAUNCH + " BOOL, "
                + NOTIFICATION + " BOOL, "
                + SCHEDULE_TYPE + " TEXT, "
                + SCHEDULE_PARAMS + " TEXT, "
                + SCHEDULE_DAYS + " TEXT, "
                + PROFILE_NAME + " TEXT, "
                + PROFILE_STATUS + " BOOL, "
                + TEXT + " TEXT " + " )";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addRecord(String name, String packageName, String type, boolean appLaunch, boolean notification, String scheduleType, String scheduleParams, String scheduleDays, String profileName, boolean profileStatus, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(PACKAGE_NAME, packageName);
        values.put(TYPE, type);
        values.put(APP_LAUNCH, appLaunch);
        values.put(NOTIFICATION, notification);
        values.put(SCHEDULE_TYPE, scheduleType);
        values.put(SCHEDULE_PARAMS, scheduleParams);
        values.put(SCHEDULE_DAYS, scheduleDays);
        values.put(PROFILE_NAME, profileName);
        values.put(PROFILE_STATUS, profileStatus);
        values.put(TEXT, text);
        db.insert(TABLE_NAME, null, values);
    }

    public List<HashMap<String, String>> readRecords(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?", new String[]{packageName});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(APP_LAUNCH, cursor.getString(4));
            map.put(NOTIFICATION, cursor.getString(5));
            map.put(SCHEDULE_TYPE, cursor.getString(6));
            map.put(SCHEDULE_PARAMS, cursor.getString(7));
            map.put(SCHEDULE_DAYS, cursor.getString(8));
            map.put(PROFILE_NAME, cursor.getString(9));
            map.put(PROFILE_STATUS, cursor.getString(10));
            map.put(TEXT, cursor.getString(11));
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public Cursor readAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean isAppBlocked(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?" + " AND " + PROFILE_NAME + " IS NULL", new String[]{packageName});
        boolean blocked =  cursor.getCount() > 0;
        cursor.close();
        return blocked;
    }

    public void updateRecord(String id, String name, String packageName, String type, boolean appLaunch, boolean notification, String scheduleType, String scheduleParams, String scheduleDays, String profileName, boolean profileStatus, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(PACKAGE_NAME, packageName);
        values.put(TYPE, type);
        values.put(APP_LAUNCH, appLaunch);
        values.put(NOTIFICATION, notification);
        values.put(SCHEDULE_TYPE, scheduleType);
        values.put(SCHEDULE_PARAMS, scheduleParams);
        values.put(SCHEDULE_DAYS, scheduleDays);
        values.put(PROFILE_NAME, profileName);
        values.put(PROFILE_STATUS, profileStatus);
        values.put(TEXT, text);
        db.update(TABLE_NAME, values, ID + " = ?", new String[]{id});
    }

    public void deleteRecordByPackageName(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[]{packageName});
    }
}