package com.david.worldtourist.common.presentation.navigation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;

import com.david.worldtourist.R;

import static dagger.internal.Preconditions.checkNotNull;

public class Navigator {

    private static final int ENTER_ANIMATION_DURATION = 500;
    private static final int EXIT_ANIMATION_DURATION = 800;

    private Activity activity;
    private Fragment fragment;

    private FragmentType fragmentType = FragmentType.EVENTS;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public FragmentType getFragmentFilter() {
        return fragmentType;
    }

    public void openActivity(@NonNull Class<?> className) {
        clearBackStackFragments();
        Intent intent = new Intent(activity, className);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public void openFragment(@NonNull FragmentType currentType,
                             @Nullable Bundle arguments) {

        fragmentType = currentType;

        if (isFragmentInBackStack()) {
            getFragmentFromBackStack()
                    .updateFragmentArguments(arguments)
                    .showFragment();

        } else {
            createFragmentFromFilter()
                    .updateFragmentArguments(arguments)
                    .addAnimation()
                    .showFragment();
        }
    }

    public void restoreNextToLastFragment(String fragmentTag) {
        fragmentType = FragmentType.valueOf(fragmentTag);
    }

    private boolean isFragmentInBackStack() {
        return activity
                .getFragmentManager()
                .findFragmentByTag(fragmentType.toString()) != null;
    }

    private Navigator getFragmentFromBackStack() {
        fragment = checkNotNull(activity.getFragmentManager()
                .findFragmentByTag(fragmentType.toString()), "fragment cannot be null");
        return this;
    }

    private Navigator createFragmentFromFilter() {
        FragmentFactory factory = new FragmentFactory();
        fragment = factory.create(fragmentType);
        return this;
    }

    private Navigator updateFragmentArguments(@Nullable Bundle arguments) {
        if (arguments != null) {
            if (fragment.getArguments() != null) {
                fragment.getArguments().putAll(arguments);
            } else {
                fragment.setArguments(arguments);
            }
        }
        return this;
    }

    private Navigator addAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setupFragmentAnimations();

        return this;
    }

    private void showFragment() {
        if(fragment.isVisible()) {
            refreshFragment();
            return;
        }

        activity.getFragmentManager()
                .beginTransaction()
                .replace(determineContainerId(), fragment, fragmentType.toString())
                .addToBackStack(fragmentType.toString())
                .commit();

        fragment = null;
    }

    private void refreshFragment() {
        activity.getFragmentManager()
                .beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit();

        fragment = null;
    }

    private void clearBackStackFragments(){
        activity.getFragmentManager()
                .popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentType = FragmentType.EVENTS;
    }

    private int determineContainerId() {
        int containerId = activity.findViewById(R.id.single_container) != null ?
                R.id.single_container : R.id.left_container;

        switch (fragmentType) {

            case ITEM_DETAIL:
            case PREFERENCES:
            case WRITE_MESSAGE:
            case REPORT:
            case STREET_VIEW:
                return containerId == R.id.single_container ? R.id.single_container :
                        R.id.right_container;

        }
        return containerId;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupFragmentAnimations() {

        Slide enterAnim = new Slide(Gravity.START);
        Slide exitAnim = new Slide(Gravity.START);
        Fade reenterAnim = new Fade(Fade.OUT);

        enterAnim.setDuration(ENTER_ANIMATION_DURATION);
        reenterAnim.setDuration(ENTER_ANIMATION_DURATION);
        exitAnim.setDuration(EXIT_ANIMATION_DURATION);

        fragment.setEnterTransition(enterAnim);
        fragment.setExitTransition(exitAnim);
        fragment.setReturnTransition(reenterAnim);
    }
}