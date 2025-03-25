package com.group1;

public class StudentDetail {
    private String id;
    private String name;
    private String gender;
    private String email;
    private String major;
    private String status;
    private String courseid;

    public StudentDetail(String id, String name, String gender, String email,
            String major, String status, String courseid) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.major = major;
        this.status = status;
        this.courseid = courseid;
    }

    public StudentDetail(String id, String name, String gender, String email,
            String major, String status) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.major = major;
        this.status = status;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getMajor() {
        return major;
    }

    public String getStatus() {
        return status;
    }

    public String getCourseId() {
        return courseid;
    }

    // setter
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCourseId(String courseid) {
        this.courseid = courseid;
    }
}
