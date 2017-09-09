package com.david.worldtourist.common.presentation.presenter;

import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.ActivityPresenter;
import com.david.worldtourist.common.presentation.boundary.ActivityView;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;

import javax.inject.Inject;

public class ActivityPresenterImp implements ActivityPresenter<ActivityView> {

    private ActivityView view;

    private final UseCaseHandler useCaseHandler;
    private final GetUser getUser;
    private final RemoveUser removeUser;

    @Inject
    public ActivityPresenterImp(UseCaseHandler useCaseHandler,
                                GetUser getUser,
                                RemoveUser removeUser) {
        this.useCaseHandler = useCaseHandler;
        this.getUser = getUser;
        this.removeUser = removeUser;
    }

    /////////////////////////////BasePresenter implementation//////////////////////////////
    @Override
    public void setView(@NonNull ActivityView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
        loadUser();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    /////////////////////////////ActivityPresenter implementation//////////////////////////////
    @Override
    public void removeUser(String userId) {

        final RemoveUser.RequestValues requestValues = new RemoveUser.RequestValues(userId);

        useCaseHandler.execute(removeUser, requestValues,
                new UseCase.Callback<RemoveUser.ResponseValues>() {
                    @Override
                    public void onSuccess(RemoveUser.ResponseValues response) {
                        updateUserView(User.EMPTY_USER);
                    }

                    @Override
                    public void onError(String error) {
                        updateUserView(User.EMPTY_USER);
                    }
                });
    }

    @Override
    public void updateUserSession() {

        getUser.execute(new GetUser.RequestValues(),
                new UseCase.Callback<GetUser.ResponseValues>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValues response) {
                        removeUser(response.getUser().getId());
                    }

                    @Override
                    public void onError(String error) {
                        view.onUserLogout();
                    }
                });
    }

    private void loadUser() {

        useCaseHandler.execute(getUser, new GetUser.RequestValues(),
                new UseCase.Callback<GetUser.ResponseValues>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValues response) {
                        updateUserView(response.getUser());
                    }

                    @Override
                    public void onError(String error) {
                        updateUserView(User.EMPTY_USER);
                    }
                });
    }

    private void updateUserView(User user) {
        view.showUserName(user.getName());
        view.showUserMail(user.getMail());
        view.showUserPhoto(user.getImage());

        if (user == User.EMPTY_USER) {
            view.showSessionState(R.string.menu_sign_in);
        } else {
            view.showSessionState(R.string.menu_sign_out);
        }
    }
}