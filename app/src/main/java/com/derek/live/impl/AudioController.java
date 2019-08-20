package com.derek.live.impl;

import android.app.Activity;
import android.content.Context;
import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;

import com.derek.live.Interface.Controller;
import com.derek.live.JniPush.Pusher;
import com.derek.live.config.GlobalConfig;

public class AudioController extends Controller {

    private static final String TAG = AudioController.class.getName();
    /**
     * 音频采样参数
     */
    private AudioRecord audioRecord;
    private int audioResource;
    private int audioSampleRate;
    private int channelConfig;
    private int audioFormat;
    private int bufferSizeInBytes;

    public Context context;

    public AudioController(Activity ac, Pusher pusher) {
        super(pusher);
        audioResource = GlobalConfig.AUDIO_RESOURCE;
        audioSampleRate = GlobalConfig.AUDIO_SAMPLE_RATE;
        channelConfig = GlobalConfig.CHANNEL_CONFIG;
        audioFormat = GlobalConfig.AUDIO_FORMAT;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(audioSampleRate,channelConfig,audioFormat);
        audioRecord = new AudioRecord(audioResource, audioSampleRate, channelConfig, audioFormat, bufferSizeInBytes);
        this.context = ac.getApplicationContext();
    }

    @Override
    public void onStart() {
        if (isRecording){
            return;
        }
        nativePusher.setAudioOptions(GlobalConfig.AUDIO_SAMPLE_RATE,GlobalConfig.CHANNEL_CONFIG);
        //创建一个流，存放从AudioRecord读取的数据
        new Thread(recordTask).start();
    }

    /**
     * 释放资源
     */
    @Override
    public void onRelease() {
        isRecording = false;
        if (audioRecord !=null ){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    /**
     * 暂停播放
     */
    @Override
    public void onPause() {
        isRecording = false;
        audioRecord.stop();
    }

    /**
     * 开启子线程采集音频
     */
    private Runnable recordTask = new Runnable() {
        @Override
        public void run() {
            audioRecord.startRecording();

            //设置线程的优先级
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            Log.i(TAG, "设置采集音频线程优先级");
            byte[] data = new byte[bufferSizeInBytes];
            //标记为开始采集状态
            isRecording = true;
            Log.i(TAG, "设置当前当前状态为采集状态");
            //getRecordingState获取当前AudioReroding是否正在采集数据的状态
            Log.e(TAG, audioRecord.getRecordingState()+"");
            while (isRecording && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                //读取采集数据到缓冲区中，read就是读取到的数据量
                int read = audioRecord.read(data, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != read && AudioRecord.ERROR != read){
                    //将数据写入到文件中  传递给Native,

                    nativePusher.fireAudio(data,read);
                }
            }
        }
    };
}
