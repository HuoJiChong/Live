package com.derek.live.Interface;

import com.derek.live.JniPush.Pusher;

public abstract class Controller {

    protected Pusher nativePusher;
    protected boolean isRecording;

    public Controller(Pusher pusher){
        nativePusher = pusher;
    }

    public abstract void onStart();
    public abstract void onPause();
    public abstract void onRelease();
}
