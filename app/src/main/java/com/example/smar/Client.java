package com.example.smar;

public class Client {


    String date;
    int tick;
    String title;
    String Images;
    boolean isSelected=false;
    String taskId;


    public Client(String date, int tick, String title, String images, String taskId) {
        this.date = date;
        this.tick = tick;
        this.title = title;
        Images = images;
        this.taskId = taskId;
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

    public String getImages() {
        return Images;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getTaskId() {
        return taskId;
    }
}
