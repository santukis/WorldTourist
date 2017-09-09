package com.david.worldtourist.useritems.domain.usecases.filter;

import com.david.worldtourist.items.domain.model.Item;

import java.util.List;


public interface ItemFilter {

    List<Item> filter(List<Item> unfilteredList);
}
