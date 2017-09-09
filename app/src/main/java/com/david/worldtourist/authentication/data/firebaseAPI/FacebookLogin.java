package com.david.worldtourist.authentication.data.firebaseAPI;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.Login;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Arrays;

public class FacebookLogin implements Login {

    private final Activity activity;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private UseCase.Callback<DoLogin.ResponseValues> authCallback;


    public FacebookLogin(Activity activity, UseCase.Callback<DoLogin.ResponseValues> callback) {
        this.activity = activity;
        authCallback = callback;
        auth = FirebaseAuth.getInstance();
        init();
    }

    ////////////////////////////Login implementation/////////////////////////////
    @Override
    public void init() {
        FacebookSdk.sdkInitialize(activity);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void login() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        facebookAuth(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        authCallback.onError(Constants.LOGIN_AUTH_ERROR);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        authCallback.onError(error.getLocalizedMessage());
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(activity,
                Arrays.asList("public_profile", "email"));
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookAuth(AccessToken accessToken) {
        final AuthCredential credential =
                FacebookAuthProvider.getCredential(accessToken.getToken());

        auth.signInWithCredential(credential).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            authCallback.onError(Constants.LOGIN_AUTH_ERROR);

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                LoginManager.getInstance().logOut();
                            }

                        } else {
                            authCallback.onSuccess(new DoLogin.ResponseValues());
                        }
                    }
                });
    }

}
