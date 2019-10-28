package com.example.smar;

public class ModulesPojo {

    int image;
    String name;
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

    public ModulesPojo(int image, String name) {
        this.image = image;
        this.name = name;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
