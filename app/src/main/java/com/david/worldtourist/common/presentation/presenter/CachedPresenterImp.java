package com.david.worldtourist.common.presentation.presenter;

import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.items.domain.usecase.CacheItem;
import com.david.worldtourist.items.domain.usecase.CacheItemCategory;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.report.domain.model.Report;
import com.david.worldtourist.useritems.domain.usecases.CacheItemFilter;


public abstract class CachedPresenterImp<T extends BaseView> implements CachedPresenter<T> {

    private final CacheItemCategory cacheItemCategory;
    private final GetItemCategory getItemCategory;
    private final CacheItem cacheItem;
    private final CacheMapCoordinates cacheMapCoordinates;
    private final CacheItemFilter cacheItemFilter;
    private final CacheMessage cacheMessage;
    private final CacheReport cacheReport;


    public CachedPresenterImp(CacheItemCategory cacheItemCategory,
                              GetItemCategory getItemCategory,
                              CacheItem cacheItem,
                              CacheMapCoordinates cacheMapCoordinates,
                              CacheItemFilter cacheItemFilter,
                              CacheMessage cacheMessage,
                              CacheReport cacheReport) {
        this.cacheItemCategory = cacheItemCategory;
        this.getItemCategory = getItemCategory;
        this.cacheItem = cacheItem;
        this.cacheMapCoordinates = cacheMapCoordinates;
        this.cacheItemFilter = cacheItemFilter;
        this.cacheMessage = cacheMessage;
        this.cacheReport = cacheReport;
    }

    @Override
    public void cacheItemCategory(ItemCategory itemCategory) {
        cacheItemCategory.execute(new CacheItemCategory.RequestValues(itemCategory));
    }

    @Override
    public void cacheItem(Item item) {
        cacheItem.execute(new CacheItem.RequestValues(item));
    }

    @Override
    public void cacheMapCoordinates(GeoCoordinate coordinate) {
        cacheMapCoordinates.execute(new CacheMapCoordinates.RequestValues(coordinate));
    }

    @Override
    public void cacheUserFilter(ItemUserFilter itemUserFilter){
        cacheItemFilter.execute(new CacheItemFilter.RequestValues(itemUserFilter));
    }

    @Override
    public void cacheMessage(Message message, MessageActionFilter messageAction){
        cacheMessage.execute(new CacheMessage.RequestValues(message, messageAction));
    }

    @Override
    public void cacheReport(Report report){
        cacheReport.execute(new CacheReport.RequestValues(report));
    }

    @Override
    public int getItemName(ItemNameFilter itemName) {
        ItemCategory itemCategory = getItemCategory.executeSync().getItemCategory();

        switch (itemName) {
            case SINGLE:
                return itemCategory.getSingleName();
            case PLURAL:
                return itemCategory.getPluralName();
            default:
                return -1;
        }
    }
}
