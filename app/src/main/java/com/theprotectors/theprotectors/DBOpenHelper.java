package com.theprotectors.theprotectors;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "theprotectors.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_ID = "_id";
    public static final String CONTACT_FIRSTNAME = "firstName";
    public static final String CONTACT_LASTNAME = "lastName";
    public static final String CONTACT_PHONE = "phone";

    public static final String[] ALL_COLUMNS = {CONTACT_ID, CONTACT_FIRSTNAME, CONTACT_LASTNAME};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_FIRSTNAME + " TEXT, " +
                    CONTACT_LASTNAME + " TEXT," +
                    CONTACT_PHONE + " INTEGER" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }
}
