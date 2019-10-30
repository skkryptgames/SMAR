package com.example.smar;

public class Client {


    String date;
    int tick;
    String title;
    int Images;
    boolean isSelected=false;

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
    public int getImages() {
        return Images;
    }

    public Client(String date, int tick, String title, int images) {
        this.date = date;
        this.tick = tick;
        this.title = title;
        this.Images = images;
    }
}
