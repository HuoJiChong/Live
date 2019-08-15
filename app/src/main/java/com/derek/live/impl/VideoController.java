package com.derek.live.impl;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;
import com.derek.live.params.VideoParam;

import java.io.IOException;

public class VideoController extends Controller implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private byte[] buffers;
    private VideoParam videoParams;

    public VideoController(SurfaceHolder surfaceHolder, VideoParam videoParams) {
        this.videoParams = videoParams;
        this.surfaceHolder = surfaceHolder;
        surfaceHolder.addCallback(this);

    }

    @Override
    public void onStart() {
        isRecording = true;
    }

    private void startPreview() {
        try {
            //SurfaceView初始化完成，开始相机预览
            mCamera = Camera.open(videoParams.getCameraId());
            mCamera.setPreviewDisplay(surfaceHolder);
            //获取预览图像数据
            buffers = new byte[videoParams.getWidth() * videoParams.getHeight() * 4];
            mCamera.addCallbackBuffer(buffers);
            mCamera.setPreviewCallbackWithBuffer(this);

            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        isRecording = false;
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onResume() {
        isRecording = true;
    }

    @Override
    public void onPause() {
        isRecording = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isRecording){
            if (mCamera != null) {
                mCamera.addCallbackBuffer(buffers);
            }


        }


        //回调函数中获取图像数据，然后给Native代码编码
//            pushNative.fireVideo(data);


    }
}
