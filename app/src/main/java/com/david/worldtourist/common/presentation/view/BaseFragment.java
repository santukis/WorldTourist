package com.david.worldtourist.common.presentation.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.authentication.presentation.view.LoginActivity;
import com.david.worldtourist.utils.AndroidApplication;
import com.squareup.leakcanary.RefWatcher;

public class BaseFragment extends Fragment implements FragmentView {

    private View toastView;
    private ProgressDialog loadingDialog;

    private TextView toastText;
    private ImageView toastIcon;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);
        toastView = inflater.inflate(R.layout.element_toast_view, container, false);

        return toastView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }

    @Override
    public void onDestroy() {
        RefWatcher refWatcher = AndroidApplication.get(getActivity()).getRefWatcher();
        refWatcher.watch(this);
        super.onDestroy();
    }

    ////////////////////////////BaseView implementation///////////////////////////
    @Override
    public void initializeViewComponents() {
        toastText = (TextView) toastView.findViewById(R.id.toast_text);
        toastIcon = (ImageView) toastView.findViewById(R.id.toast_icon);
    }

    @Override
    public void initializeViewListeners() {

    }

    //////////////////////////FragmentView implementation/////////////////////////
    @Override
    public void showToastMessage(int messageReference, int iconReference) {
        toastText.setText(messageReference);
        toastIcon.setBackgroundResource(iconReference);

        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(toastView);
        toast.show();
    }

    @Override
    public void showDialogMessage(int dialogId) {
        switch (dialogId) {
            case UNREGISTER_USER:
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.unregistered_user)
                        .setIcon(R.drawable.ic_user_registration)
                        .setMessage(R.string.login_requiremente_message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((HomeActivity) getActivity()).openActivity(LoginActivity.class);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                break;

            case NETWORK_ACTIVATION:
                showActivationDialog(
                        R.string.error_connection,
                        R.string.internet_disconnected,
                        Settings.ACTION_SETTINGS);
                break;

            case LOCATION_ACTIVATION:
                showActivationDialog(
                        R.string.error_connection,
                        R.string.provider_disable,
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                break;
        }
    }

    @Override
    public void showLoadingBar() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setMessage(getString(R.string.loading));
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCanceledOnTouchOutside(false);
        }

        loadingDialog.show();

    }

    @Override
    public void hideLoadingBar() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void closeView() {
        ((HomeActivity) getActivity()).restoreNextToLastFragment();
        getActivity().getFragmentManager().popBackStackImmediate();
    }

    private void showActivationDialog(int title, int message, final String actionRequest) {

        new AlertDialog.Builder(getActivity())
                .setTitle(title).setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(actionRequest);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })
                .show();
    }
}