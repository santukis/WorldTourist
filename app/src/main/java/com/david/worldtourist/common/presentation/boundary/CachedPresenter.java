package com.david.worldtourist.common.presentation.boundary;


import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.report.domain.model.Report;

public interface CachedPresenter<T extends BaseView> extends BasePresenter<T> {

    void cacheItemCategory(ItemCategory itemCategory);

    void cacheItem(Item item);

    void cacheMapCoordinates(GeoCoordinate coordinate);

    void cacheUserFilter(ItemUserFilter itemUserFilter);

    void cacheMessage(Message message, MessageActionFilter messageAction);

    void cacheReport(Report report);

    int getItemName(ItemNameFilter itemName);
}
