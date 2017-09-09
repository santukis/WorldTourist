package com.david.worldtourist.preferences.di.component;


import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.preferences.di.modules.PreferencePresenterModule;
import com.david.worldtourist.preferences.di.modules.PreferenceRepositoryModule;
import com.david.worldtourist.preferences.presentation.boundary.SettingsPresenter;
import com.david.worldtourist.preferences.presentation.boundary.SettingsView;

import dagger.Component;

@FragmentScope
@Component(modules = {PreferencePresenterModule.class, PreferenceRepositoryModule.class},
        dependencies = ApplicationComponent.class)
public interface PreferenceComponent {

    SettingsPresenter<SettingsView> getSettingsPresenter();
}
