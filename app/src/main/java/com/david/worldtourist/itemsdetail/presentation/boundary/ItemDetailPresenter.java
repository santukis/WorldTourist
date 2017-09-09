package com.david.worldtourist.itemsdetail.presentation.boundary;


import com.david.worldtourist.common.presentation.boundary.BaseView;
import com.david.worldtourist.common.presentation.boundary.CachedPresenter;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;

public interface ItemDetailPresenter<T extends BaseView> extends CachedPresenter<T> {

    User loadUser();

    void shareItem();

    void loadItem();

    void editMessage(Message message, MessageActionFilter messageAction);

    void onMessageMenuClick(Message message);

    void deleteMessage(Message message);

    void addLikeToMessage(Message message, String userId);

    void removeLikeToMessage(Message message, String userId);

    void updateItemUserLists();

    void updateItemUserFilter(int itemFilter, boolean isChecked);

    void editReport(User user, Message message);

}
