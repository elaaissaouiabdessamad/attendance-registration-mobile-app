package com.example.amapp.beans;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.amapp.beans.StudentContract.StudentEntry.TABLE_NAME;

public class StudentDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statements to create and upgrade the database
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    StudentContract.StudentEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER + " TEXT," +
                    StudentContract.StudentEntry.COLUMN_NAME + " TEXT," +
                    StudentContract.StudentEntry.COLUMN_IS_PRESENT + " INTEGER," +
                    StudentContract.StudentEntry.COLUMN_PHOTO_ID + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache of student data, so its upgrade policy is
        // to simply discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void deleteAllStudents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("students", null, null);
    }
}
