package com.david.worldtourist.report.data.repository;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.data.boundary.ReportDataSource;
import com.david.worldtourist.report.data.boundary.ReportRepository;
import com.david.worldtourist.report.domain.model.Report;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.report.domain.usecase.SendReport;

public class ReportRepositoryImp implements ReportRepository {

    private static ReportRepositoryImp INSTANCE = null;

    private final ReportDataSource dataSource;

    private Report cachedReport;

    private ReportRepositoryImp(ReportDataSource dataStorage) {
        dataSource = dataStorage;
    }

    public static ReportRepositoryImp getInstance(ReportDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ReportRepositoryImp(dataSource);
        }
        return INSTANCE;
    }

    @Override
    public void sendReport(@NonNull SendReport.RequestValues requestValues,
                           @NonNull final UseCase.Callback<SendReport.ResponseValues> callback) {

        dataSource.sendReport(requestValues, callback);
    }

    @Override
    public void cacheReport(@NonNull CacheReport.RequestValues requestValues){
        this.cachedReport = requestValues.getReport();
    }

    @Override
    public Report fetchReport(){
        return cachedReport;
    }

}
