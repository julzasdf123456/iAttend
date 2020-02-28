package com.lopez.myattendance;

public class Blocks {

    private String block, courseId, uid;

    public Blocks() {}

    public Blocks(String block, String courseId) {
        this.block = block;
        this.courseId = courseId;
    }

    public Blocks(String block, String courseId, String uid) {
        this.block = block;
        this.courseId = courseId;
        this.uid = uid;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
