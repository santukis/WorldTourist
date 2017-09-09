package com.david.worldtourist.itemsdetail.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.data.boundary.ReportRepository;
import com.david.worldtourist.report.domain.model.Report;

import javax.inject.Inject;

public class CacheReport extends UseCase<CacheReport.RequestValues, CacheReport.ResponseValues> {

    private final ReportRepository repository;

    @Inject
    public CacheReport(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.cacheReport(requestValues);
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

        public RequestValues(Report report) {
            this.report = report;
        }

        public Report getReport() {
            return this.report;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
