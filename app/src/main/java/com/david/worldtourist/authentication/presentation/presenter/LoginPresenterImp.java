package com.david.worldtourist.authentication.presentation.presenter;


import android.content.Intent;
import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.usecase.AuthResult;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;
import com.david.worldtourist.authentication.presentation.boundary.LoginPresenter;
import com.david.worldtourist.authentication.presentation.boundary.LoginView;

import com.david.worldtourist.authentication.domain.model.ProviderFilter;

public class LoginPresenterImp<T extends LoginView> implements LoginPresenter<T> {

    private LoginView view;

    private final UseCaseHandler useCaseHandler;
    private final DoLogin doLogin;
    private final AuthResult authResult;
    private final GetUser getUser;
    private final SaveUser saveUser;

    public LoginPresenterImp(UseCaseHandler useCaseHandler,
                             DoLogin doLogin,
                             AuthResult authResult,
                             GetUser getUser,
                             SaveUser saveUser) {
        this.useCaseHandler = useCaseHandler;
        this.doLogin = doLogin;
        this.authResult = authResult;
        this.getUser = getUser;
        this.saveUser = saveUser;
    }

    ///////////////////////LoginPresenter implementation////////////////////////
    @Override
    public void setView(@NonNull LoginView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {
        loadUser();
    }

    @Override
    public void onStop() {
        useCaseHandler.shutdown();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void login(final ProviderFilter provider) {

        if(provider == ProviderFilter.NONE) {
            view.loadActivity();
            return;
        }

        view.showLoadingBar();

        DoLogin.RequestValues requestValues = new DoLogin.RequestValues(provider);

        useCaseHandler.execute(doLogin, requestValues,
                new UseCase.Callback<DoLogin.ResponseValues>() {
                    @Override
                    public void onSuccess(DoLogin.ResponseValues response) {
                        loadUser();
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                        view.showErrorMessage(provider.toString());
                    }
                });
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        view.showLoadingBar();

        AuthResult.RequestValues requestValues =
                new AuthResult.RequestValues(requestCode, resultCode, data);

        authResult.execute(requestValues);
    }

    private void loadUser() {

        view.showLoadingBar();

        useCaseHandler.execute(getUser, null,
                new UseCase.Callback<GetUser.ResponseValues>() {
                    @Override
                    public void onSuccess(GetUser.ResponseValues response) {
                        saveUser(response.getUser());
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                    }
                });

    }

    private void saveUser(User user) {

        SaveUser.RequestValues requestValues = new SaveUser.RequestValues(user);

        useCaseHandler.execute(saveUser, requestValues,
                new UseCase.Callback<SaveUser.ResponseValues>() {
                    @Override
                    public void onSuccess(SaveUser.ResponseValues response) {
                        view.loadActivity();
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                    }
                });
    }
}
