package com.example.amapp.beans;

public class Student {
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String studentNumber;
    private String name;
    private boolean isPresent;
    private int photoId;

    // Constructor
    public Student(String studentNumber, String name, boolean isPresent, int photoId) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.isPresent = isPresent;
        this.photoId = photoId;
    }


    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }



}
