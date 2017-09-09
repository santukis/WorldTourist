package com.david.worldtourist.report.di.components;


import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.report.di.modules.ReportPresenterModule;
import com.david.worldtourist.report.presentation.boundary.ReportPresenter;

import dagger.Component;

@FragmentScope
@Component(modules = ReportPresenterModule.class,
        dependencies = ApplicationComponent.class)
public interface ReportComponent {

    ReportPresenter<FragmentView> getReportPresenter();
}
