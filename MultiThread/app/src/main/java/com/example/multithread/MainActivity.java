package com.example.multithread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private static final int UPDATE_TEXT = 1;
    private static final int UPDATE_FLASH = 2;
//    private static

    private TextView mText;
    private Button mButton;
    private Handler mHandler;
    private Handler mCameraHandler;
    private CameraManager mCameraManager;

    private CameraManager.TorchCallback mTorchCallback = new CameraManager.TorchCallback() {
        @Override
        public void onTorchModeUnavailable(@NonNull String cameraId) {
            super.onTorchModeUnavailable(cameraId);
        }

        @Override
        public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
            super.onTorchModeChanged(cameraId, enabled);
            Message msg = new Message();
            msg.what = UPDATE_FLASH; // TODO
            msg.obj = enabled;
            mHandler.sendMessage(msg);
        }
    };
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//
//        }
//    };

    private class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT:
                    mText.setText("Nice to meet you");
                    break;
                case UPDATE_FLASH:
                    String str = "闪光灯";
                    if ((boolean) msg.obj == true)
                        str += "打开";
                    else
                        str += "关闭";
                    mText.setText(str);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "未知消息", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private class TestHandlerThread extends Thread {
        TestHandlerThread() {
            super("TestHandlerThread");
        }

        @Override
        public void run() {
            Looper.prepare();
            Looper looperTest = Looper.myLooper();
            mHandler = new WorkerHandler(looperTest);
            Looper.loop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new WorkerHandler(Looper.myLooper());
        HandlerThread thread = new HandlerThread(TAG, Thread.NORM_PRIORITY);
        thread.start();
        mCameraHandler = new Handler(thread.getLooper());
        try {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            mCameraManager.registerTorchCallback(mTorchCallback, null);
        } catch (Exception e) {
            Log.w(TAG, "cannot get camera manager");
        }

        mText = findViewById(R.id.tv_change_text);
        mButton = findViewById(R.id.bn_change_text);
        mButton.setOnClickListener(this);
//        TestHandlerThread testHandlerThread = new TestHandlerThread();
//        Message msg = Message.obtain();
//        msg.arg1 = 1;
//        msg.what = UPDATE_TEXT;
//        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = UPDATE_TEXT;
                        mHandler.sendMessage(msg);
                    }
                }).start();
                break;
        }
    }
}