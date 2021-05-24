package com.shamil.applocker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Packages.db";
    public static final String TABLE_NAME = "packages";
    public static final String COLUMN_NAME = "name";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table packages " + "(id integer primary key, name text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS packages");
        onCreate(db);
    }

    public boolean insertPackage(String packages) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", packages);
        db.insert("packages", null, contentValues);
        return true;
    }

    public boolean deletePackage(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_NAME + "=?", new String[]{name}) > 0;
    }

    public ArrayList<String> getAllPackage() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from packages", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}