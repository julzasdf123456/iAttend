package com.lopez.myattendance;

public class Students {

    private String firstname, middlename, lastname, address, parentContact, blockId, id;
    public boolean isChecked;

    public Students() {}

    public Students(String firstname, String middlename, String lastname, String address, String parentContact, String blockId) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.address = address;
        this.parentContact = parentContact;
        this.blockId = blockId;
    }

    public Students(String firstname, String middlename, String lastname, String address, String parentContact, String blockId, String id) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.address = address;
        this.parentContact = parentContact;
        this.blockId = blockId;
        this.id = id;
    }

    public Students(String firstname, String middlename, String lastname, String address, String parentContact, String blockId, String id, boolean isChecked) {
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.address = address;
        this.parentContact = parentContact;
        this.blockId = blockId;
        this.id = id;
        this.isChecked = isChecked;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentContact() {
        return parentContact;
    }

    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
