package com.example.smar;

public class ProjectList {


    String projectName;
    String day;
    String date;

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

    public ProjectList(String projectName, String day, String date) {
        this.projectName = projectName;
        this.day = day;
        this.date = date;



    }
}
