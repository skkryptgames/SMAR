package com.example.smar;

public class ProjectList {


    String projectName;
    String work;
    String date;
    String pId;
    int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

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

    public void setWork(String work) {
        this.work = work;
    }

    public String getWork() {
        return work;
    }

    public String getDate() {
        return date;
    }

    public ProjectList(String projectName, String work, String date,String pId,int progress) {
        this.projectName = projectName;
        this.work = work;
        this.date = date;
        this.pId=pId;
        this.progress=progress;



    }
}
