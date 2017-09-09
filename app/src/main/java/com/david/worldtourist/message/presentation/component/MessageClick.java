package com.david.worldtourist.message.presentation.component;


import android.view.View;

import com.david.worldtourist.message.domain.model.Message;

public interface MessageClick {

    void onMessageClick(View view, Message message);
}
