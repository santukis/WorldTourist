package com.david.worldtourist.useritems.domain.usecases.filter;

import com.david.worldtourist.items.domain.model.Item;

import java.util.List;


public class NoneFilter implements ItemFilter {

    @Override
    public List<Item> filter(List<Item> unfilteredList) {
        return unfilteredList;
    }
}
