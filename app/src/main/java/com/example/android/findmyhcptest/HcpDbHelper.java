package com.example.android.findmyhcptest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HcpDbHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hcp.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_HCP = "CREATE TABLE " + HCPContract.HCPEntry.TABLE_NAME + " (" +
            HCPContract.HCPEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_ADDRESS + TEXT_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_CITY + TEXT_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_STATE + TEXT_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_ZIP + INTEGER_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_AVG_COVERED_CHARGES + REAL_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_AVG_TOTAL_PAYMENTS + REAL_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_AVG_MEDICARE_PAYMENTS + REAL_TYPE + COMMA_SEP +
            HCPContract.HCPEntry.COLUMN_TOTAL_DISCHARGES + INTEGER_TYPE + " )";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + HCPContract.HCPEntry.TABLE_NAME;

    //need constructor with just context...
    public HcpDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HCP);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

}
