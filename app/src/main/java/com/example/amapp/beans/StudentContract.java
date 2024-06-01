package com.example.amapp.beans;

import android.provider.BaseColumns;

public final class StudentContract {

    private StudentContract() {}

    public static class StudentEntry implements BaseColumns {
        public static final String TABLE_NAME = "students";
        public static final String COLUMN_STUDENT_NUMBER = "student_number";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IS_PRESENT = "is_present";
        public static final String COLUMN_PHOTO_ID = "photo_id";
    }
}
