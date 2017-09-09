package com.david.worldtourist.authentication.presentation.boundary;


import android.content.Intent;

import com.david.worldtourist.common.presentation.boundary.BasePresenter;
import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.authentication.domain.model.ProviderFilter;

public interface LoginPresenter<T extends BaseView> extends BasePresenter<T> {

    void login(ProviderFilter provider);

    void onResult(int requestCode, int resultCode, Intent data);
}
