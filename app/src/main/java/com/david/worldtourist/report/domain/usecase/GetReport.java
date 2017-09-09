package com.david.worldtourist.report.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.data.boundary.ReportRepository;
import com.david.worldtourist.report.domain.model.Report;

import javax.inject.Inject;

public class GetReport extends UseCase<GetReport.RequestValues, GetReport.ResponseValues> {

    private final ReportRepository repository;

    @Inject
    public GetReport(ReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return new ResponseValues(repository.fetchReport());
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private Report report;

        public ResponseValues(Report report) {
            this.report = report;
        }

        public Report getReport() {
            return report;
        }
    }
}
