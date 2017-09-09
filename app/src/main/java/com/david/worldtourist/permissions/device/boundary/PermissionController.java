package com.david.worldtourist.permissions.device.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;

public interface PermissionController {

    void isPermissionGranted(@NonNull IsPermissionGranted.RequestValues requestValues,
                             @NonNull UseCase.Callback<IsPermissionGranted.ResponseValues> callback);

    void requestPermission(@NonNull RequestPermission.RequestValues requestValues);

    boolean isNetworkAvailable();
}
