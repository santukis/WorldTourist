package com.david.worldtourist.permissions.presentation.view;


import android.app.AlertDialog;
import android.content.pm.PackageManager;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.view.BaseFragment;
import com.david.worldtourist.utils.Constants;

public class PermissionFragment extends BaseFragment {

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.FINE_LOCATION_REQUEST_CODE ||
                requestCode == Constants.CAMERA_REQUEST_CODE) {
            if (grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.permission_denied)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }

        } else if (requestCode == Constants.RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.audio_permission_denied)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        }
    }
}
