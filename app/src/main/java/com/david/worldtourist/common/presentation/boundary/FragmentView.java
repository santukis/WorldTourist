package com.david.worldtourist.common.presentation.boundary;


public interface FragmentView extends BaseView {

    int UNREGISTER_USER = 1;
    int NETWORK_ACTIVATION = 2;
    int LOCATION_ACTIVATION = 3;


    void showToastMessage (int messageReference, int iconReference);

    void showDialogMessage(int dialogId);

    void showLoadingBar();

    void hideLoadingBar();

    void closeView();

}
