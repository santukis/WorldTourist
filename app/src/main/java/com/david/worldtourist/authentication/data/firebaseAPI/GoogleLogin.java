package com.david.worldtourist.authentication.data.firebaseAPI;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.Login;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.utils.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleLogin implements Login {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 123;

    private final Activity activity;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth auth;
    private UseCase.Callback<DoLogin.ResponseValues> authCallback;


    public GoogleLogin(Activity activity, UseCase.Callback<DoLogin.ResponseValues> callback) {
        this.activity = activity;
        authCallback = callback;
        auth = FirebaseAuth.getInstance();
        init();
    }

    ////////////////////////////Login implementation/////////////////////////////
    @Override
    public void init() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.google_web_client_ID))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage((AppCompatActivity) activity,
                        (GoogleApiClient.OnConnectionFailedListener) activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    public void login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            googleAuth(account);

        } else {
            authCallback.onError(Constants.LOGIN_AUTH_ERROR);
            googleApiClient.stopAutoManage((AppCompatActivity) activity);
            googleApiClient.disconnect();
        }
    }

    private void googleAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(activity,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            authCallback.onError(Constants.LOGIN_AUTH_ERROR);

                        } else {
                            authCallback.onSuccess(new DoLogin.ResponseValues());
                        }
                    }
                });
    }
}
