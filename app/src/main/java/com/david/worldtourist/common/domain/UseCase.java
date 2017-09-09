package com.david.worldtourist.common.domain;


public abstract class UseCase<Q extends UseCase.RequestValues, R extends UseCase.ResponseValues> {

    private Callback<R> callback;


    public void setCallback(Callback<R> callback){
        this.callback = callback;
    }

    public Callback<R> getCallback(){
        return callback;
    }



    public abstract void execute(Q requestValues);

    public abstract void execute(Q requestValues, Callback<R> callback);

    public abstract R executeSync();



    public interface RequestValues{}

    public interface ResponseValues{}

    public interface Callback<R>{

        void onSuccess(R response);

        void onError(String error);
    }
}
