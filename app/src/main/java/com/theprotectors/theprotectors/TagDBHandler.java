package com.theprotectors.theprotectors;

/**
 * Created by apple on 08/12/2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TagDBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TAG.db";

    public TagDBHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static interface tagentry extends BaseColumns {
        String MARKER_OPTIONS = "TITLE";
        String LATITUDE = "latitude";
        String LONTITUDE = "longtitude";
        String PATH_1 = "first_image";
        String PATH_2 = "second_image";
        String PATH_3 = "third_image";
        String TABLE_SHOPS = "Locations";
        String ID_NOTE="_id";
    }

            @Override
        public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_ENTRIES = "CREATE TABLE " + tagentry.TABLE_SHOPS + "("
                    +tagentry.ID_NOTE + " INTEGER PRIMARY KEY,"+ tagentry.MARKER_OPTIONS + "STRING"+"," + tagentry.LATITUDE + "DOUBLE ," + tagentry.LONTITUDE + "DOUBLE"+ "," + tagentry.PATH_1 + "STRING"+","
                    + tagentry.PATH_2 + "STRING"+"," + tagentry.PATH_3 + "STRING" + ")";
            db.execSQL(SQL_CREATE_ENTRIES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS" + tagentry.TABLE_SHOPS);
// Creating tables again
            onCreate(db);

        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    /*public void insertTag (TagDataBase tag)
    {
        ContentValues values = new ContentValues();
        values.put(tagentry.MARKER_OPTIONS, tag.getTitle());
        values.put(tagentry.LATITUDE,tag.getLat());
        values.put(tagentry.LONTITUDE,tag.getLong());
        if(tag.getPath().get(0) != null) {
            values.put(tagentry.PATH_1, tag.getPath().get(0));
        }
        if(tag.getPath().get(1) != null) {
            values.put(tagentry.PATH_2, tag.getPath().get(1));
        }
        if(tag.getPath().get(2) != null) {
            values.put(tagentry.PATH_3, tag.getPath().get(2));
        }
        getWritableDatabase().insert(tagentry.TABLE_SHOPS, null, values);
        getWritableDatabase().close();
    }*/


    public Cursor getTag()
    {
        String[] tag ={tagentry._ID,tagentry.MARKER_OPTIONS,tagentry.LATITUDE,tagentry.LONTITUDE,tagentry.PATH_1,tagentry.PATH_2,tagentry.PATH_3};

        return getReadableDatabase().query(tagentry.TABLE_SHOPS,tag,null,null,null,null,null);
    }


}
