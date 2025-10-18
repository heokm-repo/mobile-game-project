package com.example.termproject_game;

import android.app.Application;
import android.media.MediaPlayer;

public class BackgroundMusic extends Application {
    private static BackgroundMusic instance;
    public static MediaPlayer mainBGM;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BackgroundMusic getInstance() {
        return instance;
    }

    public MediaPlayer getMainBGM() {
        return mainBGM;
    }

    public void setMainBGM(MediaPlayer mainBGM) {
        this.mainBGM = mainBGM;
    }
}

