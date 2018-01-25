package com.hospicebangladesh.pms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by This pc on 9/26/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME ="hospice.db";
    private static int version=1;
    private static DBHelper instance;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    public  static synchronized  DBHelper getInstance(Context context){
        if (instance== null){
            instance = new DBHelper(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( DB.getInstance().getPatientProfile().SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DB.getInstance().getPatientProfile().SQL_DROP);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
