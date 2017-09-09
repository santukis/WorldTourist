package com.david.worldtourist.common.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.david.worldtourist.R;
import com.david.worldtourist.authentication.presentation.view.LoginActivity;

public class SplashActivity extends Activity {


    private final int ANIMATION_DURATION = 3000;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.up_translation);
        animation.setDuration(400);

        relativeLayout.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                SplashActivity.this.finish();
            }
        }, ANIMATION_DURATION);
    }

    @Override
    protected void onResume(){
        super.onResume();

    }
}
