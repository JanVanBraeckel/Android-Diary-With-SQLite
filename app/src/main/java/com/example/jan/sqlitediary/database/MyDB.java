package com.example.jan.sqlitediary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by Jan on 19/12/2015.
 */
public class MyDB {

    private SQLiteDatabase mDatabase;
    private MyDBHelper mDBHelper;

    public MyDB(Context context) {
        mDBHelper = new MyDBHelper(context);
    }

    public void open() {
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public Cursor getDiaries(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return qb.query(mDatabase, projection, selection, selectionArgs, groupBy, having, orderBy);
    }

    public long insertDiary(String tableName, String entry, ContentValues values) {
        return mDatabase.insert(tableName, entry, values);
    }

    public int delete(String tableName, String where, String[] whereArgs) {
        return mDatabase.delete(tableName, where, whereArgs);
    }

    public int update(String tableName, ContentValues values, String s, String[] selectionArgs) {
        return mDatabase.update(tableName, values, s, selectionArgs);
    }
}
