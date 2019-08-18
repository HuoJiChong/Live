package com.derek.live.impl;

import android.app.Activity;
import android.content.Context;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;
import com.derek.live.JniPush.Pusher;

public class LiveController {

    AudioController audioController;
    VideoController videoController;

    Context context;
    Pusher pusher;
    public LiveController(Activity ac, SurfaceHolder surfaceHolder) {
        this.context = ac.getApplicationContext();
        pusher = new Pusher();

        audioController = new AudioController(ac,pusher);
        videoController = new VideoController(ac,surfaceHolder,pusher);
    }

    public void switchCamera(){
        videoController.switchCamera();
    }


    public void onStart(String url) {
        audioController.onStart();
        videoController.onStart();
        pusher.startPush(url);
    }

    public void onRelease() {
        audioController.onRelease();
        videoController.onRelease();
    }


    public void onPause() {
        audioController.onPause();
        videoController.onPause();
    }

}
