package com.derek.live.impl;

import android.content.Context;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;
import com.derek.live.params.VideoParam;

public class LiveController extends Controller {

    AudioController audioController;
    VideoController videoController;

    Context context;
    public LiveController(Context context,SurfaceHolder surfaceHolder) {
        this.context = context.getApplicationContext();
        VideoParam videoParam = new VideoParam(480,320, android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);
        videoController = new VideoController(surfaceHolder,videoParam);
    }

    @Override
    public void onStart() {
        audioController.onStart();
        videoController.onStart();
    }

    @Override
    public void onStop() {
        audioController.onStop();
        videoController.onStop();
    }

    @Override
    public void onResume() {
        audioController.onResume();
        videoController.onResume();
    }

    @Override
    public void onPause() {
        audioController.onPause();
        videoController.onPause();
    }

}
