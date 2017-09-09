package com.david.worldtourist.items.presentation.presenter;

import android.Manifest;
import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.sensors.device.services.LocationService;
import com.david.worldtourist.sensors.device.services.SensorListener;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.voice.domain.model.VoiceResult;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.voice.domain.usecase.ReleaseVoiceRecognition;
import com.david.worldtourist.items.domain.usecase.SearchItems;
import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.sensors.domain.usecase.StopSensorService;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.sensors.domain.usecase.StartSensorService;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;
import com.david.worldtourist.items.presentation.boundary.ItemsPresenter;
import com.david.worldtourist.items.presentation.boundary.ItemsView;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.preferences.domain.usecase.SaveCoordinates;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class ItemsPresenterImp extends CachedPresenterImp<ItemsView>
        implements ItemsPresenter<ItemsView>, SensorListener, Observer {

    private ItemsView view;

    private final UseCaseHandler useCaseHandler;

    private final GetItemCategory getItemCategory;
    private final GetDistance getDistance;
    private final GetItemTypes getItemTypes;
    private final GetCoordinates getUserLocation;
    private final SaveCoordinates saveUserLocation;
    private final GetItems getItems;
    private final StartSensorService startSensorService;
    private final StopSensorService stopSensorService;
    private final StartVoiceRecognition startVoiceRecognition;
    private final StopVoiceRecognition stopVoiceRecognition;
    private final ReleaseVoiceRecognition releaseVoiceRecognition;
    private final SearchItems searchItems;
    private final IsPermissionGranted isPermissionGranted;
    private final IsNetworkAvailable isNetworkAvailable;
    private final RequestPermission requestPermission;

    private boolean loadingItems = false;

    public ItemsPresenterImp(UseCaseHandler useCaseHandler,
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

        super(cacheItemCategory, getItemCategory, cacheItem, cacheMapCoordinates, null, null, null);

        this.useCaseHandler = useCaseHandler;
        this.getItemCategory = getItemCategory;
        this.getDistance = getDistance;
        this.getItemTypes = getItemTypes;
        this.getUserLocation = getLocation;
        this.saveUserLocation = saveLocation;
        this.getItems = getItems;
        this.startSensorService = startSensorService;
        this.stopSensorService = stopSensorService;
        this.startVoiceRecognition = startVoiceRecognition;
        this.stopVoiceRecognition = stopVoiceRecognition;
        this.releaseVoiceRecognition = releaseVoiceRecognition;
        this.searchItems = searchItems;
        this.isPermissionGranted = isPermissionGranted;
        this.isNetworkAvailable = isNetworkAvailable;
        this.requestPermission = requestPermission;
    }

    //////////////////////////BasePresenter implementation////////////////////////////

    @Override
    public void setView(@NonNull ItemsView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {
        getDistance.execute(
                new GetDistance.RequestValues(getItemCategory.executeSync().getItemCategory()));
    }

    @Override
    public void onStop() {
        useCaseHandler.shutdown();
        GeoCoordinate userLocation = getUserLocation.executeSync().getCurrentLocation();
        saveUserLocation.execute(new SaveCoordinates.RequestValues(userLocation));
        stopSensorService.execute(new StopSensorService.RequestValues());
    }

    @Override
    public void onDestroy() {
        releaseVoiceRecognition.execute(new ReleaseVoiceRecognition.RequestValues());
    }


    ///////////////////////ItemsSearchPresenter implementation/////////////////////////
    @Override
    public void startItemsSearch(final String text) {
        IsPermissionGranted.RequestValues requestValues =
                new IsPermissionGranted.RequestValues(Manifest.permission.RECORD_AUDIO);
        isPermissionGranted.execute(requestValues,
                new UseCase.Callback<IsPermissionGranted.ResponseValues>() {
                    @Override
                    public void onSuccess(IsPermissionGranted.ResponseValues response) {
                        view.enableMic(false);
                        startVoiceRecognition.execute(new StartVoiceRecognition.RequestValues(text, ItemsPresenterImp.this));
                    }

                    @Override
                    public void onError(String error) {
                        requestPermission.execute(new RequestPermission.RequestValues(
                                Manifest.permission.RECORD_AUDIO,
                                Constants.RECORD_AUDIO_REQUEST_CODE));
                    }
                });
    }

    @Override
    public void stopItemsSearch() {
        view.enableMic(true);
        stopVoiceRecognition.execute(new StopVoiceRecognition.RequestValues(this));
    }

    /////////////////////////ItemsPresenter implementation////////////////////////////
    @Override
    public void connectSensor(int sensorId, final SensorListener sensorListener) {
        if (sensorId == LOCATION_SENSOR) {

            IsPermissionGranted.RequestValues requestValues =
                    new IsPermissionGranted.RequestValues(Manifest.permission.ACCESS_FINE_LOCATION);
            isPermissionGranted.execute(requestValues,
                    new UseCase.Callback<IsPermissionGranted.ResponseValues>() {
                        @Override
                        public void onSuccess(IsPermissionGranted.ResponseValues response) {
                            connectLocationSensor(sensorListener);
                        }

                        @Override
                        public void onError(String error) {
                            requestPermission.execute(new RequestPermission.RequestValues(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Constants.FINE_LOCATION_REQUEST_CODE));
                        }
                    });
        }
    }

    private void connectLocationSensor(SensorListener sensorListener) {
        StartSensorService.RequestValues requestValues =
                new StartSensorService.RequestValues(LocationService.class, sensorListener);

        startSensorService.execute(requestValues,
                new UseCase.Callback<StartSensorService.ResponseValues>() {
                    @Override
                    public void onSuccess(StartSensorService.ResponseValues response) {
                        loadItems(LoadingState.FIRST_LOAD);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void loadItems(final LoadingState state) {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        if (!loadingItems) {

            loadingItems = true;

            view.hideInfoMessage();
            view.showLoadingBar();

            final ItemCategory itemCategory = getItemCategory.executeSync().getItemCategory();
            final Set<String> itemTypes = getItemTypes.executeSync().getItemTypes();
            final GeoCoordinate currentLocation = getUserLocation.executeSync().getCurrentLocation();
            final double distance = getDistance.executeSync().getDistance();

            GetItems.RequestValues requestValues = new GetItems.RequestValues(state, itemCategory, itemTypes,
                    currentLocation, distance);

            useCaseHandler.execute(getItems, requestValues,
                    new UseCase.Callback<GetItems.ResponseValues>() {
                        @Override
                        public void onSuccess(GetItems.ResponseValues response) {
                            loadingItems = false;
                            view.hideLoadingBar();
                            view.showItems(response.getItems(), currentLocation);
                        }

                        @Override
                        public void onError(String error) {
                            loadingItems = false;
                            view.hideLoadingBar();

                            if (state == LoadingState.FIRST_LOAD || state == LoadingState.RELOAD) {
                                view.showItems(new ArrayList<Item>(), currentLocation);
                                view.showInfoMessage(itemCategory.getNoItemsText(), itemCategory.getItemIcon());

                            }
                        }
                    });
        }
    }

    @Override
    public void restoreItems() {
        view.hideInfoMessage();
        view.showItems(getItems.executeSync().getItems(),
                getUserLocation.executeSync().getCurrentLocation());
    }

    /////////////////////////SensorListener implementation///////////////////////////
    @Override
    public void onSensorDataChanged(int sensorId, Object data) {
        if (sensorId == LOCATION_SENSOR) {
            saveUserLocation.execute(new SaveCoordinates.RequestValues((GeoCoordinate) data));
        }
    }

    @Override
    public void isSensorAvailable(int sensorId, boolean available) {
        if (sensorId == LOCATION_SENSOR) {
            view.showDialogMessage(FragmentView.LOCATION_ACTIVATION);
        }
    }

    ///////////////////////////Observer implementation/////////////////////////////
    @Override
    public void update(Observable observable, Object data) {

        if (data instanceof VoiceResult) {
            VoiceResult voiceResult = (VoiceResult) data;

            switch (voiceResult.getState()) {
                case VoiceResult.READY_FOR_SPEECH:
                    view.showToastMessage(
                            R.string.tts_ready,
                            R.drawable.ic_mic_ready);
                    break;
                case VoiceResult.RESULTS_RECOGNITION:
                    searchItems(voiceResult.getSentences());
                    break;
                case VoiceResult.ERROR_NETWORK:
                    view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
                    break;
                case VoiceResult.ERROR_NO_MATCH:
                    view.showToastMessage(
                            R.string.tts_error_no_match,
                            android.R.drawable.ic_dialog_alert);
                    break;
                case VoiceResult.ERROR_INSUFFICIENT_PERMISSIONS:
                    view.showToastMessage(
                            R.string.audio_permission_request,
                            android.R.drawable.ic_lock_idle_lock);
                    break;
            }

            if (voiceResult.getState() != VoiceResult.READY_FOR_SPEECH) {
                view.enableMic(true);
            }
        } else if (data instanceof List<?>) {
            searchItems((List<String>) data);
        }
    }

    private void searchItems(List<String> sentences) {
        view.showLoadingBar();

        SearchItems.RequestValues requestValues =
                new SearchItems.RequestValues(getItems.executeSync().getItems(), sentences);

        searchItems.execute(requestValues, new UseCase.Callback<SearchItems.ResponseValues>() {
            @Override
            public void onSuccess(SearchItems.ResponseValues response) {
                view.hideLoadingBar();
                view.hideInfoMessage();
                view.showItems(response.getItemsFound(),
                        getUserLocation.executeSync().getCurrentLocation());
            }

            @Override
            public void onError(String error) {
                view.hideLoadingBar();
                view.showItems(new ArrayList<Item>(),
                        getUserLocation.executeSync().getCurrentLocation());
                ItemCategory itemCategory = getItemCategory.executeSync().getItemCategory();
                view.showInfoMessage(itemCategory.getNoItemsText(), itemCategory.getItemIcon());
            }
        });
    }
}