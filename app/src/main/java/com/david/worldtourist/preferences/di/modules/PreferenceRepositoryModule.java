package com.david.worldtourist.preferences.di.modules;


import android.content.Context;

import com.david.worldtourist.preferences.data.boundary.PreferenceDataSource;
import com.david.worldtourist.preferences.data.boundary.PreferenceRepository;
import com.david.worldtourist.preferences.data.local.PreferenceSource;
import com.david.worldtourist.preferences.data.repository.PreferenceRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferenceRepositoryModule {

    @Provides
    public PreferenceRepository preferenceRepository(PreferenceDataSource dataSource) {
        return PreferenceRepositoryImp.getInstance(dataSource);
    }

    @Provides
    public PreferenceDataSource preferenceDataSource(Context context) {
        return PreferenceSource.getInstance(context);
    }
}
