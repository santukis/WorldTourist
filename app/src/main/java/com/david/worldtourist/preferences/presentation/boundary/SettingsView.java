package com.david.worldtourist.preferences.presentation.boundary;



import com.david.worldtourist.common.presentation.boundary.BaseView;

import java.util.List;

public interface SettingsView extends BaseView {

    void showSiteDistance(double distance);

    void showEventDistance(double distance);

    void showWarmingMessage();

    void showItemsTypeSummary(List<String> selectedItems);

}
