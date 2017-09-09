package com.david.worldtourist.report.data.boundary;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.report.domain.usecase.SendReport;

public interface ReportDataSource {

    void sendReport (SendReport.RequestValues requestValues,
                     UseCase.Callback<SendReport.ResponseValues> callback);
}
