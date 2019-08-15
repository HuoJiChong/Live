package com.derek.live.impl;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioRecordUtil {
    private static final String TAG = AudioRecordUtil.class.getName();
    private AudioRecord audioRecord;
    private int audioResource;
    private int audioSampleRate;
    private int channelConfig;
    private int audioFormat;
    private int bufferSizeInBytes;
    private boolean isRecording;
    private DataOutputStream dataOutputStream;

    File saveFile;

    public Context context;

    public AudioRecordUtil(Context context) {
        audioResource = MediaRecorder.AudioSource.MIC;
        audioSampleRate = 44100;
        channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(audioSampleRate,channelConfig,audioFormat);
        audioRecord = new AudioRecord(audioResource, audioSampleRate, channelConfig, audioFormat, bufferSizeInBytes);
        this.context = context;
    }

    public void startRecord(){
        if (isRecording){
            Toast.makeText(this.context,"recording ....",Toast.LENGTH_SHORT).show();
            return;
        }
        audioRecord.startRecording();

        //创建一个流，存放从AudioRecord读取的数据
        saveFile = new File(Environment.getExternalStorageDirectory(), "audio-record.pcm");
        try {
            dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(saveFile)));

            new Thread(recordTask).start();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Runnable recordTask = new Runnable() {
        @Override
        public void run() {
            //设置线程的优先级
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            Log.i(TAG, "设置采集音频线程优先级");
            final byte[] data = new byte[bufferSizeInBytes];
            //标记为开始采集状态
            isRecording = true;
            Log.i(TAG, "设置当前当前状态为采集状态");
            //getRecordingState获取当前AudioReroding是否正在采集数据的状态
            Log.e(TAG, audioRecord.getRecordingState()+"");
            while (isRecording && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                //读取采集数据到缓冲区中，read就是读取到的数据量
                final int read = audioRecord.read(data, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != read && AudioRecord.ERROR != read){
                    //将数据写入到文件中
                    try {
                        dataOutputStream.write(data, 0, read);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public void stopRecord() {
        isRecording = false;
        audioRecord.stop();

        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
