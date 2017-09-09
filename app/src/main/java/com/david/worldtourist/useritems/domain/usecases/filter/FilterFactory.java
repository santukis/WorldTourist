package com.david.worldtourist.useritems.domain.usecases.filter;

import com.david.worldtourist.items.domain.model.ItemUserFilter;

import java.util.HashMap;
import java.util.Map;


public class FilterFactory {

    private final Map<ItemUserFilter, ItemFilter> filter = new HashMap<>();

    public FilterFactory(){
        filter.put(ItemUserFilter.NONE, new NoneFilter());
        filter.put(ItemUserFilter.ALL, new NoneFilter());
        filter.put(ItemUserFilter.FAVOURITE, new FavouriteFilter());
        filter.put(ItemUserFilter.TO_VISIT, new ToVisitFilter());
    }

    public ItemFilter createFilter(ItemUserFilter filterType){
        return filter.get(filterType);
    }
}