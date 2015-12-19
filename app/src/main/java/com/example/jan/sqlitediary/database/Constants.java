package com.example.jan.sqlitediary.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jan on 19/12/2015.
 */
public class Constants {
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DiaryTable.TABLE_NAME + " (" +
            DiaryTable._ID + " integer primary key autoincrement, " +
            DiaryTable.COLUMN_NAME_DIARY_TITLE + " text not null, " +
            DiaryTable.COLUMN_NAME_DIARY_CONTENT + " text not null, " +
            DiaryTable.COLUMN_NAME_DIARY_DATE + " long)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DiaryTable.TABLE_NAME;
    public static final String AUTHORITY = "com.example.jan.sqlitediary";
    public static final int ENTRY = 1;
    public static final int ENTRY_ID = 2;
    public static final String DEFAULT_SORT_ORDER = "recordDate DESC";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.jan.provider.entries";

    public static abstract class DiaryTable implements BaseColumns {
        public static final String TABLE_NAME = "diary";
        public static final String COLUMN_NAME_DIARY_TITLE = "title";
        public static final String COLUMN_NAME_DIARY_CONTENT = "content";
        public static final String COLUMN_NAME_DIARY_DATE = "recordDate";
        public static final String CONTENT ="entry";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/entries");
    }
}
