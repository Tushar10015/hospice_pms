package com.hospicebangladesh.pms.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.hospicebangladesh.pms.db.BriteDBHelper;
import com.hospicebangladesh.pms.db.DB;
import com.hospicebangladesh.pms.db.DBHelper;
import com.hospicebangladesh.pms.model.Profile;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.ArrayList;
import java.util.List;


public class ProfileRepository {

    public static List<Profile> getAll(Context context){
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
          List<Profile> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+ DB.getInstance().getPatientProfile().TABLE+" ORDER BY "+DB.getInstance().getPatientProfile().C_id, null);
        if(cursor!=null && cursor.moveToFirst()){
            do{
                Profile profile = new Profile();
                profile.setName(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_name)));
                profile.setUsername(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_user_name)));
                profile.setPassword(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_password)));
                profile.setMobile(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_mobile)));
                profile.setEmail(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_email)));
                profile.setGender(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_gender)));
                profile.setAge(cursor.getString(cursor.getColumnIndex(DB.getInstance().getPatientProfile().C_age)));
                list.add(profile);
            }while(cursor.moveToNext());
            cursor.close();
        }

        return list;
    }


    public static void insert(Context context, List<Profile> list){
        BriteDatabase db = BriteDBHelper.getBriteDatabase(context);
        for(Profile profile: list){
            ContentValues cv = new ContentValues();
            cv.put(DB.getInstance().getPatientProfile().C_name, profile.getName());
            cv.put(DB.getInstance().getPatientProfile().C_user_name, profile.getUsername());
            cv.put(DB.getInstance().getPatientProfile().C_password, profile.getPassword());
            cv.put(DB.getInstance().getPatientProfile().C_mobile, profile.getMobile());
            cv.put(DB.getInstance().getPatientProfile().C_email, profile.getEmail());
            cv.put(DB.getInstance().getPatientProfile().C_gender, profile.getGender());
            cv.put(DB.getInstance().getPatientProfile().C_age, profile.getAge());
            db.insert(DB.getInstance().getPatientProfile().TABLE, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public static void insert(Context context,ContentValues cv ){
        BriteDatabase db = BriteDBHelper.getBriteDatabase(context);
            db.insert(DB.getInstance().getPatientProfile().TABLE, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }


}
