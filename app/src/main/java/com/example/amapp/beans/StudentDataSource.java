package com.example.amapp.beans;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class StudentDataSource {
    private SQLiteDatabase database;
    private StudentDbHelper dbHelper;

    public StudentDataSource(Context context) {
        dbHelper = new StudentDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

   public void clearTable()
   {
       dbHelper.deleteAllStudents();
   }
    public void close() {
        dbHelper.close();
    }

    public long insertStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER, student.getStudentNumber());
        values.put(StudentContract.StudentEntry.COLUMN_NAME, student.getName());
        values.put(StudentContract.StudentEntry.COLUMN_IS_PRESENT, student.isPresent() ? 1 : 0);
        values.put(StudentContract.StudentEntry.COLUMN_PHOTO_ID, student.getPhotoId());

        return database.insert(StudentContract.StudentEntry.TABLE_NAME, null, values);
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();

        Cursor cursor = database.query(
                StudentContract.StudentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Student student = cursorToStudent(cursor);
            students.add(student);
            cursor.moveToNext();
        }
        cursor.close();

        return students;
    }

    private Student cursorToStudent(Cursor cursor) {
        @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(StudentContract.StudentEntry._ID));
        @SuppressLint("Range") String studentNumber = cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER));
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_NAME));
        @SuppressLint("Range") boolean isPresent = cursor.getInt(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_IS_PRESENT)) == 1;
        @SuppressLint("Range") int photoId = cursor.getInt(cursor.getColumnIndex(StudentContract.StudentEntry.COLUMN_PHOTO_ID));

        Student student = new Student(studentNumber, name, isPresent, photoId);
        student.setId(id);

        return student;
    }

    // Implement other CRUD operations as needed
}
