package com.example.smar;

public class ModulesPojo {

    int image;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(String numOfDays) {
        this.numOfDays = numOfDays;
    }

    String name,startDate,numOfDays;
    private boolean isSelected=false;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModulesPojo(int image, String name,String startDate,String numOfDays) {
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
