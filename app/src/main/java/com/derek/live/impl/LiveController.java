package com.derek.live.impl;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;

import com.derek.live.JniPush.Pusher;
import com.derek.live.listener.LiveStateChangeListener;

public class LiveController {

    private AudioController audioController;
    private VideoController videoController;

    private Context context;
    private Pusher pusher;
    public LiveController(Activity ac, SurfaceHolder surfaceHolder) {
        this.context = ac.getApplicationContext();
        pusher = new Pusher();

        audioController = new AudioController(ac,pusher);
        videoController = new VideoController(ac,surfaceHolder,pusher);
    }

    public void switchCamera(){
        videoController.switchCamera();
    }

    public void onStart(String url, LiveStateChangeListener liveStateChangeListener) {
        audioController.onStart();
        videoController.onStart();
        pusher.startPush(url);
        pusher.setLiveStateChangeListener(liveStateChangeListener);
    }

    public void onRelease() {
        audioController.onRelease();
        videoController.onRelease();
        pusher.release();
    }

    public void onPause() {
        audioController.onPause();
        videoController.onPause();
        pusher.stopPush();
    }

}
