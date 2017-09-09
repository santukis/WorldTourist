package com.david.worldtourist.useritems.domain.usecases.filter;

import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.model.ItemUserFilter;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFilter implements ItemFilter {

    @Override
    public List<Item> filter(List<Item> unfilteredList) {
        List<Item> filteredList = new ArrayList<>();

        for(Item item : unfilteredList){
            if (item.getFilter() == ItemUserFilter.FAVOURITE ||
                    item.getFilter() == ItemUserFilter.ALL){
                filteredList.add(item);
            }
        }

        return filteredList;
    }
}
