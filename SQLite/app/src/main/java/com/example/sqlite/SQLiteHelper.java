package com.example.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDatebase";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "MyTable";
    public static final String ID = "ID";
    public static final String TITLE = "title";   // Title → TITLE (MainActivity와 일치)
    public static final String TIME = "time";

    // "CREATE TABLE" 뒤 공백 추가 + 컬럼 순서: ID, time, title (MainActivity의 getLong(1), getString(2)와 맞춤)
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TIME + " TEXT, " +
            TITLE + " TEXT" +
            " )";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE); // TABLE_NAME 실행 → SQL 실행으로 교체
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
