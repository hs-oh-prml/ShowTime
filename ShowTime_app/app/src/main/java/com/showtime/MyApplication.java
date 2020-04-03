package com.showtime;

import android.app.Application;

public class MyApplication extends Application {
    private int width = 0;
    private int height = 0;


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int h){
        height = h;
    }

    public void setWidth(int w){
        width = w;
    }

}
