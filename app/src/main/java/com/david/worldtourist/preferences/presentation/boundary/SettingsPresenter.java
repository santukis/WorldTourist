package com.david.worldtourist.preferences.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BasePresenter;
import com.david.worldtourist.items.domain.model.ItemCategory;

import java.util.Set;

public interface SettingsPresenter<T extends SettingsView> extends BasePresenter<T> {

    void saveTypes(Set<String> types);

    void saveDistance(double distance, ItemCategory itemCategory);

}
