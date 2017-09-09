package com.david.worldtourist.preferences.presentation.presenter;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.preferences.domain.usecase.GetDistance;
import com.david.worldtourist.preferences.domain.usecase.GetItemTypes;
import com.david.worldtourist.preferences.domain.usecase.SaveDistance;
import com.david.worldtourist.preferences.domain.usecase.SaveItemTypes;
import com.david.worldtourist.preferences.presentation.boundary.SettingsPresenter;
import com.david.worldtourist.preferences.presentation.boundary.SettingsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingsPresenterImp implements SettingsPresenter<SettingsView> {

    private SettingsView view;

    private final GetItemTypes getItemTypes;
    private final SaveItemTypes saveItemTypes;
    private final GetDistance getDistance;
    private final SaveDistance saveDistance;

    public SettingsPresenterImp(GetItemTypes getItemTypes,
                                SaveItemTypes saveItemTypes,
                                GetDistance getDistance,
                                SaveDistance saveDistance) {
        this.getItemTypes = getItemTypes;
        this.saveItemTypes = saveItemTypes;
        this.getDistance = getDistance;
        this.saveDistance = saveDistance;
    }

    @Override
    public void setView(@NonNull SettingsView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {

        getDistance.execute(new GetDistance.RequestValues(ItemCategory.SITE),
                new UseCase.Callback<GetDistance.ResponseValues>() {
                    @Override
                    public void onSuccess(GetDistance.ResponseValues response) {
                        view.showSiteDistance(response.getDistance());
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        getDistance.execute(new GetDistance.RequestValues(ItemCategory.EVENT),
                new UseCase.Callback<GetDistance.ResponseValues>() {
                    @Override
                    public void onSuccess(GetDistance.ResponseValues response) {
                        view.showEventDistance(response.getDistance());
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        loadItemTypesSummary();
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void saveTypes(Set<String> currentTypes) {
        saveItemTypes.execute(new SaveItemTypes.RequestValues(currentTypes));
        loadItemTypesSummary();
    }

    @Override
    public void saveDistance(double distance, ItemCategory itemCategory) {
        saveDistance.execute(new SaveDistance.RequestValues(distance, itemCategory));
    }

    private void loadItemTypesSummary() {

        List<String> selectedTypes =
                new ArrayList<>(getItemTypes.executeSync().getItemTypes());

        if(selectedTypes.isEmpty()) {
            view.showWarmingMessage();
        }

        view.showItemsTypeSummary(selectedTypes);

    }
}
