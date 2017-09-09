package com.david.worldtourist.items.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.useritems.domain.usecases.CacheItemFilter;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.GetSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;
import com.david.worldtourist.useritems.domain.usecases.UpdateSelectedItemIds;

import java.util.List;

public interface ItemsRepository {

    void loadItem(@NonNull final GetItem.RequestValues requestValues,
                  @NonNull final UseCase.Callback<GetItem.ResponseValues> callback);

    void loadItems(@NonNull final GetItems.RequestValues requestValues,
                   @NonNull final UseCase.Callback<GetItems.ResponseValues> callback);

    void loadUserItems(@NonNull GetUserItems.RequestValues requestValues,
                       @NonNull final UseCase.Callback<GetUserItems.ResponseValues> callback);

    void saveItem(@NonNull SaveItem.RequestValues requestValues,
                  @NonNull final UseCase.Callback<SaveItem.ResponseValues> callback);

    void deleteItems(@NonNull DeleteItems.RequestValues requestValues,
                     @NonNull final UseCase.Callback<DeleteItems.ResponseValues> callback);

    void loadUserFilter(@NonNull final GetItemFilter.RequestValues requestValues,
                        @NonNull final UseCase.Callback<GetItemFilter.ResponseValues> callback);

    void cacheItem(@NonNull final CacheItem.RequestValues requestValues);

    Item fetchItem();

    List<Item> fetchItems();

    void cacheItemCategory(@NonNull CacheItemCategory.RequestValues requestValues);

    ItemCategory fetchItemCategory();

    void cacheUserFilter(@NonNull CacheItemFilter.RequestValues requestValues);

    ItemUserFilter fetchUserFilter();

    List<String> fetchSelectedItemIds();

    void deleteSelectedItemIds();

    void updateItemId(@NonNull UpdateSelectedItemIds.RequestValues requestValues,
                      @NonNull UseCase.Callback<UpdateSelectedItemIds.ResponseValues> callback);
}
