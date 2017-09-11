package com.david.worldtourist.aritems.presentation.presenter;

import android.Manifest;
import android.support.annotation.NonNull;

import com.david.worldtourist.aritems.presentation.boundary.ArItemsPresenter;
import com.david.worldtourist.aritems.presentation.components.AItem;
import com.david.worldtourist.aritems.presentation.boundary.ArItemsView;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.preferences.domain.usecase.GetCoordinates;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ArItemsPresenterImp extends CachedPresenterImp<ArItemsView> implements ArItemsPresenter<ArItemsView> {

    private ArItemsView view;

    private final UseCaseHandler useCaseHandler;

    private final GetItemCategory getItemCategory;
    private final GetDistance getDistance;
    private final GetItemTypes getItemTypes;
    private final GetCoordinates getUserLocation;
    private final GetItems getItems;
    private final IsPermissionGranted isPermissionGranted;
    private final IsNetworkAvailable isNetworkAvailable;
    private final RequestPermission requestPermission;

    private boolean loadingItems = false;

    public ArItemsPresenterImp(UseCaseHandler useCaseHandler,
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

        super(cacheItemCategory, getItemCategory, cacheItem, cacheMapCoordinates, null, null, null);

        this.useCaseHandler = useCaseHandler;
        this.getItemCategory = getItemCategory;
        this.getDistance = getDistance;
        this.getItemTypes = getItemTypes;
        this.getUserLocation = getLocation;
        this.getItems = getItems;
        this.isPermissionGranted = isPermissionGranted;
        this.isNetworkAvailable = isNetworkAvailable;
        this.requestPermission = requestPermission;
    }

    //////////////////////////BasePresenter implementation////////////////////////////

    @Override
    public void setView(@NonNull ArItemsView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();

    }

    @Override
    public void onStart() {
        getDistance.execute(new GetDistance.RequestValues(getItemCategory.executeSync().getItemCategory()));

        checkPermissions(
                new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                new int[] {
                        Constants.CAMERA_REQUEST_CODE,
                        Constants.FINE_LOCATION_REQUEST_CODE});
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
        view.releaseArItemsManager();
    }

    @Override
    public void cacheItemCategory(ItemCategory itemCategory) {
        super.cacheItemCategory(itemCategory);
        getDistance.execute(new GetDistance.RequestValues(getItemCategory.executeSync().getItemCategory()));
    }

    /////////////////////////ItemsPresenter implementation////////////////////////////

    public void checkPermissions(final String[] permissions, final int[] requestCodes) {

        for(int i = 0; i < permissions.length; i++) {
            final String permission = permissions[i];
            final int requestCode = requestCodes[i];
            final int counter = i;

            isPermissionGranted.execute(new IsPermissionGranted.RequestValues(permission),
                    new UseCase.Callback<IsPermissionGranted.ResponseValues>() {
                        @Override
                        public void onSuccess(IsPermissionGranted.ResponseValues response) {
                            if(counter == permissions.length - 1) {
                                view.initializeArItemsManager();
                                loadArItems(LoadingState.FIRST_LOAD);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            requestPermission.execute(new RequestPermission.RequestValues(
                                    permission, requestCode));
                        }
                    });
        }
    }

    @Override
    public void loadArItems(final LoadingState state) {

        if (!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        if (!loadingItems) {

            loadingItems = true;

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
                            view.showArItems(createArItems(response.getItems()));
                        }

                        @Override
                        public void onError(String error) {
                            loadingItems = false;
                            view.hideLoadingBar();

                            if (state == LoadingState.FIRST_LOAD || state == LoadingState.RELOAD) {
                                view.showArItems(new ArrayList<AItem>());
                                view.showToastMessage(
                                        itemCategory.getNoItemsText(),
                                        itemCategory.getItemIcon());

                            }
                        }
                    });
        }
    }

    private List<AItem> createArItems(List<Item> items) {
        List<AItem> aItems = new ArrayList<>();

        for(Item item : items) {
            AItem aItem = new AItem(item);
            aItems.add(aItem);
        }

        return aItems;
    }
}
