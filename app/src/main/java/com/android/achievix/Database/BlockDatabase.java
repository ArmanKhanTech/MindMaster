package com.android.achievix.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BlockDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "BlockDatabase";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "BlockData";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PACKAGE_NAME = "packageName";
    private static final String TYPE = "type";
    private static final String LAUNCH = "launch";
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
                + LAUNCH + " BOOL, "
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
        values.put(LAUNCH, appLaunch);
        values.put(NOTIFICATION, notification);
        values.put(SCHEDULE_TYPE, scheduleType);
        values.put(SCHEDULE_PARAMS, scheduleParams);
        values.put(SCHEDULE_DAYS, scheduleDays);
        values.put(PROFILE_NAME, profileName);
        values.put(PROFILE_STATUS, profileStatus);
        values.put(TEXT, text);
        db.insert(TABLE_NAME, null, values);
    }

    public List<HashMap<String, String>> readRecordsApp(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?", new String[]{packageName});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readRecordsWeb(String url) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{url, "web"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readRecordsKey(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{key, "key"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readRecordsInternet(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{packageName, "internet"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readAllRecordsWeb() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " = ?" + " AND " + PROFILE_NAME + " IS NULL", new String[]{"web"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readAllRecordsKey() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " = ?" + " AND " + PROFILE_NAME + " IS NULL", new String[]{"key"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readAllProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " = ?", new String[]{"profile"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(PROFILE_NAME, cursor.getString(9));
            map.put(PROFILE_STATUS, cursor.getString(10));
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public List<HashMap<String, String>> readProfileSchedule(String profileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " = ?", new String[]{profileName});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(TYPE, cursor.getString(3));
            map.put(LAUNCH, cursor.getString(4));
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

    public List<HashMap<String, String>> readProfileApps(String profileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{profileName, "app"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(PROFILE_NAME, cursor.getString(9));
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public List<HashMap<String, String>> readProfileWebs(String profileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{profileName, "web"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(PROFILE_NAME, cursor.getString(9));
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public List<HashMap<String, String>> readProfileKeys(String profileName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " = ?" + " AND " + TYPE + " = ?", new String[]{profileName, "key"});
        List<HashMap<String, String>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(ID, cursor.getString(0));
            map.put(NAME, cursor.getString(1));
            map.put(PACKAGE_NAME, cursor.getString(2));
            map.put(TYPE, cursor.getString(3));
            map.put(PROFILE_NAME, cursor.getString(9));
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public void addAllItemsToNewProfileSchedule(boolean launch, boolean noti, String profileName, String scheduleType, String scheduleParams, String scheduleDays, boolean profileStatus, String text) {
        List<HashMap<String, String>> list = readProfileWebs(profileName);
        for (HashMap<String, String> map : list) {
            addRecord(
                    map.get(NAME),
                    map.get(PACKAGE_NAME),
                    map.get(TYPE),
                    launch,
                    noti,
                    scheduleType,
                    scheduleParams,
                    scheduleDays,
                    profileName,
                    profileStatus,
                    text
            );
        }
        list.clear();

        list = readProfileKeys(profileName);
        for (HashMap<String, String> map : list) {
            addRecord(
                    map.get(NAME),
                    map.get(PACKAGE_NAME),
                    map.get(TYPE),
                    launch,
                    noti,
                    scheduleType,
                    scheduleParams,
                    scheduleDays,
                    profileName,
                    profileStatus,
                    text
            );
        }
        list.clear();

        list = readProfileApps(profileName);
        for (HashMap<String, String> map : list) {
            addRecord(
                    map.get(NAME),
                    map.get(PACKAGE_NAME),
                    map.get(TYPE),
                    launch,
                    noti,
                    scheduleType,
                    scheduleParams,
                    scheduleDays,
                    profileName,
                    profileStatus,
                    text
            );
        }
    }

    public void deleteProfileItem(String profileName, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PROFILE_NAME + " = ?" + " AND " + NAME + " = ?", new String[]{profileName, name});
    }

    public int getAppBlockCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE + " = ?" + " AND " + PROFILE_NAME + " IS NULL", new String[]{"app"});

        HashSet<String> uniqueApps = new HashSet<>();
        while (cursor.moveToNext()) {
            uniqueApps.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }

        cursor.close();
        return uniqueApps.size();
    }

    public int getInternetBlockCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " IS NULL" + " AND " + TYPE + " = ?", new String[]{"internet"});

        HashSet<String> uniqueInternet = new HashSet<>();
        while (cursor.moveToNext()) {
            uniqueInternet.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }

        cursor.close();
        return uniqueInternet.size();
    }

    public int getKeysBlockCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " IS NULL" + " AND " + TYPE + " = ?", new String[]{"key"});

        HashSet<String> uniqueKeys = new HashSet<>();
        while (cursor.moveToNext()) {
            uniqueKeys.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }

        cursor.close();
        return uniqueKeys.size();
    }

    public int getWebBlockCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PROFILE_NAME + " IS NULL" + " AND " + TYPE + " = ?", new String[]{"web"});

        HashSet<String> uniqueWeb = new HashSet<>();
        while (cursor.moveToNext()) {
            uniqueWeb.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        }

        cursor.close();
        return uniqueWeb.size();
    }

    public void deleteProfileItems(String profileName, String scheduleType, String scheduleParams, String scheduleDays) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PROFILE_NAME + " = ?" + " AND " + SCHEDULE_TYPE + " = ?" + " AND " + SCHEDULE_PARAMS + " = ?" + " AND " + SCHEDULE_DAYS + " = ?", new String[]{profileName, scheduleType, scheduleParams, scheduleDays});
    }

    public void toggleProfile(String profileName, boolean status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PROFILE_STATUS, !status);
        db.update(TABLE_NAME, values, PROFILE_NAME + " = ?", new String[]{profileName});
    }

    public boolean isAppBlocked(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?" + " AND " + PROFILE_NAME + " IS NULL" + " AND " + TYPE + " = ?", new String[]{packageName, "app"});
        boolean blocked =  cursor.getCount() > 0;
        cursor.close();
        return blocked;
    }

    public boolean isInternetBlocked(String packageName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PACKAGE_NAME + " = ?" + " AND " + PROFILE_NAME + " IS NULL" + " AND " + TYPE + " = ?", new String[]{packageName, "internet"});
        boolean blocked = cursor.getCount() > 0;
        cursor.close();
        return blocked;
    }

    public void deleteRecordById(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[]{id});
    }
}