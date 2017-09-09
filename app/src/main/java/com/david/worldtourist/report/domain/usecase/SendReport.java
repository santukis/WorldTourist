package com.david.worldtourist.report.domain.usecase;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.data.boundary.ReportRepository;
import com.david.worldtourist.report.domain.model.Report;

import javax.inject.Inject;

public class SendReport extends UseCase<SendReport.RequestValues, SendReport.ResponseValues> {


    private final ReportRepository repository;

    @Inject
    public SendReport(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.sendReport(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                getCallback().onSuccess(response);
            }

            @Override
            public void onError(String error) {
                getCallback().onError(error);
            }
        });
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Report report;

        public RequestValues (@NonNull Report report) {
            this.report = report;
        }

        public Report getReport() {
            return report;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
