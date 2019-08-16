package com.derek.live.Interface;

public abstract class Controller {
    protected boolean isRecording;
    public abstract void onStart();
    public abstract void onPause();
    public abstract void onRelease();
}
