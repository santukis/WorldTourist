package com.david.worldtourist.common.domain;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseCaseThreadPoolScheduler implements UseCaseScheduler{

    private final Handler handler = new Handler();

    private static final int POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int TIMEOUT = 30;

    private ThreadPoolExecutor threadPoolExecutor;

    public UseCaseThreadPoolScheduler(){
        threadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }

    @Override
    public void shutdown() {
        while(threadPoolExecutor.getQueue().poll() != null) {
            threadPoolExecutor.remove(threadPoolExecutor.getQueue().poll());
        }
    }

    @Override
    public <V extends UseCase.ResponseValues> void notifyResponse(
            final V response, final UseCase.Callback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onSuccess(response);
            }
        });
    }

    @Override
    public <V extends UseCase.ResponseValues> void onError(
            final String error, final UseCase.Callback<V> useCaseCallback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                useCaseCallback.onError(error);
            }
        });
    }
}