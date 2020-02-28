package com.lopez.myattendance;

public class Courses {

    private String course, description, year;

    public Courses() {}

    public Courses(String course, String description, String year) {
        this.course = course;
        this.description = description;
        this.year = year;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
