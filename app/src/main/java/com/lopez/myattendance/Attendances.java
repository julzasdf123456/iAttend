package com.lopez.myattendance;

public class Attendances {

    private String id, classId, studentId, date;
    public boolean isPresent;

    public Attendances() {}

    public Attendances(String id, String classId, String studentId, String date, boolean isPresent) {
        this.id = id;
        this.classId = classId;
        this.studentId = studentId;
        this.date = date;
        this.isPresent = isPresent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
