package com.zjt.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultThreadPool implements IThreadPool{
    private static IThreadPool instance = null;
    private ExecutorService mExecutor;

    private DefaultThreadPool(){
        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors(),KEEP_ALIVE_TIME = 1;
        TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
        mExecutor = new ThreadPoolExecutor(NUMBER_OF_CORES,NUMBER_OF_CORES * 2,KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue);
    }

    public static IThreadPool getInstance(){
        if(instance == null){
            instance = new DefaultThreadPool();
        }
        return instance;
    }

    public Future submit(Runnable runnable){
        return mExecutor.submit(runnable);
    }

    public void shutdown(){
        mExecutor.shutdown();
    }
}