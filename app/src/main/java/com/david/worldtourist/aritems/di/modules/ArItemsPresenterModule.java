package com.david.worldtourist.aritems.di.modules;


import com.david.worldtourist.aritems.presentation.boundary.ArItemsPresenter;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsView;
import com.david.worldtourist.aritems.presentation.presenter.ArItemsPresenterImp;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.sensors.di.modules.SensorControllerModule;
import com.david.worldtourist.items.di.modules.ItemsRepositoryModule;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.sensors.domain.usecase.StartSensorService;
import com.david.worldtourist.sensors.domain.usecase.StopSensorService;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;
import com.david.worldtourist.itemsmap.di.modules.ItemsMapRepositoryModule;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.preferences.di.modules.PreferenceRepositoryModule;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ItemsRepositoryModule.class,
        SensorControllerModule.class,
        ItemsMapRepositoryModule.class,
        PermissionControllerModule.class,
        PreferenceRepositoryModule.class})
public class ArItemsPresenterModule {

    @Provides
    public ArItemsPresenter<ArItemsView> arItemsPresenter(UseCaseHandler useCaseHandler,
                                                          CacheItemCategory cacheItemCategory,
                                                          GetItemCategory getItemCategory,
                                                          CacheItem cacheItem,
                                                          CacheMapCoordinates cacheMapCoordinates,
                                                          GetDistance getDistance,
                                                          GetItemTypes getItemTypes,
                                                          GetCoordinates getLocation,
                                                          GetItems getItems,
                                                          IsPermissionGranted isPermissionGranted,
                                                          IsNetworkAvailable isNetworkAvailable,
                                                          RequestPermission requestPermission) {

        return new ArItemsPresenterImp(
                useCaseHandler,
                cacheItemCategory,
                getItemCategory,
                cacheItem,
                cacheMapCoordinates,
                getDistance,
                getItemTypes,
                getLocation,
                getItems,
                isPermissionGranted,
                isNetworkAvailable,
                requestPermission);
    }
}
