package com.david.worldtourist.items.data.repository;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.data.boundary.ItemsDataSource;
import com.david.worldtourist.items.data.boundary.ItemsRepository;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.useritems.domain.usecases.CacheItemFilter;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.UpdateSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;
import com.david.worldtourist.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemsRepositoryImp implements ItemsRepository {

    private static ItemsRepositoryImp INSTANCE = null;

    private ItemsDataSource.Local localDataSource;
    private ItemsDataSource.Remote remoteDataSource;

    private Item cachedItem = Item.EMPTY_ITEM;
    private List<Item> cachedItems = new ArrayList<>();
    private List<String> cachedItemIds = new ArrayList<>();
    private ItemCategory cachedItemCategory = ItemCategory.NONE;
    private ItemUserFilter cachedItemUserFilter = ItemUserFilter.FAVOURITE;

    private ItemsRepositoryImp(ItemsDataSource.Local localDataSource,
                               ItemsDataSource.Remote remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    public static ItemsRepositoryImp getInstance(ItemsDataSource.Local localDataSource,
                                                 ItemsDataSource.Remote remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ItemsRepositoryImp(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadItem(@NonNull final GetItem.RequestValues requestValues,
                         @NonNull final UseCase.Callback<GetItem.ResponseValues> callback) {

        remoteDataSource.getItem(requestValues, new UseCase.Callback<GetItem.ResponseValues>() {
            @Override
            public void onSuccess(GetItem.ResponseValues response) {
                callback.onSuccess(response);
                cachedItem = response.getItem();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void loadItems(@NonNull final GetItems.RequestValues requestValues,
                          @NonNull final UseCase.Callback<GetItems.ResponseValues> callback) {

        if (requestValues.getItemTypes().isEmpty()) {
            callback.onError(Constants.EMPTY_LIST_ERROR);
            return;
        }

        remoteDataSource.getItems(requestValues, new UseCase.Callback<GetItems.ResponseValues>() {
            @Override
            public void onSuccess(GetItems.ResponseValues response) {
                LoadingState state = requestValues.getLoadingState();

                if(state == LoadingState.FIRST_LOAD || state == LoadingState.RELOAD) {
                    cachedItems = response.getItems();
                    callback.onSuccess(response);

                } else if (state == LoadingState.UPDATE) {
                    cachedItems.addAll(response.getItems());
                    callback.onSuccess(new GetItems.ResponseValues(cachedItems));
                }

            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void loadUserItems(@NonNull GetUserItems.RequestValues requestValues,
                              @NonNull final UseCase.Callback<GetUserItems.ResponseValues> callback) {
        localDataSource.getUserItems(requestValues, new UseCase.Callback<GetUserItems.ResponseValues>() {
            @Override
            public void onSuccess(GetUserItems.ResponseValues response) {
                callback.onSuccess(response);
                cachedItems = response.getUserItems();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void saveItem(@NonNull SaveItem.RequestValues requestValues,
                         @NonNull UseCase.Callback<SaveItem.ResponseValues> callback) {
        localDataSource.saveItem(requestValues, callback);
    }

    @Override
    public void deleteItems(@NonNull DeleteItems.RequestValues requestValues,
                            @NonNull final UseCase.Callback<DeleteItems.ResponseValues> callback) {

        localDataSource.deleteItems(requestValues, new UseCase.Callback<DeleteItems.ResponseValues>() {
            @Override
            public void onSuccess(DeleteItems.ResponseValues response) {
                callback.onSuccess(response);
                cachedItemIds.clear();
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
                cachedItemIds.clear();
            }
        });
    }

    @Override
    public void loadUserFilter(@NonNull final GetItemFilter.RequestValues requestValues,
                               @NonNull final UseCase.Callback<GetItemFilter.ResponseValues> callback) {

        localDataSource.getItem(new GetItem.RequestValues(null, requestValues.getItemId(), null),
                new UseCase.Callback<GetItem.ResponseValues>() {
                    @Override
                    public void onSuccess(GetItem.ResponseValues response) {
                        cachedItemUserFilter = response.getItem().getFilter();
                        callback.onSuccess(new GetItemFilter.ResponseValues(cachedItemUserFilter));
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError(error);
                    }
                });
    }

    @Override
    public void cacheItem(@NonNull final CacheItem.RequestValues requestValues) {
        cachedItem = requestValues.getItem();
    }

    @Override
    public Item fetchItem() {
        return cachedItem;
    }

    @Override
    public List<Item> fetchItems() {
        return cachedItems;
    }

    @Override
    public void cacheItemCategory(CacheItemCategory.RequestValues requestValues){
        this.cachedItemCategory = requestValues.getItemCategory();
    }

    @Override
    public ItemCategory fetchItemCategory(){
        return cachedItemCategory;
    }

    @Override
    public void cacheUserFilter(@NonNull CacheItemFilter.RequestValues requestValues) {
        cachedItemUserFilter = requestValues.getUserFilter();
    }

    @Override
    public ItemUserFilter fetchUserFilter() {
        return cachedItemUserFilter;
    }

    @Override
    public synchronized void updateItemId(@NonNull UpdateSelectedItemIds.RequestValues requestValues,
                                          @NonNull UseCase.Callback<UpdateSelectedItemIds.ResponseValues> callback) {
        String itemId = requestValues.getItemId();

        for(String id : cachedItemIds) {
            if(itemId.equals(id)) {
                removeIdFromCachedIds(itemId);

                if(cachedItemIds.isEmpty()) {
                    callback.onError(Constants.EMPTY_LIST_ERROR);
                }
                return;
            }
        }
        cachedItemIds.add(itemId);

        callback.onSuccess(new UpdateSelectedItemIds.ResponseValues());

    }

    private synchronized void removeIdFromCachedIds(String itemId) {

        for(Iterator<String> iterator = cachedItemIds.iterator(); iterator.hasNext();) {
            String id = iterator.next();
            if(itemId.equals(id)) {
                iterator.remove();
            }
        }
    }

    @Override
    public List<String> fetchSelectedItemIds() {
        return cachedItemIds;
    }

    @Override
    public void deleteSelectedItemIds() {
        cachedItemIds.clear();
    }
}