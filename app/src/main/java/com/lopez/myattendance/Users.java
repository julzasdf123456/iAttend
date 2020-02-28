package com.lopez.myattendance;

public class Users {

    private String fullName, username, password, role, address, contactNumber; // role = PARENT, TEACHER

    public Users() {}

    public Users(String fullName, String username, String password, String role, String address, String contactNumber) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
