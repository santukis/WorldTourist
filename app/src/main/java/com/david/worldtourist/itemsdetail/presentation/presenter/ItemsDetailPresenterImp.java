package com.david.worldtourist.itemsdetail.presentation.presenter;

import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.ItemCategory;
import com.david.worldtourist.items.domain.model.ItemUserFilter;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailPresenter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailView;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.rating.domain.model.Rating;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.message.domain.usecase.GetMessages;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.items.domain.model.Item;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;
import com.david.worldtourist.report.domain.model.Report;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;

import java.util.ArrayList;
import java.util.List;

public class ItemsDetailPresenterImp extends CachedPresenterImp<ItemDetailView>
        implements ItemDetailPresenter<ItemDetailView> {

    private ItemDetailView view;

    private final UseCaseHandler useCaseHandler;

    private final GetItemCategory getItemCategory;
    private final GetUser getUser;
    private final GetItem getItem;
    private final GetItemFilter getItemFilter;
    private final SaveItem saveItem;
    private final DeleteItems deleteItems;
    private final GetMessages getMessages;
    private final EditMessage editMessage;
    private final GetRatingValue getRatingValue;
    private final UpdateRating deleteItemRating;
    private final ClearConnection clearMessageConnection;
    private final ClearConnection clearRatingConnection;
    private final IsNetworkAvailable isNetworkAvailable;

    public ItemsDetailPresenterImp(UseCaseHandler useCaseHandler,
                                   GetItemCategory getItemCategory,
                                   CacheMapCoordinates cacheMapCoordinates,
                                   CacheMessage cacheMessage,
                                   CacheReport cacheReport,
                                   GetUser getUser,
                                   GetItem getItem,
                                   GetItemFilter getItemFilter,
                                   SaveItem saveItem,
                                   DeleteItems deleteItems,
                                   GetMessages getMessages,
                                   EditMessage editMessage,
                                   GetRatingValue getRatingValue,
                                   UpdateRating deleteItemRating,
                                   ClearConnection clearMessageConnection,
                                   ClearConnection clearRatingConnection,
                                   IsNetworkAvailable isNetworkAvailable) {

        super(null, getItemCategory, null, cacheMapCoordinates, null, cacheMessage, cacheReport);

        this.useCaseHandler = useCaseHandler;
        this.getItemCategory = getItemCategory;
        this.getUser = getUser;
        this.getItem = getItem;
        this.getItemFilter = getItemFilter;
        this.saveItem = saveItem;
        this.deleteItems = deleteItems;
        this.getMessages = getMessages;
        this.editMessage = editMessage;
        this.getRatingValue = getRatingValue;
        this.deleteItemRating = deleteItemRating;
        this.clearMessageConnection = clearMessageConnection;
        this.clearRatingConnection = clearRatingConnection;
        this.isNetworkAvailable = isNetworkAvailable;
    }

    /////////////////////////////BasePresenter implementation//////////////////////////////
    @Override
    public void setView(@NonNull ItemDetailView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
    }

    @Override
    public void onStart() {
        loadItem();
        Item item = getItem.executeSync().getItem();
        loadItemRating(item);
        loadItemMessages(item);
    }

    @Override
    public void onStop() {
        useCaseHandler.shutdown();
        clearRemoteConnections();
    }

    @Override
    public void onDestroy() {
        clearRemoteConnections();
    }


    /////////////////////////CachePresenter implementation//////////////////////////
    @Override
    public void cacheMapCoordinates(GeoCoordinate coordinate) {
        super.cacheMapCoordinates(coordinate);
        view.loadView(FragmentType.ITEMS_MAP);
    }

    /////////////////////////ItemsDetailPresenter implementation//////////////////////////
    @Override
    public User loadUser() {
        return getUser.executeSync().getUser();
    }

    @Override
    public void shareItem() {
        Item item = getItem.executeSync().getItem();
        view.share(item.getName(), item.getInfo());
    }

    @Override
    public void loadItem() {

        if(!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        view.showLoadingBar();

        GetItem.RequestValues requestValues =
                new GetItem.RequestValues(
                        getItemCategory.executeSync().getItemCategory(),
                        getItem.executeSync().getItem().getId(),
                        getItem.executeSync().getItem().getType());

        useCaseHandler.execute(getItem, requestValues,
                new UseCase.Callback<GetItem.ResponseValues>() {
                    @Override
                    public void onSuccess(GetItem.ResponseValues response) {
                        view.hideLoadingBar();
                        getItemUserFilter(response.getItem());
                        showItem(response.getItem());
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                        view.closeView();
                    }
                });
    }

    @Override
    public void editMessage(final Message message, final MessageActionFilter messageAction) {
        User user = getUser.executeSync().getUser();

        if(user != User.EMPTY_USER) {
            message.setItemId(getItem.executeSync().getItem().getId());
            cacheMessage(message, messageAction);
            view.loadView(FragmentType.WRITE_MESSAGE);

        } else {
            view.showDialogMessage(FragmentView.UNREGISTER_USER);
        }
    }

    @Override
    public void onMessageMenuClick(final Message message){
        User user = getUser.executeSync().getUser();

        if(user != User.EMPTY_USER) {
            String userId = user.getId();
            boolean isUserMessage = userId.equals(message.getUser().getId());
            final boolean isUserInUserLikesList =
                    message.getUserLikesIds().contains(userId);

            view.setupMessageMenu(isUserMessage, isUserInUserLikesList);
            view.showMessageMenu(message, user);

        } else {
            view.showDialogMessage(FragmentView.UNREGISTER_USER);
        }
    }

    @Override
    public void deleteMessage(Message message) {
        EditMessage.RequestValues deleteMessageValues = new EditMessage.RequestValues(
                message, MessageActionFilter.DELETE);

        useCaseHandler.execute(editMessage, deleteMessageValues,
                new UseCase.Callback<EditMessage.ResponseValues>() {
                    @Override
                    public void onSuccess(EditMessage.ResponseValues response) {
                        view.showToastMessage(
                                R.string.message_deleted,
                                android.R.drawable.ic_delete);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

        Rating currentRating = new Rating(getItem.executeSync().getItem().getId(),
                -1, -message.getUserRating());
        UpdateRating.RequestValues deleteRatingValues = new UpdateRating.RequestValues(currentRating);

        useCaseHandler.execute(deleteItemRating, deleteRatingValues,
                new UseCase.Callback<UpdateRating.ResponseValues>() {
                    @Override
                    public void onSuccess(UpdateRating.ResponseValues response) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void addLikeToMessage(Message message, String userId) {
        message.getUserLikesIds().add(userId);
        message.setLikes(message.getLikes() + 1);

        EditMessage.RequestValues requestValues =
                new EditMessage.RequestValues(message, MessageActionFilter.UPDATE);
        useCaseHandler.execute(editMessage, requestValues,
                new UseCase.Callback<EditMessage.ResponseValues>() {
                    @Override
                    public void onSuccess(EditMessage.ResponseValues response) {
                        view.showToastMessage(
                                R.string.add_one,
                                android.R.drawable.ic_input_add);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void removeLikeToMessage(Message message, String userId) {
        message.getUserLikesIds().remove(message.getUserLikesIds().indexOf(userId));
        message.setLikes(message.getLikes() - 1);

        EditMessage.RequestValues requestValues =
                new EditMessage.RequestValues(message, MessageActionFilter.UPDATE);
        useCaseHandler.execute(editMessage, requestValues,
                new UseCase.Callback<EditMessage.ResponseValues>() {
                    @Override
                    public void onSuccess(EditMessage.ResponseValues response) {
                        view.showToastMessage(
                                R.string.remove_one,
                                android.R.drawable.ic_delete);
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void updateItemUserFilter(int itemFilter, boolean isChecked) {
        Item item = getItem.executeSync().getItem();

        switch (itemFilter) {
            case 0: //IsFavourite
                if (isChecked) {
                    if (item.getFilter() == ItemUserFilter.TO_VISIT)
                        item.setFilter(ItemUserFilter.ALL);
                    else
                        item.setFilter(ItemUserFilter.FAVOURITE);
                } else {
                    if (item.getFilter() == ItemUserFilter.ALL)
                        item.setFilter(ItemUserFilter.TO_VISIT);
                    else
                        item.setFilter(ItemUserFilter.NONE);
                }
                break;
            case 1: //IsToVisit
                if (isChecked) {
                    if (item.getFilter() == ItemUserFilter.FAVOURITE)
                        item.setFilter(ItemUserFilter.ALL);
                    else
                        item.setFilter(ItemUserFilter.TO_VISIT);
                } else {
                    if (item.getFilter() == ItemUserFilter.ALL)
                        item.setFilter(ItemUserFilter.FAVOURITE);
                    else
                        item.setFilter(ItemUserFilter.NONE);
                }
                break;
        }
    }

    @Override
    public void updateItemUserLists() {
        Item item = getItem.executeSync().getItem();

        if (item.getFilter() != ItemUserFilter.NONE) {
            saveItem(item, getItemCategory.executeSync().getItemCategory());
        } else {
            deleteItem(item);
        }
    }

    @Override
    public void editReport(User user, Message message){
        Report report = new Report(user, getItem.executeSync().getItem(), message);
        cacheReport(report);
    }

    private void loadItemRating(Item item) {
        GetRatingValue.RequestValues requestValues = new GetRatingValue.RequestValues(item.getId());
        useCaseHandler.execute(getRatingValue, requestValues,
                new UseCase.Callback<GetRatingValue.ResponseValues>() {
                    @Override
                    public void onSuccess(GetRatingValue.ResponseValues response) {
                        view.showItemRating(response.getRating());
                    }

                    @Override
                    public void onError(String error) {
                        view.showItemRating(Rating.EMPTY_RATING);
                    }
                });
    }

    private void loadItemMessages(Item item) {
        GetMessages.RequestValues requestValues = new GetMessages.RequestValues(item.getId());
        useCaseHandler.execute(getMessages, requestValues,
                new UseCase.Callback<GetMessages.ResponseValues>() {
                    @Override
                    public void onSuccess(GetMessages.ResponseValues response) {
                        switch (response.getMessageAction()) {
                            case CREATE:
                                view.addMessage(response.getMessage());
                                break;
                            case UPDATE:
                                view.updateMessage(response.getMessage());
                                break;
                            case DELETE:
                                view.deleteMessage(response.getMessage());
                                break;
                        }
                    }

                    @Override
                    public void onError(String error) {
                        view.showToastMessage(
                                R.string.message_not_updated,
                                android.R.drawable.ic_menu_send);
                    }
                });
    }

    private void saveItem(final Item item, ItemCategory itemCategory) {
        SaveItem.RequestValues requestValues = new SaveItem.RequestValues(item, itemCategory);
        useCaseHandler.execute(saveItem, requestValues,
                new UseCase.Callback<SaveItem.ResponseValues>() {
                    @Override
                    public void onSuccess(SaveItem.ResponseValues response) {
                        view.showUserListIcon(true);
                        view.setupUserListListener(item.getName(), isItemFavourite(item), isItemToVisit(item));
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private void deleteItem(final Item item) {
        List<String> itemIds = new ArrayList<>();
        itemIds.add(item.getId());;

        DeleteItems.RequestValues requestValues = new DeleteItems.RequestValues(itemIds);
        useCaseHandler.execute(deleteItems, requestValues,
                new UseCase.Callback<DeleteItems.ResponseValues>() {
                    @Override
                    public void onSuccess(DeleteItems.ResponseValues response) {
                        view.showUserListIcon(false);
                        view.setupUserListListener(item.getName(), isItemFavourite(item), isItemToVisit(item));
                    }

                    @Override
                    public void onError(String error) {
                    }
                });
    }

    private void getItemUserFilter(final Item item) {
        GetItemFilter.RequestValues requestValues = new GetItemFilter.RequestValues(item.getId());
        useCaseHandler.execute(getItemFilter, requestValues,
                new UseCase.Callback<GetItemFilter.ResponseValues>() {
                    @Override
                    public void onSuccess(GetItemFilter.ResponseValues response) {
                        item.setFilter(response.getItemFilter());
                        view.showUserListIcon(isItemFavourite(item) || isItemToVisit(item));
                        view.setupUserListListener(item.getName(), isItemFavourite(item), isItemToVisit(item));
                    }

                    @Override
                    public void onError(String error) {
                        view.showUserListIcon(isItemFavourite(item) || isItemToVisit(item));
                        view.setupUserListListener(item.getName(), isItemFavourite(item), isItemToVisit(item));
                    }
                });
    }

    private boolean isItemFavourite(Item item) {
        return item.getFilter() == ItemUserFilter.FAVOURITE ||
                item.getFilter() == ItemUserFilter.ALL;
    }

    private boolean isItemToVisit(Item item) {
        return item.getFilter() == ItemUserFilter.TO_VISIT ||
                item.getFilter() == ItemUserFilter.ALL;
    }

    private void showItem(Item item) {
        view.showItemName(item.getName());
        view.showItemIcon(item.getType());
        view.showItemAddress(item.getAddress(), item.getCity().toUpperCase());
        view.showItemTimetable(item.getTimetable());
        view.showItemDate(item.getStartDate(), item.getEndDate());
        view.showItemTime(item.getStartDate());
        view.showItemTicket(item.getTicket());
        view.showItemPhone(item.getPhone());
        view.setupPhoneListener(item.getPhone());
        view.setupMapListener(item.getCoordinate());
        view.setupInfoListener(item.getInfo());
        view.setupPanoramicListener(item.getCoordinate());
        view.showItemDescription(item.getDescription());
        view.showItemPhotos(item.getPhotos());
        view.showDotsLayout(item.getPhotos().size());
        view.setupWriteMessageListener();
        view.setupAddPhotosListener();
    }

    private void clearRemoteConnections() {
        Item item = getItem.executeSync().getItem();

        clearMessageConnection.execute(new ClearConnection.RequestValues(item.getId()));
        clearRatingConnection.execute(new ClearConnection.RequestValues(item.getId()));
    }
}