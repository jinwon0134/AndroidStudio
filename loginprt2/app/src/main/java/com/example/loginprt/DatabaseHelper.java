package com.example.loginprt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_PW = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " TEXT PRIMARY KEY, " +
                COL_PW + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // 로그인
    public boolean loginUser(String id, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE id=? AND password=?",
                new String[]{id, password});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    // 회원가입 (RegisterActivity에서 사용)
    public boolean registerUser(String id, String password) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE id=?", new String[]{id});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // 이미 존재
        }

        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_PW, password);

        long result = db.insert(TABLE_USERS, null, values);
        cursor.close();
        return result != -1;
    }
}
