package com.lopez.myattendance;

public class Subjects {

    private String subjectName, subjectDescription;

    public Subjects() {

    }

    public Subjects(String subjectName, String subjectDescription) {
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }
}
