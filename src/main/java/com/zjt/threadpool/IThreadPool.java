package com.zjt.threadpool;

import java.util.concurrent.Future;

public interface IThreadPool {

    public Future submit(Runnable runnable);
    public void shutdown();
}