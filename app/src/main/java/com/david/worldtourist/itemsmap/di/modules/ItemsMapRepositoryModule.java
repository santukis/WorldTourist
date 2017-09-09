package com.david.worldtourist.itemsmap.di.modules;


import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.itemsmap.data.boundary.ItemsMapRepository;
import com.david.worldtourist.itemsmap.data.boundary.MapsDataSource;
import com.david.worldtourist.itemsmap.data.remote.GoogleMapsDataSource;
import com.david.worldtourist.itemsmap.data.repository.ItemsMapRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module
public class ItemsMapRepositoryModule {

    @Provides
    public ItemsMapRepository itemsMapRepository(MapsDataSource dataSource) {
        return ItemsMapRepositoryImp.getInstance(dataSource);
    }

    @Provides
    public MapsDataSource directionsDataSource(NetworkClient networkClient) {
        return GoogleMapsDataSource.getInstance(networkClient);
    }
}
