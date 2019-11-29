package com.example.smar;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class AddPhotos {
    public String key;
    public String userId;
    public String downloadUrl;
    boolean selected;



    // these properties will not be saved to the database
    @Exclude
    public DBUser dbUser;


    public AddPhotos(){

    }


    public String getKey() {
        return key;
    }

    public String getUserId() {
        return userId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public DBUser getDbUser() {
        return dbUser;
    }

    public AddPhotos(String key, String userId, String downloadUrl) {
        this.key = key;
        this.userId = userId;
        this.downloadUrl = downloadUrl;



    }


    public boolean isSelected() {

        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
