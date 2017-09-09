package com.david.worldtourist.items.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;

import java.util.List;

public interface RestApi {

    Item getItem(@NonNull GetItem.RequestValues requestValues);

    List<Item> getItems(@NonNull GetItems.RequestValues requestValues);
}
