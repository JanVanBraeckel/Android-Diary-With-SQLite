package com.example.jan.sqlitediary.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.HashMap;

public class DiaryContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher;
    private MyDB mDatabase;
    private static HashMap<String, String> sEntryProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Constants.AUTHORITY, "entries", Constants.ENTRY);
        sUriMatcher.addURI(Constants.AUTHORITY, "entries/#", Constants.ENTRY_ID);

        sEntryProjectionMap = new HashMap<>();
        sEntryProjectionMap.put(Constants.DiaryTable._ID, Constants.DiaryTable._ID);
        sEntryProjectionMap.put(Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE, Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE);
        sEntryProjectionMap.put(Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT, Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT);
        sEntryProjectionMap.put(Constants.DiaryTable.COLUMN_NAME_DIARY_DATE, Constants.DiaryTable.COLUMN_NAME_DIARY_DATE);
    }

    public DiaryContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mDatabase = new MyDB(getContext());
        mDatabase.open();
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count;
        switch(sUriMatcher.match(uri)){
            case Constants.ENTRY:
                count = mDatabase.delete(Constants.DiaryTable.TABLE_NAME, where, whereArgs);
                break;
            case Constants.ENTRY_ID:
                String entryId = uri.getPathSegments().get(1);
                count = mDatabase.delete(Constants.DiaryTable.TABLE_NAME, Constants.DiaryTable._ID + "="+ entryId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), whereArgs);
                break;
            default: throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case Constants.ENTRY:
            case Constants.ENTRY_ID:
                return Constants.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != Constants.ENTRY) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        Long now = Long.valueOf(System.currentTimeMillis());

        if (!values.containsKey(Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE)) {
            values.put(Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE, "EMPTY");
        }

        if (!values.containsKey(Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT)) {
            values.put(Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT, "EMPTY");
        }

        if (!values.containsKey(Constants.DiaryTable.COLUMN_NAME_DIARY_DATE)) {
            values.put(Constants.DiaryTable.COLUMN_NAME_DIARY_DATE, now);
        }

        long rowId = mDatabase.insertDiary(Constants.DiaryTable.TABLE_NAME, Constants.DiaryTable.CONTENT ,values);
        if(rowId > 0){
            Uri diaryUri = ContentUris.withAppendedId(Constants.DiaryTable.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(diaryUri, null);
            return diaryUri;
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Constants.DiaryTable.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case Constants.ENTRY:
                qb.setProjectionMap(sEntryProjectionMap);
                break;
            case Constants.ENTRY_ID:
                qb.setProjectionMap(sEntryProjectionMap);
                qb.appendWhere(Constants.DiaryTable._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = Constants.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        Cursor c = mDatabase.getDiaries(qb, projection, selection, selectionArgs, null, null, orderBy);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch(sUriMatcher.match(uri)){
            case Constants.ENTRY:
                count = mDatabase.update(Constants.DiaryTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case Constants.ENTRY_ID:
                String entryId = uri.getPathSegments().get(1);
                count = mDatabase.update(Constants.DiaryTable.TABLE_NAME, values, Constants.DiaryTable._ID + "=" + entryId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
