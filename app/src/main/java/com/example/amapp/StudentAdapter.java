package com.example.amapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.amapp.beans.Student;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class StudentAdapter extends ArrayAdapter<Student> {
    private ArrayList<Student> students;
    private LayoutInflater inflater;
    private Context context;

    public StudentAdapter(Context context, ArrayList<Student> students) {
        super(context, 0, students);
        this.context = context;
        this.students = students;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = inflater.inflate(R.layout.list_item_student, parent, false);
        }

        final Student currentStudent = students.get(position);

        // Get references to views in list item layout
        TextView studentNumberTextView = listItemView.findViewById(R.id.studentNumber);
        ImageView studentPhotoImageView = listItemView.findViewById(R.id.studentPhoto);
        TextView studentNameTextView = listItemView.findViewById(R.id.studentName);
        TextView presenceStatusTextView = listItemView.findViewById(R.id.presenceStatus);

        // Set student name and presence status
        studentNumberTextView.setText(currentStudent.getStudentNumber());
        studentNameTextView.setText(currentStudent.getName());
        presenceStatusTextView.setText(currentStudent.isPresent() ? "Present" : "Absent");
        int textColor = currentStudent.isPresent() ? Color.parseColor("#00FF00") : Color.parseColor("#FF0000");
        presenceStatusTextView.setTextColor(textColor);
        studentPhotoImageView.setImageResource(currentStudent.getPhotoId());

        // Set click listener for the list item view


        return listItemView;
    }
}
