package com.david.worldtourist.preferences.di.modules;


import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.preferences.domain.usecase.SaveDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveItemTypes;
import com.david.worldtourist.preferences.presentation.boundary.SettingsPresenter;
import com.david.worldtourist.preferences.presentation.boundary.SettingsView;
import com.david.worldtourist.preferences.presentation.presenter.SettingsPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencePresenterModule {

    @Provides
    public SettingsPresenter<SettingsView> settingsPresenter (GetItemTypes getItemTypes,
                                                              SaveItemTypes saveItemTypes,
                                                              GetDistance getDistance,
                                                              SaveDistance saveDistance) {

        return new SettingsPresenterImp(getItemTypes, saveItemTypes, getDistance, saveDistance);
    }
}
