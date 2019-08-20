package com.derek.live.impl;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.derek.live.Interface.Controller;
import com.derek.live.JniPush.Pusher;
import com.derek.live.config.GlobalConfig;

import java.io.IOException;
import java.util.List;

public class VideoController extends Controller implements SurfaceHolder.Callback, Camera.PreviewCallback {
    public static final String TAG = VideoController.class.getName();

    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private byte[] buffers;
    Activity ac;
    private boolean surfaceCreated;

    public VideoController(Activity ac, SurfaceHolder surfaceHolder, Pusher pusher) {
        super(pusher);
        this.ac = ac;
        this.surfaceHolder = surfaceHolder;
        this.surfaceHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        isRecording = true;
        if (surfaceCreated){
            isRecording = true;
            nativePusher.setVideoOptions(GlobalConfig.Video_Width,GlobalConfig.Video_Height,GlobalConfig.VIDEO_BITRATE,GlobalConfig.VIDEO_FPS);
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
        if (mCamera != null){
            mCamera.stopPreview();
        }

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
            //回调函数中获取图像数据，然后给Native代码编码
            nativePusher.fireVideo(data);

        }
    }

    /**
     * 开始预览界面
     */
    private void startPreview() {
        try {
            //SurfaceView初始化完成，开始相机预览
            mCamera = Camera.open(GlobalConfig.Camera_ID);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPictureFormat(ImageFormat.NV21);
            parameters.setPreviewSize(GlobalConfig.Video_Width,GlobalConfig.Video_Height);
            mCamera.setParameters(parameters);

//            设置相机的方向
            Camera.CameraInfo info = new Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(GlobalConfig.Camera_ID, info);
            int rotation = ac.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }
            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (info.orientation - degrees + 360) % 360;
            }
            mCamera.setDisplayOrientation(result);

            mCamera.setPreviewDisplay(surfaceHolder);
//            parameters.setPreviewFpsRange(GlobalConfig.VIDEO_FPS - 1,GlobalConfig.VIDEO_FPS);

            //获取预览图像数据
            buffers = new byte[GlobalConfig.Video_Width * GlobalConfig.Video_Height * 4];
            mCamera.addCallbackBuffer(buffers);
            mCamera.setPreviewCallbackWithBuffer(this);

            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止预览界面
     */
    private void stopPreview(){
        if (mCamera !=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 切换摄像机
     */
    public void  switchCamera(){
        int cameraIdTemp ;
        if (GlobalConfig.Camera_ID == Camera.CameraInfo.CAMERA_FACING_BACK){
            cameraIdTemp = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else{
            cameraIdTemp = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        GlobalConfig.Camera_ID = cameraIdTemp;

        stopPreview();
        startPreview();
    }
}
