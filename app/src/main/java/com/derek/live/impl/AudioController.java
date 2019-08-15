package com.derek.live.impl;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.derek.live.Interface.Controller;

public class AudioController extends Controller {

    private static final String TAG = AudioRecordUtil.class.getName();
    private AudioRecord audioRecord;
    private int audioResource;
    private int audioSampleRate;
    private int channelConfig;
    private int audioFormat;
    private int bufferSizeInBytes;


    public Context context;

    public AudioController(Context context) {
        audioResource = MediaRecorder.AudioSource.MIC;
        audioSampleRate = 44100;
        channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(audioSampleRate,channelConfig,audioFormat);
        audioRecord = new AudioRecord(audioResource, audioSampleRate, channelConfig, audioFormat, bufferSizeInBytes);
        this.context = context;
    }

    @Override
    public void onStart() {
        if (isRecording){
            Toast.makeText(this.context,"audio recording ....",Toast.LENGTH_SHORT).show();
            return;
        }
        //创建一个流，存放从AudioRecord读取的数据
        new Thread(recordTask).start();
    }


    @Override
    public void onStop() {
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
        audioRecord = null;
    }

    @Override
    public void onResume() {
        isRecording = true;
    }

    @Override
    public void onPause() {
        isRecording = false;
    }

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
                final int read = audioRecord.read(data, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != read && AudioRecord.ERROR != read){
                    //将数据写入到文件中  传递给Native,

                }
            }
        }
    };
}
