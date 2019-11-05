package com.example.smar;

public class ProjectList {


    String projectName;
    String day;
    String date;
    String pId;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public ProjectList(){


    }

    public String getProjectName() {
        return projectName;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public ProjectList(String projectName, String day, String date,String pId) {
        this.projectName = projectName;
        this.day = day;
        this.date = date;
        this.pId=pId;



    }
}
