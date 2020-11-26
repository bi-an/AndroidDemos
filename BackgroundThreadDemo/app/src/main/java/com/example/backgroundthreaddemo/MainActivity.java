package com.example.backgroundthreaddemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// Implements View.OnClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int MSG_HELLO = 1;

    private Handler mHandler;
    private BackgroundThread mBackgroundThread;

    private Button mBnTest;
    private TextView mTvTest;

    private class WorkerHandler extends Handler {
        WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // @warning 不可以在非UI线程更新UI，因为线程不安全
            // mTvTest.setText("\nTest"); // Error
            Log.d(TAG, "zzg: BackgroundThread handleMessage");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBnTest = findViewById(R.id.bn_test);
        mTvTest = findViewById(R.id.tv_test);

        // set Button's OnClickListener
        mBnTest.setOnClickListener(this);
        mHandler = new WorkerHandler(BackgroundThread.get().getLooper());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bn_test:
                mHandler.sendMessage(new Message());
                break;
            default:
                break;
        }
    }
}