package com.SE.team12.pillalarm2021;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbHelper extends SQLiteOpenHelper {

    // singleton
    // have one instance
    private static MemoDbHelper sInstance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Memo.db";
    public static final String SQL_CREATE_ENTERS =
            String.format(
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
                    MemoContract.MemoEntry.TABLE_NAME,
                    MemoContract.MemoEntry._ID,
                    MemoContract.MemoEntry.COLUMN_NAME_TITLE,
                    MemoContract.MemoEntry.COLUMN_NAME_CONTENT,
                    MemoContract.MemoEntry.COLUMN_NAME_IMAGE);

    public static final String SQL_DELETE_ENTERS =
            "DROP TABLE IF EXISTS " + MemoContract.MemoEntry.TABLE_NAME;

    public static MemoDbHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MemoDbHelper(context);
        }
        return sInstance;
    }

    private MemoDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 최초의 DB 생성 부분
    // First DB generation part
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERS);
    }

    // DB가 변경될 경우 버전을 올려주고
    // DB 변경점을 대응
    // If the DB changes, can upload the version.
    // respond to DB changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTERS);
        onCreate(db);
    }
}