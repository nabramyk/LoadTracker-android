package com.example.nathan.loadtracker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nathan.loadtracker.jobsession.JobSession;
import com.example.nathan.loadtracker.jobsession.JobSessionContract;
import com.example.nathan.loadtracker.load.Load;
import com.example.nathan.loadtracker.load.LoadContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 2017-10-13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "loadTracker";

    //CREATE
    private static final String JOB_SESSIONS_TABLE_CREATE =
            "CREATE TABLE " + JobSessionContract.JobSessionEntry.TABLE_NAME + " ("
                    + JobSessionContract.JobSessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + JobSessionContract.JobSessionEntry.COLUMN_NAME_JOB_TITLE + " TEXT NOT NULL,"
                    + JobSessionContract.JobSessionEntry.COLUMN_NAME_JOB_DESCRIPTION + "TEXT NULL,"
                    + JobSessionContract.JobSessionEntry.COLUMN_NAME_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                    + JobSessionContract.JobSessionEntry.COLUMN_NAME_MODIFIED + " TIMESTAMP NULL,"
                    + JobSessionContract.JobSessionEntry.COLUMN_NAME_CLOSED + " TIMESTAMP NULL"
                    + ")";

    private static final String LOADS_TABLE_CREATE =
            "CREATE TABLE " + LoadContract.LoadEntry.TABLE_NAME + " ("
                    + LoadContract.LoadEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_JOB_TITLE + " TEXT NOT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_DRIVER + " TEXT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_UNIT_ID + " TEXT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_MATERIAL + " TEXT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_COMPANY_NAME + " TEXT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_TIME_LOADED + " TEXT NOT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_DATE_LOADED + " TEXT NOT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_CREATED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,"
                    + LoadContract.LoadEntry.COLUMN_NAME_MODIFIED + " TIMESTAMP NULL"
                    + ")";

    //READ
    private static final String READ_ALL_JOB_SESSIONS =
            "SELECT * FROM " + JobSessionContract.JobSessionEntry.TABLE_NAME + ";";

    private static final String READ_ALL_LOADS =
            "SELECT * FROM " + LoadContract.LoadEntry.TABLE_NAME + " WHERE " + LoadContract.LoadEntry.COLUMN_NAME_JOB_TITLE + "=";

    private static final String READ_UNIQUE_TIMES =
            "SELECT DISTINCT ";

    //UPDATE
    private static final String UPDATE_JOB_SESSION = "";

    private static final String UPDATE_LOAD = "";

    //DELETE
    private static final String DELETE_LOADS =
            "DROP TABLE IF EXISTS " + LoadContract.LoadEntry.TABLE_NAME;

    private static final String DELETE_JOB_SESSIONS =
            "DROP TABLE IF EXISTS " + JobSessionContract.JobSessionEntry.TABLE_NAME;


    private static final String DELETE_LOAD_ENTRY_FROM_SESSION = "DELETE FROM " + LoadContract.LoadEntry.TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JOB_SESSIONS_TABLE_CREATE);
        db.execSQL(LOADS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_LOADS);
        db.execSQL(DELETE_JOB_SESSIONS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<JobSession> getAllJobSessions() {
        List<JobSession> sessions = new ArrayList<JobSession>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(READ_ALL_JOB_SESSIONS, null);

        if(cursor.moveToFirst()) {
            do {
                JobSession js = new JobSession();
                js.setId(cursor.getInt(0));
                js.setJobTitle(cursor.getString(1));
                js.setCreated(cursor.getString(3));
                js.setClosedDate(cursor.getString(2));
                js.setTotalLoads(getAllLoadsForSession(cursor.getString(1)).size());
                sessions.add(js);
            } while(cursor.moveToNext());
        }
        return sessions;
    }

    public List<Load> getAllLoadsForSession(String session) {
        List<Load> loads = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(READ_ALL_LOADS + "'" + session + "'" + ";", null);

        if(cursor.moveToFirst()) {
            do {
                Load load = new Load();
                load.setId(cursor.getInt(0));
                load.setTitle(cursor.getString(1));
                load.setDriver(cursor.getString(2));
                load.setUnitId(cursor.getString(3));
                load.setMaterial(cursor.getString(4));
                load.setCompanyName(cursor.getString(5));
                load.setTimeLoaded(cursor.getString(6));
                load.setDateLoaded(cursor.getString(7));
                load.setCreated(cursor.getString(8));
                load.setModified(cursor.getString(9));
                loads.add(load);

                Log.d("Reading, Load: ", load.toString());
            } while(cursor.moveToNext());
        }
        return loads;
    }

    public void addJobSession(JobSession js) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(JobSessionContract.JobSessionEntry.COLUMN_NAME_JOB_TITLE, js.getJobTitle());

        db.insert(JobSessionContract.JobSessionEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteJobSession(JobSession js) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = JobSessionContract.JobSessionEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(js.getId())};
        db.delete(JobSessionContract.JobSessionEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void addLoadToSession(Load l) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LoadContract.LoadEntry.COLUMN_NAME_JOB_TITLE, l.getTitle());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_DRIVER, l.getDriver());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_MATERIAL, l.getMaterial());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_TIME_LOADED, l.getTimeLoaded());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_UNIT_ID, l.getUnitId());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_DATE_LOADED, l.getDateLoaded());
        values.put(LoadContract.LoadEntry.COLUMN_NAME_COMPANY_NAME, l.getCompanyName());

        db.insert(LoadContract.LoadEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteLoadFromSession(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = LoadContract.LoadEntry._ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        Log.d("Delete: ", "ID: " + id);
        db.delete(LoadContract.LoadEntry.TABLE_NAME, selection, selectionArgs);
    }

    public List<String> getDistinctDatesForSession(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + LoadContract.LoadEntry.COLUMN_NAME_DATE_LOADED + " FROM " + LoadContract.LoadEntry.TABLE_NAME + " WHERE " + LoadContract.LoadEntry.COLUMN_NAME_JOB_TITLE + "=\'" + title + "\'", null);
        List<String> dates = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            do
                dates.add(cursor.getString(0));
            while(cursor.moveToNext());
        }
        return dates;
    }

    public List<String> getDistinctTimesForSession(String title, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT " + LoadContract.LoadEntry.COLUMN_NAME_TIME_LOADED + " FROM " + LoadContract.LoadEntry.TABLE_NAME + " WHERE " + LoadContract.LoadEntry.COLUMN_NAME_JOB_TITLE + "=\'" + title + "\'," + LoadContract.LoadEntry.COLUMN_NAME_DATE_LOADED + "=\'" + date, null);
        List<String> dates = new ArrayList<String>();
        if(cursor.moveToFirst()) {
            dates.add(cursor.getString(0));
        }
        return dates;
    }
}
