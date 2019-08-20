package com.derek.live;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.shapes.PathShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.derek.live.JniPush.Pusher;
import com.derek.live.impl.LiveController;
import com.derek.live.listener.LiveStateChangeListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.

    private final static String URL = "rtmp://39.100.153.163/live360p/test";

    public static final String TAG = MainActivity.class.getName();

    LiveController liveController;
    SurfaceView surfaceView;

    static class MyHandler extends Handler {
        WeakReference<MainActivity> mActivity;

        MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity theActivity = mActivity.get();
            switch (msg.what) {
                case Pusher.CONNECT_FAILED:
                    theActivity.Toast("连接失败");
                    break;
                case Pusher.INIT_FAILED:
                    theActivity.Toast("初始化失败");
                    break;
                case Pusher.INIT_SUCCESS:
                    theActivity.Toast("初始化成功");
                    break;
                case Pusher.CONNECT_SUCCESS:
                    theActivity.Toast("连接成功");
                    break;
                case Pusher.START_PUSH:
                    theActivity.Toast("开始推送");
                    break;
            }
        }
    }

    MyHandler handler = new MyHandler(this);

    private void Toast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    LiveStatusListener liveStatusListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();

        surfaceView = findViewById(R.id.videoView);
        SurfaceHolder holder = surfaceView.getHolder();

        liveStatusListener = new LiveStatusListener();
        liveController = new LiveController(this,holder);
    }

    private void requestPer() {
        String [] perms = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[2]) == PackageManager.PERMISSION_DENIED || checkSelfPermission(perms[3]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,200);
            }
        }
    }

    public  void  recordStart(View v){
        Button btn = (Button) v;
        Log.e(TAG,"" + btn.getText().toString().trim());
        if(btn.getText().toString().equals( "START")){
            liveController.onStart(URL,liveStatusListener);
            btn.setText("STOP");
        }else{
            btn.setText("START");
            liveController.onPause();
        }
    }

    public void switchCamera(View v){
        liveController.switchCamera();
    }

    @Override
    protected void onDestroy() {
        liveController.onPause();
        liveController.onRelease();

        super.onDestroy();
    }

    class LiveStatusListener implements LiveStateChangeListener{

        @Override
        public void onError(int code) {
            handler.sendEmptyMessage(code);
        }
    }
}
