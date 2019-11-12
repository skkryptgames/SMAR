package com.example.smar;

public class Client {


    String date;
    int tick;
    String title;
    String Images;
    boolean isSelected=false;
    String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Client(){

    }

    public String getDate() {
        return date;
    }

    public int getTick() {
        return tick;
    }

    public String getTitle() {
        return title;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public String getImages() {
        return Images;
    }

    public Client(String date, int tick, String title, String images,String taskId) {
        this.date = date;
        this.tick = tick;
        this.title = title;
        this.Images = images;
        this.taskId=taskId;
    }
}
