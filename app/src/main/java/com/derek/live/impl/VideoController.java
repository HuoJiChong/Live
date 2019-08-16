package com.derek.live.impl;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;
import com.derek.live.config.GlobalConfig;

import java.io.IOException;

public class VideoController extends Controller implements SurfaceHolder.Callback, Camera.PreviewCallback {
    public static final String TAG = VideoController.class.getName();

    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private byte[] buffers;
    Context context;
    private boolean surfaceCreated;

    public VideoController(Context context, SurfaceHolder surfaceHolder) {
        this.context = context.getApplicationContext();
        this.surfaceHolder = surfaceHolder;
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        isRecording = true;
        if (surfaceCreated){
            isRecording = true;
            startPreview();
        }else{
            try {
                throw new Exception(" surfaceDestroyed cann't start ... ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRelease() {
        isRecording = false;
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPause() {
        isRecording = false;
        mCamera.stopPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceCreated = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (isRecording){
            // 获取每一帧的画面数据
            if (mCamera != null) {
                mCamera.addCallbackBuffer(buffers);
            }

        }

        Log.e(TAG," onPreviewFrame ");


        //回调函数中获取图像数据，然后给Native代码编码
//            pushNative.fireVideo(data);

    }

    private void startPreview() {
        try {
            //SurfaceView初始化完成，开始相机预览
            mCamera = Camera.open(GlobalConfig.Camera_ID);
            mCamera.setPreviewDisplay(surfaceHolder);
            //获取预览图像数据
            buffers = new byte[GlobalConfig.Video_Width * GlobalConfig.Video_Height * 4];
            mCamera.addCallbackBuffer(buffers);
            mCamera.setPreviewCallbackWithBuffer(this);

            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview(){
        if (mCamera !=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void  switchCamera(){
        int cameraIdTemp ;
        if (GlobalConfig.Camera_ID == Camera.CameraInfo.CAMERA_FACING_BACK){
            cameraIdTemp = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else{
            cameraIdTemp = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        GlobalConfig.Camera_ID = cameraIdTemp;

        stopPreview();
        startPreview();
    }
}
