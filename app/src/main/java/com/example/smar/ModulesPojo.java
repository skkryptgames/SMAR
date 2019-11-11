package com.example.smar;

public class ModulesPojo {

    String image;
    int numOfDays;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    String name,startDate;
    private boolean isSelected=true;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModulesPojo(String image, String name,String startDate,int numOfDays) {
        this.image = image;
        this.name = name;
        this.startDate=startDate;
        this.numOfDays=numOfDays;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
