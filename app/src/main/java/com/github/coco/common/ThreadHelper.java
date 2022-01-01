package com.github.coco.common;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2022/1/1.
 *
 * @author wy
 */
public final class ThreadHelper {
    private final ThreadPoolExecutor executor;
    private static volatile ThreadHelper INSTANCE = null;

    private ThreadHelper() {
        executor = new ThreadPoolExecutor(5, 15, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50));
    }

    public static ThreadHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ThreadHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ThreadHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }
}
