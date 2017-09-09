package com.david.worldtourist.common.presentation.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.david.worldtourist.R;
import com.david.worldtourist.common.di.components.ActivityComponent;
import com.david.worldtourist.common.di.components.DaggerActivityComponent;
import com.david.worldtourist.common.di.modules.HomeActivityModule;
import com.david.worldtourist.common.presentation.boundary.ActivityView;
import com.david.worldtourist.common.presentation.boundary.ActivityPresenter;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.common.presentation.navigation.Navigator;
import com.david.worldtourist.authentication.presentation.view.LoginActivity;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityView {

    private static final String CURRENT_FRAGMENT_KEY = "current_fragment";

    @Inject ActivityPresenter<ActivityView> presenter;
    @Inject Navigator navigator;

    private View headerLayout;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout navigationDrawer;

    //////////////////////////////Activity Lifecycle///////////////////////////////

    @Override
    protected void onCreate(Bundle inputState) {
        super.onCreate(inputState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_home);

        ActivityComponent component = DaggerActivityComponent.builder()
                .applicationComponent(AndroidApplication.get(this).getApplicationComponent())
                .homeActivityModule(new HomeActivityModule(this))
                .build();

        component.inject(this);

        presenter.setView(this);

        presenter.onCreate();

        initializeFragment(inputState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FRAGMENT_KEY, navigator.getFragmentFilter());
    }

    public void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    ///////////////////////////////Navigation View///////////////////////////////////////
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.user_signing:
                presenter.updateUserSession();
                break;
            case R.id.events:
                openFragment(FragmentType.EVENTS, null);
                break;
            case R.id.sites:
                openFragment(FragmentType.SITES, null);
                break;
            case R.id.virtual_tour:
                openFragment(FragmentType.ITEMS_MAP, null);
                break;
            case R.id.user_sites:
                openFragment(FragmentType.USER_SITES, null);
                break;
            case R.id.user_events:
                openFragment(FragmentType.USER_EVENTS, null);
                break;
            case R.id.ar_geolocation:
                openFragment(FragmentType.ARITEMS, null);
                break;
            case R.id.preferences:
                openFragment(FragmentType.PREFERENCES, null);
                break;
            case R.id.about:
                showAbout();
                break;
            default:
                break;
        }

        navigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAbout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_about)
                .setMessage(R.string.about_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (closeNavigationDrawerIfOpened()) {
            return;

        } else if (closeAppIfLastFragment()) {
            finish();

        } else {
            restoreNextToLastFragment();
        }

        super.onBackPressed();
    }

    private boolean closeNavigationDrawerIfOpened(){
        if(navigationDrawer.isDrawerOpen(GravityCompat.START)){
            navigationDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean closeAppIfLastFragment(){
        final int LAST_FRAGMENT = 1;
        return getFragmentManager().getBackStackEntryCount() == LAST_FRAGMENT;
    }

    public void restoreNextToLastFragment(){
        final int NEXT_TO_LAST = getFragmentManager().getBackStackEntryCount() - 2;
        String fragmentTag = getFragmentManager()
                .getBackStackEntryAt(NEXT_TO_LAST)
                .getName();
        navigator.restoreNextToLastFragment(fragmentTag);
    }

    ////////////////////////////////ActionBar Options/////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////ActivityView implementation////////////////////////////////
    @Override
    public void initializeViewComponents() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle(Constants.EMPTY_STRING);

        headerLayout = navigationView.getHeaderView(0);
    }

    @Override
    public void initializeViewListeners() {
        navigationView.setNavigationItemSelectedListener(this);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            getSupportActionBar().show();

                        } else {
                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            getSupportActionBar().hide();
                        }
                    }
                });
    }

    @Override
    public void showUserName(String name) {
        if(name.isEmpty()) name = getString(R.string.unregistered_user);
        ((TextView) headerLayout.findViewById(R.id.tv_user_name)).setText(name);
    }

    @Override
    public void showUserMail(String mail) {
        if(mail.isEmpty()) mail = getString(R.string.user_state);
        ((TextView) headerLayout.findViewById(R.id.tv_user_mail)).setText(mail);
    }

    @Override
    public void showUserPhoto(String photoUrl) {
        ImageView userImage = (ImageView) headerLayout.findViewById(R.id.user_image);

        if(photoUrl.isEmpty()) {
            userImage.setImageResource(R.mipmap.ic_site_type);
        }
        else {
            Glide.with(this)
                    .load(photoUrl)
                    .asBitmap()
                    .into(userImage);
        }
    }

    @Override
    public void showSessionState(int stateResource) {
        navigationView.getMenu().getItem(0).setTitle(getString(stateResource));
    }

    @Override
    public void onUserLogout() {
        navigator.openActivity(LoginActivity.class);
    }

    ///////////////////////////Navigation Management////////////////////////////////
    private void initializeFragment(Bundle inputState) {
        FragmentType startingFragment = navigator.getFragmentFilter();

        if(inputState != null) {
            startingFragment = (FragmentType) inputState.getSerializable(CURRENT_FRAGMENT_KEY);
        }
        openFragment(startingFragment, null);
    }

    public void openFragment(@NonNull FragmentType fragmentType,
                             @Nullable Bundle arguments) {
        navigator.openFragment(fragmentType, arguments);
    }

    public void openActivity(@NonNull Class<?> className){
        navigator.openActivity(className);
    }
}