package com.david.worldtourist.items.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.ItemsSearchPresenter;
import com.david.worldtourist.items.domain.model.LoadingState;
import com.david.worldtourist.sensors.device.services.SensorListener;

public interface ItemsPresenter<T extends BaseView> extends ItemsSearchPresenter<T> {

    void connectSensor(int sensorId, SensorListener listener);

    void loadItems(LoadingState state);

    void restoreItems();

}