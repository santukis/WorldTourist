package com.david.worldtourist.items.di.modules;

import android.content.Context;

import com.david.worldtourist.common.data.remote.NetworkClient;
import com.david.worldtourist.common.di.qualifiers.Local;
import com.david.worldtourist.common.di.qualifiers.Remote;
import com.david.worldtourist.items.data.boundary.ItemsDataSource;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.data.boundary.RestApi;
import com.david.worldtourist.items.data.remote.googleAPI.GooglePlacesJSONParser;
import com.david.worldtourist.items.data.remote.googleAPI.GooglePlacesRestApi;
import com.david.worldtourist.items.data.remote.RemoteDataSource;
import com.david.worldtourist.items.data.remote.ticketmasterAPI.TicketmasterJSONParser;
import com.david.worldtourist.items.data.remote.ticketmasterAPI.TicketmasterRestApi;
import com.david.worldtourist.items.data.remote.wikipediaAPI.WikipediaJSONParser;
import com.david.worldtourist.items.data.remote.wikipediaAPI.WikipediaRestApi;
import com.david.worldtourist.items.data.repository.ItemsRepositoryImp;
import com.david.worldtourist.items.di.qualifiers.Places;
import com.david.worldtourist.items.di.qualifiers.Ticket;
import com.david.worldtourist.items.di.qualifiers.Wiki;
import com.david.worldtourist.useritems.data.local.ItemsSQLiteDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class ItemsRepositoryModule {

    @Provides
    public ItemsRepository itemsRepository(@Local ItemsDataSource.Local localDataSource,
                                           @Remote ItemsDataSource.Remote remoteDataSource){
        return ItemsRepositoryImp.getInstance(localDataSource, remoteDataSource);
    }

    @Local
    @Provides
    public ItemsDataSource.Local localDataSource(Context context) {
        return new ItemsSQLiteDataSource(context);
    }

    @Remote
    @Provides
    public ItemsDataSource.Remote remoteDataSource(@Ticket RestApi ticketmasterRestApi,
                                                   @Places RestApi googlePlaceRestApi,
                                                   @Wiki RestApi wikipediaRestApi){
        return RemoteDataSource.getInstance(ticketmasterRestApi, googlePlaceRestApi, wikipediaRestApi);
    }

    @Ticket
    @Provides
    public RestApi ticketApi(NetworkClient networkClient, TicketmasterJSONParser jsonParser) {
        return TicketmasterRestApi.getInstance(networkClient, jsonParser);
    }

    @Provides
    public TicketmasterJSONParser ticketmasterJSONParser() {
        return TicketmasterJSONParser.getInstance();
    }

    @Wiki
    @Provides
    public RestApi wikiApi(NetworkClient networkClient, WikipediaJSONParser jsonParser) {
        return WikipediaRestApi.getInstance(networkClient, jsonParser);
    }

    @Provides
    public WikipediaJSONParser wikipediaJSONParser() {
        return WikipediaJSONParser.getInstance();
    }

    @Places
    @Provides
    public RestApi placesApi(NetworkClient networkClient, GooglePlacesJSONParser jsonParser) {
        return GooglePlacesRestApi.getInstance(networkClient, jsonParser);
    }

    @Provides
    public GooglePlacesJSONParser googlePlacesJSONParser() {
        return GooglePlacesJSONParser.getInstance();
    }
}