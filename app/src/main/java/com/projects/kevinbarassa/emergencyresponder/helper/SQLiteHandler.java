package com.projects.kevinbarassa.emergencyresponder.helper;

/**
 * Created by Kevin Barassa on 18-Nov-16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "emergency_responder";

    // Login table name
    private static final String TABLE_USER = "user";

    // ICE table name
    private static final String TABLE_ICE = "ice_contact";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_SIMU = "phone";
    private static final String KEY_ALLERGY = "allergy";
    private static final String KEY_BLOOD = "blood";
    private static final String KEY_PROBLEM = "problem";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_CREATED_AT = "created_at";

    // ICE Table Columns names
    private static final String ICE_ID = "id";
    private static final String ICE_NAME = "name";
    private static final String ICE_EMAIL = "email";
    private static final String ICE_UID = "uid";
    private static final String ICE_SIMU = "phone";
    private static final String ICE_RESIDENCE = "residence";
    private static final String ICE_BLOOD = "blood";
    private static final String ICE_UPDATED_AT = "updated_at";
    private static final String ICE_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_BLOOD + " TEXT," + KEY_SIMU + " TEXT,"
                + KEY_ALLERGY + " TEXT," + KEY_PROBLEM + " TEXT," + KEY_UPDATED_AT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_ICE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ICE + "("
                + ICE_ID + " INTEGER PRIMARY KEY," + ICE_NAME + " TEXT,"
                + ICE_EMAIL + " TEXT UNIQUE," + ICE_UID + " TEXT,"
                + ICE_BLOOD + " TEXT," + ICE_SIMU + " TEXT,"
                + ICE_RESIDENCE + " TEXT," + ICE_UPDATED_AT + " TEXT,"
                + ICE_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_ICE_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid, String blood, String phone, String allergy, String problem, String updated_at, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // uid
        values.put(KEY_BLOOD, blood); // Blood
        values.put(KEY_SIMU, phone); // Phone
        values.put(KEY_ALLERGY, allergy); // Allergy
        values.put(KEY_PROBLEM, problem); // Condition
        values.put(KEY_UPDATED_AT, updated_at); // Updated At
        values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing ICE Contact details in database
     * */
    public void addICE(String name, String email, String uid, String blood, String phone, String residence, String updated_at, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ICE_NAME, name); // Name
        values.put(ICE_EMAIL, email); // Email
        values.put(ICE_UID, uid); // uid
        values.put(ICE_BLOOD, blood); // Blood
        values.put(ICE_SIMU, phone); // Phone
        values.put(ICE_RESIDENCE, residence); // Residence
        values.put(ICE_UPDATED_AT, updated_at); // Updated At
        values.put(ICE_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_ICE, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("blood", cursor.getString(4));
            user.put("phone", cursor.getString(5));
            user.put("allergy", cursor.getString(6));
            user.put("problem", cursor.getString(7));
            user.put("updated_at", cursor.getString(8));
            user.put("created_at", cursor.getString(9));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting ICE Contact data from database
     * */
    public HashMap<String, String> getICEDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_ICE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("blood", cursor.getString(4));
            user.put("phone", cursor.getString(5));
            user.put("residence", cursor.getString(6));
            user.put("updated_at", cursor.getString(7));
            user.put("created_at", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteICE() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ICE, null, null);
        db.close();

        Log.d(TAG, "Deleted all ICE info from sqlite");
    }


    /**
     * Actually deletes all tables...not just users
     */
    public void dropDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Create tables again
        onCreate(db);
    }
    /**
     * Delete ICE tables
     */
    public void dropICE(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Drop table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ICE);
        // Create tables again
        onCreate(db);
        Log.d(TAG, "Dropped existing tables, and re-created a blank one");
    }

}