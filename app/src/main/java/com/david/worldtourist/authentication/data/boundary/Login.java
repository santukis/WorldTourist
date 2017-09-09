package com.david.worldtourist.authentication.data.boundary;


import android.content.Intent;

public interface Login {

    void init();

    void login();

    void onResult(int requestCode, int resultCode, Intent data);
}


