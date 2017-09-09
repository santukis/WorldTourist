package com.david.worldtourist.itemsmap.presentation.presenter;


import android.Manifest;
import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.voice.domain.model.VoiceResult;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.voice.domain.usecase.ReleaseVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.DeleteRoute;
import com.david.worldtourist.itemsmap.domain.usecase.GetMapCoordinates;
import com.david.worldtourist.itemsmap.domain.usecase.GetAddress;
import com.david.worldtourist.itemsmap.domain.usecase.GetRoute;
import com.david.worldtourist.itemsmap.domain.usecase.model.Route;
import com.david.worldtourist.itemsmap.domain.usecase.model.TravelMode;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapPresenter;
import com.david.worldtourist.itemsmap.presentation.boundary.ItemsMapView;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class ItemsMapPresenterImp extends CachedPresenterImp<ItemsMapView>
        implements ItemsMapPresenter<ItemsMapView>, Observer {

    private ItemsMapView view;

    private final UseCaseHandler useCaseHandler;
    private final GetItemCategory getItemCategory;
    private final GetItem getItem;
    private final GetCoordinates getUserLocation;
    private final GetMapCoordinates getMapCoordinates;
    private final GetDistance getDistance;
    private final GetItemTypes getItemTypes;
    private final GetItems getItems;
    private final GetRoute getRoute;
    private final DeleteRoute deleteRoute;
    private final StartVoiceRecognition startVoiceRecognition;
    private final StopVoiceRecognition stopVoiceRecognition;
    private final ReleaseVoiceRecognition releaseVoiceRecognition;
    private final GetAddress getAddress;
    private final IsPermissionGranted isPermissionGranted;
    private final IsNetworkAvailable isNetworkAvailable;
    private final RequestPermission requestPermission;

    private boolean loadingItems = false;

    public ItemsMapPresenterImp(UseCaseHandler useCaseHandler,
                                CacheItemCategory cacheItemCategory,
                                GetItemCategory getItemCategory,
                                CacheItem cacheItem,
                                CacheMapCoordinates cacheMapCoordinates,
                                GetItem getItem,
                                GetCoordinates getUserLocation,
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

        super(cacheItemCategory, getItemCategory, cacheItem, cacheMapCoordinates, null, null, null);

        this.useCaseHandler = useCaseHandler;
        this.getItemCategory = getItemCategory;
        this.getItem = getItem;
        this.getUserLocation = getUserLocation;
        this.getMapCoordinates = getMapCoordinates;
        this.getDistance = getDistance;
        this.getItemTypes = getItemTypes;
        this.getItems = getItems;
        this.getRoute = getRoute;
        this.deleteRoute = deleteRoute;
        this.startVoiceRecognition = startVoiceRecognition;
        this.stopVoiceRecognition = stopVoiceRecognition;
        this.releaseVoiceRecognition = releaseVoiceRecognition;
        this.getAddress = getAddress;
        this.isPermissionGranted = isPermissionGranted;
        this.isNetworkAvailable = isNetworkAvailable;
        this.requestPermission = requestPermission;
    }


    /////////////////////////////BasePresenter implementation//////////////////////////////
    @Override
    public void setView(@NonNull ItemsMapView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {
        IsPermissionGranted.RequestValues requestValues =
                new IsPermissionGranted.RequestValues(Manifest.permission.ACCESS_FINE_LOCATION);
        isPermissionGranted.execute(requestValues,
                new UseCase.Callback<IsPermissionGranted.ResponseValues>() {
                    @Override
                    public void onSuccess(IsPermissionGranted.ResponseValues response) {
                        getDistance.execute(
                                new GetDistance.RequestValues(getItemCategory.executeSync().getItemCategory()));
                    }

                    @Override
                    public void onError(String error) {
                        requestPermission.execute(new RequestPermission.RequestValues(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Constants.FINE_LOCATION_REQUEST_CODE));
                    }
                });

    }

    @Override
    public void onStop() {
        useCaseHandler.shutdown();
        stopVoiceRecognition.execute(new StopVoiceRecognition.RequestValues(this));
    }

    @Override
    public void onDestroy() {
        cacheMapCoordinates(GeoCoordinate.EMPTY_COORDINATE);

        releaseVoiceRecognition.execute(new ReleaseVoiceRecognition.RequestValues());
    }

    ///////////////////////////CachePresenter implementation////////////////////////////
    @Override
    public void cacheItemCategory(ItemCategory itemCategory) {

        if (getItemCategory.executeSync().getItemCategory() != itemCategory) {
            getDistance.execute(new GetDistance.RequestValues(itemCategory));
            view.clearMap();
            view.clearMarkers();
        }

        super.cacheItemCategory(itemCategory);
    }

    @Override
    public void cacheItem(Item item) {
        super.cacheItem(item);
        view.loadItemDetail();
    }

    ///////////////////////////ItemsMapPresenter implementation////////////////////////////
    @Override
    public void loadStartingPosition() {
        GeoCoordinate mapCoordinates = getMapCoordinates();

        view.showStartingPosition(mapCoordinates);

        Item item = getItem.executeSync().getItem();
        GeoCoordinate itemCoordinates = item.getCoordinate();

        if (mapCoordinates.equals(itemCoordinates)) {
            view.clearMap();
            view.clearMarkers();
            view.showStartingItem(item);
        }
    }

    private GeoCoordinate getMapCoordinates() {
        GeoCoordinate mapCoordinates = getMapCoordinates.executeSync().getMapLocation();

        if (mapCoordinates == GeoCoordinate.EMPTY_COORDINATE) {
            mapCoordinates = getUserLocation.executeSync().getCurrentLocation();
        }
        return mapCoordinates;
    }

    @Override
    public void loadItems(GeoCoordinate coordinates) {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        if (!loadingItems) {

            loadingItems = true;

            ItemCategory itemCategory = getItemCategory.executeSync().getItemCategory();
            Set<String> itemTypes = getItemTypes.executeSync().getItemTypes();
            double distance = getDistance.executeSync().getDistance();

            GetItems.RequestValues requestValues = new GetItems.RequestValues(LoadingState.FIRST_LOAD,
                    itemCategory, itemTypes, coordinates, distance);

            useCaseHandler.execute(getItems, requestValues,
                    new UseCase.Callback<GetItems.ResponseValues>() {
                        @Override
                        public void onSuccess(GetItems.ResponseValues response) {
                            view.showItems(response.getItems());
                            loadingItems = false;
                        }

                        @Override
                        public void onError(String error) {
                            view.showItems(new ArrayList<Item>());
                            loadingItems = false;
                        }
                    });
        }
    }

    @Override
    public void loadRoute(Item item, GeoCoordinate origin, TravelMode travelMode) {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        view.showLoadingBar();

        final GetRoute.RequestValues requestValues = new GetRoute.RequestValues(
                item.getName(), origin, item.getCoordinate(), travelMode);

        useCaseHandler.execute(getRoute, requestValues, new UseCase.Callback<GetRoute.ResponseValues>() {
            @Override
            public void onSuccess(GetRoute.ResponseValues response) {
                view.hideLoadingBar();

                if (response.getRoute() != Route.EMPTY_ROUTE) {
                    view.drawRoute(response.getRoute());
                    refreshRoute(response.getRoute());
                }
            }

            @Override
            public void onError(String error) {
                view.hideLoadingBar();
                view.showToastMessage(
                        R.string.route_error,
                        R.drawable.ic_directions);
            }
        });
    }

    @Override
    public void refreshRoute(Route route) {
        view.showRouteView();
        view.showRouteDestinationName(route.getName());
        view.showRouteIcon(route.getTravelMode());
        view.showRouteDistance(route.getDistance());
        view.showRouteDuration(route.getDuration());
        view.showRouteInstructions(route.getRouteSteps());
    }

    @Override
    public void deleteRoute(Route route) {

        if (route != null) {
            DeleteRoute.RequestValues requestValues = new DeleteRoute.RequestValues(
                    route.getName(), route.getTravelMode());

            deleteRoute.execute(requestValues);
        }
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
                        startVoiceRecognition.execute(new StartVoiceRecognition.RequestValues(text, ItemsMapPresenterImp.this));
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

    }

    @Override
    public void update(Observable o, Object data) {
        if (data instanceof VoiceResult) {
            VoiceResult voiceResult = (VoiceResult) data;

            switch (voiceResult.getState()) {
                case VoiceResult.READY_FOR_SPEECH:
                    view.showToastMessage(
                            R.string.tts_ready,
                            R.drawable.ic_mic_ready);
                    break;
                case VoiceResult.RESULTS_RECOGNITION:
                    loadAddress(voiceResult.getSentences());
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

            if (voiceResult.getState() != VoiceResult.READY_FOR_SPEECH)
                view.enableMic(true);
        }
    }

    private void loadAddress(List<String> sentences) {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        view.showLoadingBar();

        GetAddress.RequestValues requestValues = new GetAddress.RequestValues(sentences);

        useCaseHandler.execute(getAddress, requestValues,
                new UseCase.Callback<GetAddress.ResponseValues>() {
                    @Override
                    public void onSuccess(GetAddress.ResponseValues response) {
                        view.hideLoadingBar();
                        view.clearMap();
                        view.clearMarkers();
                        view.showStartingPosition(response.getAddress().getCoordinates());
                        view.showAddressIcon(response.getAddress());
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                        view.showToastMessage(
                                R.string.address_error,
                                android.R.drawable.ic_dialog_alert);
                    }
                });
    }
}