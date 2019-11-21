package com.example.smar;

public class ProjectList {


    String projectName;
    String work;
    String date;
    String pId;
    int progress;
    String uId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public ProjectList(String projectName, String work, String date, String pId, int progress, String uId) {
        this.projectName = projectName;
        this.work = work;
        this.date = date;
        this.pId = pId;
        this.progress = progress;
        this.uId=uId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getWork() {
        return work;
    }

    public String getDate() {
        return date;
    }

    public String getpId() {
        return pId;
    }

    public int getProgress() {
        return progress;
    }
}
