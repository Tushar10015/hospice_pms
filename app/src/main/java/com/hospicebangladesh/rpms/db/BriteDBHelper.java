package com.hospicebangladesh.rpms.db;

import android.content.ContentResolver;
import android.content.Context;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by This pc on 9/26/2017.
 */

public class BriteDBHelper {

    private static SqlBrite sqlBrite;
    private static BriteDatabase briteDatabase;
    private static BriteContentResolver briteContentResolver;

    private static SqlBrite getSqlBrite(){
        if(sqlBrite==null){
            sqlBrite = new SqlBrite.Builder().build();
        }

        return sqlBrite;
    }

    public static BriteDatabase getBriteDatabase(Context context){
        if(briteDatabase==null){
            briteDatabase = getSqlBrite().wrapDatabaseHelper(DBHelper.getInstance(context), Schedulers.io());
        }

        return briteDatabase;
    }

    public static BriteContentResolver getBriteResolver(ContentResolver resolver){
        if(briteContentResolver==null) {
            briteContentResolver = getSqlBrite().wrapContentProvider(resolver, Schedulers.io());
        }

        return briteContentResolver;
    }

}
