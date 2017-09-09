package com.david.worldtourist.permissions.di.modules;


import android.app.Fragment;

import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.permissions.device.boundary.PermissionController;
import com.david.worldtourist.permissions.device.controller.PermissionControllerImp;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionControllerModule {

    private Fragment fragment;

    public PermissionControllerModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public PermissionController permissionController() {
        return new PermissionControllerImp(fragment);
    }
}
