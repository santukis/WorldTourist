package com.david.worldtourist.useritems.presentation.presenter;

import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.useritems.domain.usecases.CacheItemFilter;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.GetSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;
import com.david.worldtourist.useritems.domain.usecases.DeleteSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.UpdateSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.filter.FilterFactory;
import com.david.worldtourist.useritems.domain.usecases.filter.ItemFilter;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsPresenter;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsView;

import java.util.ArrayList;
import java.util.List;

public class UserItemsPresenterImp extends CachedPresenterImp<UserItemsView> implements UserItemsPresenter<UserItemsView> {

    private UserItemsView view;

    private final UseCaseHandler useCaseHandler;

    private final GetItemCategory getItemCategory;
    private final GetItemFilter getUserFilter;
    private final GetUserItems getUserItems;
    private final UpdateSelectedItemIds updateSelectedItemIds;
    private final GetSelectedItemIds getSelectedItemIds;
    private final DeleteSelectedItemIds deleteSelectedItemIds;
    private final DeleteItems deleteItems;

    public UserItemsPresenterImp(UseCaseHandler useCaseHandler,
                                 CacheItemCategory cacheItemCategory,
                                 GetItemCategory getItemCategory,
                                 CacheItem cacheItem,
                                 CacheItemFilter cacheItemFilter,
                                 GetItemFilter getUserFilter,
                                 GetUserItems getUserItems,
                                 UpdateSelectedItemIds updateSelectedItemIds,
                                 GetSelectedItemIds getSelectedItemIds,
                                 DeleteSelectedItemIds deleteSelectedItemIds,
                                 DeleteItems deleteItems) {

        super(cacheItemCategory, getItemCategory, cacheItem, null, cacheItemFilter, null, null);

        this.useCaseHandler = useCaseHandler;
        this.getItemCategory = getItemCategory;
        this.getUserFilter = getUserFilter;
        this.getUserItems = getUserItems;
        this.updateSelectedItemIds = updateSelectedItemIds;
        this.getSelectedItemIds = getSelectedItemIds;
        this.deleteSelectedItemIds = deleteSelectedItemIds;
        this.deleteItems = deleteItems;
    }

    ///////////////////////BasePresenter implementation////////////////////////
    @Override
    public void setView(@NonNull UserItemsView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        deleteSelectedItemIds.execute(null);
    }

    @Override
    public void onDestroy() {

    }

    ////////////////////UserItemsPresenter implementation/////////////////////
    @Override
    public void loadUserItems() {

        view.hideInfoMessage();
        view.showLoadingBar();

        GetUserItems.RequestValues requestValues =
                new GetUserItems.RequestValues(
                        createItemFilter(getUserFilter.executeSync().getItemFilter()),
                        getItemCategory.executeSync().getItemCategory());

        useCaseHandler.execute(getUserItems, requestValues,
                new UseCase.Callback<GetUserItems.ResponseValues>() {
                    @Override
                    public void onSuccess(GetUserItems.ResponseValues response) {
                        view.hideLoadingBar();
                        view.showItems(response.getUserItems(), null);
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                        view.showItems(new ArrayList<Item>(), null);
                        ItemCategory itemCategory = getItemCategory.executeSync().getItemCategory();
                        view.showInfoMessage(itemCategory.getSingleName(), itemCategory.getItemIcon());
                    }
                });
    }

    @Override
    public void updateSelectedItems(String itemId) {

        updateSelectedItemIds.execute(new UpdateSelectedItemIds.RequestValues(itemId),
                new UseCase.Callback<UpdateSelectedItemIds.ResponseValues>() {
                    @Override
                    public void onSuccess(UpdateSelectedItemIds.ResponseValues response) {
                        view.showDeleteMenu();
                    }

                    @Override
                    public void onError(String error) {
                        view.hideDeleteMenu();
                    }
                });
    }

    @Override
    public void deleteItems() {

        List<String> itemIds = getSelectedItemIds.executeSync().getItemIds();

        DeleteItems.RequestValues requestValues = new DeleteItems.RequestValues(itemIds);

        useCaseHandler.execute(deleteItems, requestValues,
                new UseCase.Callback<DeleteItems.ResponseValues>() {
                    @Override
                    public void onSuccess(DeleteItems.ResponseValues response) {
                        view.showToastMessage(
                                R.string.deleted_item,
                                android.R.drawable.ic_delete);
                        view.hideDeleteMenu();
                        loadUserItems();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private ItemFilter createItemFilter(ItemUserFilter itemUserFilter) {

        return new FilterFactory().createFilter(itemUserFilter);
    }
}