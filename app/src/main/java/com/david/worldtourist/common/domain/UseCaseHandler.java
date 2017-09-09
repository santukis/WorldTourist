package com.david.worldtourist.common.domain;

public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;
    private final UseCaseScheduler useCaseScheduler;


    private UseCaseHandler (UseCaseScheduler useCaseScheduler){
        this.useCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public <Q extends UseCase.RequestValues, R extends UseCase.ResponseValues>
    void execute(final UseCase<Q,R> useCase, final Q values, final UseCase.Callback<R> callback){

        setupUiCallback(useCase, callback);

        useCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.execute(values);
            }
        });
    }

    public void shutdown() {
        useCaseScheduler.shutdown();
    }

    private <Q extends UseCase.RequestValues, R extends UseCase.ResponseValues>
    void setupUiCallback(UseCase<Q,R> useCase, final UseCase.Callback<R> callback) {

        useCase.setCallback(new UseCase.Callback<R>() {
            @Override
            public void onSuccess(R response) {
                useCaseScheduler.notifyResponse(response, callback);
            }

            @Override
            public void onError(String error) {
                useCaseScheduler.onError(error, callback);
            }
        });
    }
}