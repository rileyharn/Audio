package com.example.audio;

import android.app.Application;

public class MyApplication extends Application {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String un) {
        this.userName = un;
    }
}
