package com.example.smar;



import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class DBUser {

    public String uid;
    public String name;
    public String token;

    public DBUser() {
        // Default constructor required for calls to DataSnapshot.getValue(DBUser.class)

    }


    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public DBUser(String uid, String name, String token) {
        this.uid = uid;
        this.name = name;
        this.token = token;





    }

}
