package com.david.worldtourist.permissions.device.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.permissions.device.boundary.PermissionController;
import com.david.worldtourist.permissions.domain.usecase.IsPermissionGranted;
import com.david.worldtourist.permissions.domain.usecase.RequestPermission;
import com.david.worldtourist.utils.Constants;


public class PermissionControllerImp implements PermissionController {

    private Fragment fragment;


    public PermissionControllerImp(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public void isPermissionGranted(@NonNull IsPermissionGranted.RequestValues requestValues,
                                    @NonNull UseCase.Callback<IsPermissionGranted.ResponseValues> callback) {

        String permission = requestValues.getPermission();

        if (ContextCompat.checkSelfPermission(fragment.getActivity().getApplicationContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            callback.onSuccess(new IsPermissionGranted.ResponseValues(true));

        } else {
            callback.onError(Constants.PERMISSION_REQUEST_ERROR);
        }

    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(@NonNull RequestPermission.RequestValues requestValues) {

        String permission = requestValues.getPermission();
        int requestCode = requestValues.getRequestCode();

        fragment.requestPermissions(new String[] {permission}, requestCode);
    }

    @Override
    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    fragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();

        } catch (NullPointerException exception) {
            return false;
        }
    }
}
