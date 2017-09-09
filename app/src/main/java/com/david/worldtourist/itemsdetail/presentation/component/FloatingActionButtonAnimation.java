package com.david.worldtourist.itemsdetail.presentation.component;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;


public class FloatingActionButtonAnimation extends FloatingActionButton.Behavior {

    public FloatingActionButtonAnimation(Context context, AttributeSet attrs){
        super();
    }


    /////////////////////CoordinatorLayout.Behavior methods/////////////////////////
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed){
        super.onNestedScroll(coordinatorLayout,child,target,dxConsumed,
                dyConsumed,dxUnconsumed,dyUnconsumed);

        if(dyConsumed > 0){
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                    child.getLayoutParams();
            int fabBottomMargin = layoutParams.bottomMargin;
            child.animate().translationY(child.getHeight() + fabBottomMargin).start();
        } else if(dyConsumed < 0){
            child.animate().translationY(0).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes){
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
