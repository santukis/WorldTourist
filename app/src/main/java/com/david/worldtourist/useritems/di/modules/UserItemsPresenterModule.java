package com.david.worldtourist.useritems.di.modules;


import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.items.di.modules.ItemsRepositoryModule;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.useritems.domain.usecases.CacheItemFilter;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.DeleteSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.GetSelectedItemIds;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;
import com.david.worldtourist.useritems.domain.usecases.UpdateSelectedItemIds;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsPresenter;
import com.david.worldtourist.useritems.presentation.boundary.UserItemsView;
import com.david.worldtourist.useritems.presentation.presenter.UserItemsPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ItemsRepositoryModule.class})
public class UserItemsPresenterModule {

    @Provides
    public UserItemsPresenter<UserItemsView> userItemsPresenter(UseCaseHandler useCaseHandler,
                                                                CacheItemCategory cacheItemCategory,
                                                                GetItemCategory getItemCategory,
                                                                CacheItem cacheItem,
                                                                CacheItemFilter cacheItemFilter,
                                                                GetItemFilter getItemFilter,
                                                                GetUserItems getUserItems,
                                                                UpdateSelectedItemIds updateSelectedItemIds,
                                                                GetSelectedItemIds getSelectedItemIds,
                                                                DeleteSelectedItemIds deleteSelectedItemIds,
                                                                DeleteItems deleteItems) {

        return new UserItemsPresenterImp(
                useCaseHandler,
                cacheItemCategory,
                getItemCategory,
                cacheItem,
                cacheItemFilter,
                getItemFilter,
                getUserItems,
                updateSelectedItemIds,
                getSelectedItemIds,
                deleteSelectedItemIds,
                deleteItems);
    }
}
