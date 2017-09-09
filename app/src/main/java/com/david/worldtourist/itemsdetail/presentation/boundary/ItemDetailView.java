package com.david.worldtourist.itemsdetail.presentation.boundary;

import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.ItemDate;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.items.domain.model.Ticket;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.rating.domain.model.Rating;

import java.util.List;

public interface ItemDetailView extends FragmentView{

    void share(String itemName, String itemInfo);

    void showItemName(String itemName);

    void showItemIcon(ItemType itemType);

    void showItemAddress(String address, String city);

    void showItemTimetable(String[] itemTimetable);

    void showItemDate(ItemDate startDate, ItemDate endDate);

    void showItemTime(ItemDate startDate);

    void showItemTicket(Ticket ticket);

    void showItemPhone(String itemPhone);

    void setupPhoneListener(String itemPhone);

    void showItemDescription(String itemDescription);

    void showItemPhotos(List<Photo> itemPhotos);

    void showDotsLayout(int dotsAmount);

    void showItemRating(Rating itemRating);

    void setupMapListener(GeoCoordinate itemCoordinates);

    void setupInfoListener(String url);

    void setupPanoramicListener(GeoCoordinate itemCoordinates);

    void showUserListIcon(boolean isShown);

    void setupUserListListener(String name, boolean isFavourite, boolean isToVisit);

    void loadView(FragmentType type);

    void setupMessageMenu(boolean isUserMessage, boolean isUserInUserLikesList);

    void showMessageMenu(Message message, User user);

    void setupWriteMessageListener();

    void setupAddPhotosListener();

    void addMessage(Message message);

    void updateMessage(Message message);

    void deleteMessage(Message message);
}
