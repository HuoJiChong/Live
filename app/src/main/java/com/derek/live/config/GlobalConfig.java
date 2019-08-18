package com.derek.live.config;

import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.MediaRecorder;

public class GlobalConfig {
    public static final int AUDIO_RESOURCE = MediaRecorder.AudioSource.MIC;
//    //设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置
    public final static int AUDIO_SAMPLE_RATE = 44100;
//    //设置声道声道数量为双声道
    public final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
//    //设置采样精度，将采样的数据以PCM进行编码，每次采集的数据位宽为16bit。
    public final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;


    public final static int Video_Width = 720;
    public final static int Video_Height = 1280;
    //码率
    public final static int VIDEO_BITRATE = 480000;// 480k bps
    //帧频
    public final static int VIDEO_FPS = 25;
    public static int Camera_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
}
