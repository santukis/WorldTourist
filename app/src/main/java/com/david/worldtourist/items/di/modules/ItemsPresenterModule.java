package com.david.worldtourist.items.di.modules;

import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.sensors.di.modules.SensorControllerModule;
import com.david.worldtourist.voice.di.modules.VoiceControllerModule;
import com.david.worldtourist.voice.domain.usecase.ReleaseVoiceRecognition;
import com.david.worldtourist.items.domain.usecase.SearchItems;
import com.david.worldtourist.sensors.domain.usecase.StartSensorService;
import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.sensors.domain.usecase.StopSensorService;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;
import com.david.worldtourist.items.presentation.boundary.ItemsView;
import com.david.worldtourist.items.presentation.presenter.ItemsPresenterImp;
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
import com.david.worldtourist.preferences.domain.usecase.SaveCoordinates;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ItemsRepositoryModule.class,
        SensorControllerModule.class,
        ItemsMapRepositoryModule.class,
        PermissionControllerModule.class,
        VoiceControllerModule.class,
        PreferenceRepositoryModule.class})
public class ItemsPresenterModule {

    @Provides
    public ItemsPresenter<ItemsView> presenter(UseCaseHandler useCaseHandler,
                                               CacheItemCategory cacheItemCategory,
                                               GetItemCategory getItemCategory,
                                               CacheItem cacheItem,
                                               CacheMapCoordinates cacheMapCoordinates,
                                               GetDistance getDistance,
                                               GetItemTypes getItemTypes,
                                               GetCoordinates getLocation,
                                               SaveCoordinates saveLocation,
                                               GetItems getItems,
                                               StartSensorService startSensorService,
                                               StopSensorService stopSensorService,
                                               StartVoiceRecognition startVoiceRecognition,
                                               StopVoiceRecognition stopVoiceRecognition,
                                               ReleaseVoiceRecognition releaseVoiceRecognition,
                                               SearchItems searchItems,
                                               IsPermissionGranted isPermissionGranted,
                                               IsNetworkAvailable isNetworkAvailable,
                                               RequestPermission requestPermission) {

        return new ItemsPresenterImp(useCaseHandler,
                cacheItemCategory,
                getItemCategory,
                cacheItem,
                cacheMapCoordinates,
                getDistance,
                getItemTypes,
                getLocation,
                saveLocation,
                getItems,
                startSensorService,
                stopSensorService,
                startVoiceRecognition,
                stopVoiceRecognition,
                releaseVoiceRecognition,
                searchItems,
                isPermissionGranted,
                isNetworkAvailable,
                requestPermission);
    }
}