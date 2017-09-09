package com.david.worldtourist.report.di.modules;


import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.report.data.boundary.ReportDataSource;
import com.david.worldtourist.report.data.boundary.ReportRepository;
import com.david.worldtourist.report.data.remote.HostingerDataSource;
import com.david.worldtourist.report.data.repository.ReportRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module
public class ReportRepositoryModule {

    @Provides
    public ReportRepository reportRepository(ReportDataSource reportDataSource){
        return ReportRepositoryImp.getInstance(reportDataSource);
    }

    @Provides
    public ReportDataSource reportDataSource(NetworkClient networkClient){
        return HostingerDataSource.getInstance(networkClient);
    }
}
