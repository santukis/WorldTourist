package com.david.worldtourist.authentication.data.firebaseAPI;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.Login;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.TwitterAuthProvider;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;

public class TwitterLogin implements Login {

    private final Activity activity;
    private TwitterAuthClient twitterAuthClient;
    private FirebaseAuth auth;
    private UseCase.Callback<DoLogin.ResponseValues> authCallback;

    public TwitterLogin(Activity activity, UseCase.Callback<DoLogin.ResponseValues> callback) {
        this.activity = activity;
        authCallback = callback;
        auth = FirebaseAuth.getInstance();
        init();
    }

    ////////////////////////////Login implementation/////////////////////////////
    @Override
    public void init() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                activity.getString(R.string.twitter_consumer_key),
                activity.getString(R.string.twitter_consumer_secret));

        Fabric.with(activity, new Twitter(authConfig));

        twitterAuthClient = new TwitterAuthClient();
    }

    @Override
    public void login() {

        twitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterAuth(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                authCallback.onError(Constants.LOGIN_AUTH_ERROR);
            }
        });
    }

    @Override
    public void onResult(int requestCode, int resultCode, Intent data) {
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private void twitterAuth(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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
