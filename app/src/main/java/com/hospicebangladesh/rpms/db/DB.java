package com.hospicebangladesh.rpms.db;

import android.provider.BaseColumns;

/**
 * Created by This pc on 9/26/2017.
 */

public class DB {

    private static DB instance;
    private PatientProfile patientProfile;


    public class PatientProfile implements BaseColumns {

        public final String TABLE = "patient_profile";
        public final String C_id = "id";
        public final String C_name = "name";
        public final String C_user_name = "user_name";
        public final String C_password = "password";
        public final String C_email = "email";
        public final String C_mobile = "mobile";
        public final String C_gender = "gender";
        public final String C_age = "age";


        public final String SQL_CREATE = "CREATE TABLE " + TABLE + "("

                + C_id + " INTEGER PRIMARY KEY, "
                + C_name + " TEXT,"
                + C_user_name + " TEXT,"
                + C_password + " TEXT,"
                + C_email + " TEXT,"
                + C_mobile + " TEXT,"
                + C_gender + " TEXT,"
                + C_age + " TEXT"
                + ");";

        public final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE;


    }



    public static DB getInstance() {
        if (instance == null)
            instance = new DB();

        return instance;
    }

    public PatientProfile getPatientProfile() {
        if (patientProfile == null)
            patientProfile = new PatientProfile();

        return patientProfile;
    }



}
