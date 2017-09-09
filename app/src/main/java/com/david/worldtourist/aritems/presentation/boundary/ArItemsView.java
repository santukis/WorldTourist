package com.david.worldtourist.aritems.presentation.boundary;

import com.david.worldtourist.aritems.presentation.components.AItem;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.items.domain.model.GeoCoordinate;

import java.util.List;

public interface ArItemsView extends FragmentView {

    void initializeArItemsManager();

    void releaseArItemsManager();

    void showArItems(List<AItem> aItems);

}
