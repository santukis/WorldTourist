package com.david.worldtourist.common.domain;

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCase.ResponseValues>
    void notifyResponse(final V response, final UseCase.Callback<V> useCaseCallback);

    <V extends UseCase.ResponseValues>
    void onError(final String error, final UseCase.Callback<V> useCaseCallback);

    void shutdown();
}
