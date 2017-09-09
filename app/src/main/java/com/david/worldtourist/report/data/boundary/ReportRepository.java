package com.david.worldtourist.report.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.report.domain.model.Report;
import com.david.worldtourist.report.domain.usecase.SendReport;

public interface ReportRepository {

    void sendReport(@NonNull SendReport.RequestValues requestValues,
                    @NonNull final UseCase.Callback<SendReport.ResponseValues> callback);

    void cacheReport(@NonNull CacheReport.RequestValues requestValues);

    Report fetchReport();
}
