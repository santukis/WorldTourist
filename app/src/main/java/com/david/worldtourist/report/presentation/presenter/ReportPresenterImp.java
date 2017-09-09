package com.david.worldtourist.report.presentation.presenter;


import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.report.domain.model.Report;
import com.david.worldtourist.report.domain.model.ReportFilter;
import com.david.worldtourist.report.domain.usecase.GetReport;
import com.david.worldtourist.report.domain.usecase.SendReport;
import com.david.worldtourist.report.presentation.boundary.ReportPresenter;

public class ReportPresenterImp extends CachedPresenterImp<FragmentView>
        implements ReportPresenter<FragmentView> {

    private FragmentView view;

    private final UseCaseHandler useCaseHandler;
    private final GetReport getReport;
    private final GetUser getUser;
    private final SendReport sendReport;
    private final IsNetworkAvailable isNetworkAvailable;

    public ReportPresenterImp(UseCaseHandler useCaseHandler,
                              CacheReport cacheReport,
                              GetReport getReport,
                              GetUser getUser,
                              SendReport sendReport,
                              IsNetworkAvailable isNetworkAvailable) {

        super(null, null, null, null, null, null, cacheReport);

        this.useCaseHandler = useCaseHandler;
        this.getReport = getReport;
        this.getUser = getUser;
        this.sendReport = sendReport;
        this.isNetworkAvailable = isNetworkAvailable;
    }

    //////////////////////////BasePresenter implementation////////////////////////////
    @Override
    public void setView(@NonNull FragmentView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        cacheReport(Report.EMPTY_REPORT);
    }

    /////////////////////////ReportPresenter implementation////////////////////////////
    @Override
    public void setReportedProblem(ReportFilter reportFilter) {
        getReport.executeSync().getReport().setReportedProblem(reportFilter);
    }

    @Override
    public void setExtraInformation(String extraInformation) {
        getReport.executeSync().getReport().setExtraInformation(extraInformation);
    }

    @Override
    public void sendReport() {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        if (getUser.executeSync().getUser() == User.EMPTY_USER) {
            view.showDialogMessage(FragmentView.UNREGISTER_USER);
            return;
        }

        if (getReport.executeSync().getReport().getReportedProblem() != ReportFilter.NONE) {
            view.showLoadingBar();
            SendReport.RequestValues requestValues =
                    new SendReport.RequestValues(getReport.executeSync().getReport());

            useCaseHandler.execute(sendReport, requestValues,
                    new UseCase.Callback<SendReport.ResponseValues>() {
                        @Override
                        public void onSuccess(SendReport.ResponseValues response) {
                            view.hideLoadingBar();
                            view.showToastMessage(
                                    R.string.report_email_sent,
                                    android.R.drawable.ic_menu_send);
                            view.closeView();
                        }

                        @Override
                        public void onError(String error) {
                            view.hideLoadingBar();
                            view.showToastMessage(
                                    R.string.report_email_error_sent,
                                    android.R.drawable.ic_dialog_alert);
                            view.closeView();
                        }
                    });
        } else {
            view.showToastMessage(
                    R.string.report_no_problem_message,
                    android.R.drawable.ic_dialog_alert);
        }
    }
}
