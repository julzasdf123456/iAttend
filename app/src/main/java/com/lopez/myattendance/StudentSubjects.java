package com.lopez.myattendance;

public class StudentSubjects {

    private String subject, description, schoolyear;

    public StudentSubjects() {}

    public StudentSubjects(String subject, String description, String schoolyear) {
        this.subject = subject;
        this.description = description;
        this.schoolyear = schoolyear;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchoolyear() {
        return schoolyear;
    }

    public void setSchoolyear(String schoolyear) {
        this.schoolyear = schoolyear;
    }
}
