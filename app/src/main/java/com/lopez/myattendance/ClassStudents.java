package com.lopez.myattendance;

public class ClassStudents {
    private String id, classId, studentId, studentDisplayName;
    public boolean isChecked;

    public ClassStudents() {}

    public ClassStudents(String id, String classId, String studentId, String studentDisplayName, boolean isChecked) {
        this.id = id;
        this.classId = classId;
        this.studentId = studentId;
        this.studentDisplayName = studentDisplayName;
        this.isChecked = isChecked;
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

    public String getStudentDisplayName() {
        return studentDisplayName;
    }

    public void setStudentDisplayName(String studentDisplayName) {
        this.studentDisplayName = studentDisplayName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
