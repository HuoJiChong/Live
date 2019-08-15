package com.derek.live.config;

import android.media.AudioFormat;
import android.media.MediaRecorder;

public class GlobalConfig {
        private static final int AUDIO_RESOURCE = MediaRecorder.AudioSource.MIC;
//    //设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置
    private final static int AUDIO_SAMPLE_RATE = 44100;
//    //设置声道声道数量为双声道
    private final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
//    //设置采样精度，将采样的数据以PCM进行编码，每次采集的数据位宽为16bit。
    private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
}
