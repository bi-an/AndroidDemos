package com.example.backgroundthreaddemo;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

public class HandlerExecutor implements Executor {
    private Handler mHandler;

    public HandlerExecutor(Handler handler) {
        if (handler == null) {
            throw new RuntimeException("HandlerExecutor cannot constructs with parameter null");
        }
        mHandler = handler;
    }

    @Override
    public void execute(Runnable command) {
        if(!mHandler.post(command)){
            throw new RejectedExecutionException(mHandler + "is shutting down");
        }
    }
}
