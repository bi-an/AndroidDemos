package com.example.backgroundthreaddemo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.Executor;

public class BackgroundThread extends HandlerThread {
    private static final long SLOW_DISPATCH_THRESHOLD_MS = 10_000;
    private static final long SLOW_DELIVERY_THRESHOLD_MS = 30_000;
    private static BackgroundThread sInstance;
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;

    private BackgroundThread() {
        super("android.bg", android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new BackgroundThread();
            sInstance.start();
            final Looper looper = sInstance.getLooper();
//            looper.setTraceTag(Trace.TRACE_TAG_SYSTEM_SERVER);
//            looper.setSlowLogThresholdMs(
//                SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);
            sHandler = new Handler(sInstance.getLooper());
            sHandlerExecutor = new HandlerExecutor(sHandler);
        }
    }

    public static BackgroundThread get() {
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    public static Handler getHandler() {
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sHandler;
        }
    }

    public static Executor getExecutor() {
        synchronized (BackgroundThread.class) {
            ensureThreadLocked();
            return sHandlerExecutor;
        }
    }
}
