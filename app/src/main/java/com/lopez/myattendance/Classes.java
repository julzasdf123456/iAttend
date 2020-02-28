package com.lopez.myattendance;

public class Classes {

    private String id, subjectId, year, schedule, teacherEmail;

    public Classes() {}

    public Classes(String id, String subjectId, String year, String schedule, String teacherEmail) {
        this.id = id;
        this.subjectId = subjectId;
        this.year = year;
        this.schedule = schedule;
        this.teacherEmail = teacherEmail;
    }

    public Classes(String subjectId, String year, String schedule, String teacherEmail) {
        this.subjectId = subjectId;
        this.year = year;
        this.schedule = schedule;
        this.teacherEmail = teacherEmail;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
