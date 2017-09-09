package com.david.worldtourist.report.di.modules;


import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.authentication.di.modules.UserRepositoryModule;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.report.domain.usecase.GetReport;
import com.david.worldtourist.report.domain.usecase.SendReport;
import com.david.worldtourist.report.presentation.boundary.ReportPresenter;
import com.david.worldtourist.report.presentation.presenter.ReportPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ReportRepositoryModule.class,
        PermissionControllerModule.class,
        UserRepositoryModule.class})
public class ReportPresenterModule {

    @Provides
    public ReportPresenter<FragmentView> reportPresenter(UseCaseHandler useCaseHandler,
                                                         CacheReport cacheReport,
                                                         GetReport getReport,
                                                         GetUser getUser,
                                                         SendReport sendReport,
                                                         IsNetworkAvailable isNetworkAvailable) {

        return new ReportPresenterImp(
                useCaseHandler,
                cacheReport,
                getReport,
                getUser,
                sendReport,
                isNetworkAvailable);
    }
}
