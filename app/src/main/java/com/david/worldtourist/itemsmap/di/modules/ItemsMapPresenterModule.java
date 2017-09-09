package com.david.worldtourist.itemsmap.di.modules;


import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.sensors.di.modules.SensorControllerModule;
import com.david.worldtourist.items.di.modules.ItemsRepositoryModule;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.voice.di.modules.VoiceControllerModule;
import com.david.worldtourist.voice.domain.usecase.ReleaseVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.DeleteRoute;
import com.david.worldtourist.itemsmap.domain.usecase.GetMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapPresenter;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapView;
import com.david.worldtourist.itemsmap.presentation.presenter.ItemsMapPresenterImp;
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

@Module(includes = {
        ItemsRepositoryModule.class,
        SensorControllerModule.class,
        PreferenceRepositoryModule.class,
        PermissionControllerModule.class,
        VoiceControllerModule.class,
        ItemsMapRepositoryModule.class})
public class ItemsMapPresenterModule {

    @Provides
    public ItemsMapPresenter<ItemsMapView> itemsMapPresenter(UseCaseHandler useCaseHandler,
                                                             CacheItemCategory cacheItemCategory,
                                                             GetItemCategory getItemCategory,
                                                             CacheItem cacheItem,
                                                             CacheMapCoordinates cacheMapCoordinates,
                                                             GetItem getItem,
                                                             GetCoordinates getCoordinates,
                                                             GetMapCoordinates getMapCoordinates,
                                                             GetDistance getDistance,
                                                             GetItemTypes getItemTypes,
                                                             GetItems getItems,
                                                             GetRoute getRoute,
                                                             DeleteRoute deleteRoute,
                                                             StartVoiceRecognition startVoiceRecognition,
                                                             StopVoiceRecognition stopVoiceRecognition,
                                                             ReleaseVoiceRecognition releaseVoiceRecognition,
                                                             GetAddress getAddress,
                                                             IsPermissionGranted isPermissionGranted,
                                                             IsNetworkAvailable isNetworkAvailable,
                                                             RequestPermission requestPermission) {

        return new ItemsMapPresenterImp(
                useCaseHandler,
                cacheItemCategory,
                getItemCategory,
                cacheItem,
                cacheMapCoordinates,
                getItem,
                getCoordinates,
                getMapCoordinates,
                getDistance,
                getItemTypes,
                getItems,
                getRoute,
                deleteRoute,
                startVoiceRecognition,
                stopVoiceRecognition,
                releaseVoiceRecognition,
                getAddress,
                isPermissionGranted,
                isNetworkAvailable,
                requestPermission);
    }
}
