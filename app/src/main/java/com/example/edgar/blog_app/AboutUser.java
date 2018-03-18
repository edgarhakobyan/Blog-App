package com.example.edgar.blog_app;

/**
 * Created by edgar on 3/18/18.
 */

public class AboutUser {
    private String workPlace;
    private String school;
    private String university;
    private String pnoneNumber;
    private String email;


    public AboutUser(String workPlace, String school, String university, String pnoneNumber, String email) {
        this.workPlace = workPlace;
        this.school = school;
        this.university = university;
        this.pnoneNumber = pnoneNumber;
        this.email = email;
    }


    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void setPnoneNumber(String pnoneNumber) {
        this.pnoneNumber = pnoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public String getSchool() {
        return school;
    }

    public String getUniversity() {
        return university;
    }

    public String getPnoneNumber() {
        return pnoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
