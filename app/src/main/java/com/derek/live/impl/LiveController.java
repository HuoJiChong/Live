package com.derek.live.impl;

import android.content.Context;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;

public class LiveController extends Controller {

    AudioController audioController;
    VideoController videoController;

    Context context;
    public LiveController(Context context,SurfaceHolder surfaceHolder) {
        this.context = context.getApplicationContext();
        audioController = new AudioController(context);
        videoController = new VideoController(context,surfaceHolder);
    }

    public void switchCamera(){
        videoController.switchCamera();
    }

    @Override
    public void onStart() {
        audioController.onStart();
        videoController.onStart();
    }

    @Override
    public void onRelease() {
        audioController.onRelease();
        videoController.onRelease();
    }

    @Override
    public void onPause() {
        audioController.onPause();
        videoController.onPause();
    }

}
