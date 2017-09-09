package com.david.worldtourist.authentication.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.authentication.di.components.DaggerLoginComponent;
import com.david.worldtourist.authentication.di.components.LoginComponent;
import com.david.worldtourist.authentication.di.modules.AuthenticationControllerModule;
import com.david.worldtourist.authentication.domain.model.ProviderFilter;
import com.david.worldtourist.authentication.presentation.boundary.LoginPresenter;
import com.david.worldtourist.authentication.presentation.boundary.LoginView;
import com.david.worldtourist.utils.AndroidApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity implements LoginView,
        GoogleApiClient.OnConnectionFailedListener {

    private LoginPresenter<LoginView> presenter;

    @BindView(R.id.buttons_layout) LinearLayout buttonsLayout;
    @BindView(R.id.progress_bar_layout) RelativeLayout progressBarLayout;
    @BindView(R.id.tv_register) TextView titleText;
    @BindView(R.id.tv_welcome) TextView welcomeText;
    @BindView(R.id.not_login) TextView unsignedButton;
    @BindView(R.id.btnGoogle) Button googleButton;
    @BindView(R.id.btnFacebook) Button facebookButton;
    @BindView(R.id.btnTwitter) Button twitterButton;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        LoginComponent component = DaggerLoginComponent.builder()
                .applicationComponent(AndroidApplication.get(this).getApplicationComponent())
                .authenticationControllerModule(new AuthenticationControllerModule(this))
                .build();

        presenter = component.getLoginPresenter();

        presenter.setView(this);
        presenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onResult(requestCode, resultCode, data);
    }

    ///////////////////////////LoginView implementation//////////////////////////
    @Override
    public void initializeViewComponents() {
        ButterKnife.bind(this);
        progressBarLayout.setClickable(false);
    }

    @Override
    public void initializeViewListeners() {

        unsignedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(ProviderFilter.NONE);
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(ProviderFilter.GOOGLE);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(ProviderFilter.FACEBOOK);
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.login(ProviderFilter.TWITTER);
            }
        });
    }

    @Override
    public void showLoadingBar() {
        buttonsLayout.setVisibility(View.GONE);
        welcomeText.setVisibility(View.GONE);
        titleText.setVisibility(View.GONE);
        unsignedButton.setVisibility(View.GONE);
        progressBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingBar() {
        progressBarLayout.setVisibility(View.GONE);
        welcomeText.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        buttonsLayout.setVisibility(View.VISIBLE);
        unsignedButton.setVisibility(View.VISIBLE);
   }

    @Override
    public void showErrorMessage(String provider) {
        String message = String.format(getString(R.string.login_error), provider);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    //////////GoogleApiClient.OnConnectionFailedListener implementation//////////////
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }
}