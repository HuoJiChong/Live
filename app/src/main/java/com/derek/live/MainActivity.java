package com.derek.live;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.derek.live.impl.LiveController;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    LiveController liveController;
    SurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPer();

        surfaceView = findViewById(R.id.videoView);
        SurfaceHolder holder = surfaceView.getHolder();

        liveController = new LiveController(getApplicationContext(),holder);
    }

    private void requestPer() {
        String [] perms = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED || checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(perms,200);
            }
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public  void  recordStart(View v){
        liveController.onStart();
    }

    public  void  recordStop(View v){
        liveController.onStop();
    }

    private class SurfaceLayoutChangeListener implements View.OnLayoutChangeListener{

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        }
    }


}
