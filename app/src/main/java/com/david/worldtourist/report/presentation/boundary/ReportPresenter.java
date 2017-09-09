package com.david.worldtourist.report.presentation.boundary;

import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;
import com.david.worldtourist.report.domain.model.ReportFilter;


public interface ReportPresenter<T extends BaseView> extends CachedPresenter<T> {

    void setReportedProblem(ReportFilter reportFilter);

    void setExtraInformation(String extraInformation);

    void sendReport();
}
