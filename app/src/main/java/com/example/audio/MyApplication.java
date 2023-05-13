package com.example.audio;

import android.app.Application;

import java.io.File;

public class MyApplication extends Application {
    private String userName = "defaultUser";
    private File curDir = null;

    public String getUserName() {return userName;}
    public void setUserName(String un) {
        this.userName = un;
    }

    public File getCurDir(){return curDir;}
    public void setCurDir(File dir){curDir=dir;}
}
