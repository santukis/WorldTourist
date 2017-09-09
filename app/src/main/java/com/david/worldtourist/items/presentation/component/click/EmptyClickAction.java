package com.david.worldtourist.items.presentation.component.click;


import android.view.View;

import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.presentation.component.MessageClick;

public class EmptyClickAction implements ItemClick, MessageClick, ItemLongClick {

    @Override
    public void onItemClick(Item item) {

    }

    @Override
    public void onMessageClick(View view, Message message) {

    }

    @Override
    public void onItemLongClick(String itemId) {

    }
}
