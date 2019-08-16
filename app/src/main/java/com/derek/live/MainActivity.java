package com.derek.live;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.derek.live.impl.LiveController;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.


    public static final String TAG = MainActivity.class.getName();

    LiveController liveController;
    SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();

        surfaceView = findViewById(R.id.videoView);
        SurfaceHolder holder = surfaceView.getHolder();

        liveController = new LiveController(this,holder);
    }

    private void requestPer() {
        String [] perms = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,200);
            }
        }
    }



    public  void  recordStart(View v){
        Button btn = (Button) v;
        Log.e(TAG,"" + btn.getText().toString().trim());
        if(btn.getText().toString().equals( "START")){
            liveController.onStart();
            btn.setText("STOP");
        }else{
            btn.setText("START");
            liveController.onPause();
        }
    }

    public void switchCamera(View v){
        liveController.switchCamera();
    }

}
