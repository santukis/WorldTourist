package com.david.worldtourist.message.presentation.presenter;


import android.support.annotation.NonNull;

import com.david.worldtourist.R;
import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.common.presentation.presenter.CachedPresenterImp;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.usecase.GetMessage;
import com.david.worldtourist.message.presentation.boundary.MessageView;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.presentation.boundary.MessagePresenter;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.rating.domain.model.Rating;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;

public class MessagePresenterImp extends CachedPresenterImp<MessageView>
        implements MessagePresenter<MessageView> {

    private MessageView view;

    private final UseCaseHandler useCaseHandler;
    private final GetMessage getMessage;
    private final GetUser getUser;
    private final EditMessage editMessage;
    private final UpdateRating updateRating;
    private final IsNetworkAvailable isNetworkAvailable;

    private float previousRatingValue;

    public MessagePresenterImp(UseCaseHandler useCaseHandler,
                               CacheMessage cacheMessage,
                               GetMessage getMessage,
                               GetUser getUser,
                               EditMessage editMessage,
                               UpdateRating updateRating,
                               IsNetworkAvailable isNetworkAvailable) {

        super(null, null, null, null, null, cacheMessage, null);

        this.useCaseHandler = useCaseHandler;
        this.getMessage = getMessage;
        this.getUser = getUser;
        this.editMessage = editMessage;
        this.updateRating = updateRating;
        this.isNetworkAvailable = isNetworkAvailable;
    }

    ///////////////////////BasePresenter implementation////////////////////////

    @Override
    public void setView(@NonNull MessageView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        view.initializeViewComponents();
        view.initializeViewListeners();
    }

    @Override
    public void onStart() {
        Message message = getMessage.executeSync().getMessage();
        User user = getUser.executeSync().getUser();

        previousRatingValue = message.getUserRating();
        message.setUser(user);

        view.showUserName(message.getUser().getName());
        view.showUserPhoto(message.getUser().getImage());
        view.showMessageRating(message.getUserRating());
        view.showMessageText(message.getText());
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        cacheMessage(null, MessageActionFilter.NONE);
    }

    //////////////////////MessagePresenter implementation////////////////////////
    @Override
    public void sendMessage(String messageText, float messageRating) {

        if(!isNetworkAvailable.executeSync().isAvailable()) {
            view.showDialogMessage(FragmentView.NETWORK_ACTIVATION);
            return;
        }

        if (getUser.executeSync().getUser() == User.EMPTY_USER) {
            view.showDialogMessage(FragmentView.UNREGISTER_USER);
            return;
        }

        view.disableButtons();
        view.showLoadingBar();

        Message message = getMessage.executeSync().getMessage();
        MessageActionFilter messageAction = getMessage.executeSync().getMessageAction();

        message.setDate(System.currentTimeMillis());
        message.setText(messageText);
        message.setUserRating(messageRating);

        EditMessage.RequestValues requestValues =
                new EditMessage.RequestValues(message, messageAction);

        useCaseHandler.execute(editMessage, requestValues,
                new UseCase.Callback<EditMessage.ResponseValues>() {
                    @Override
                    public void onSuccess(EditMessage.ResponseValues response) {
                        view.hideLoadingBar();
                        switch (response.getMessageAction()) {
                            case CREATE:
                                view.showToastMessage(
                                        R.string.message_created,
                                        android.R.drawable.ic_input_add);
                                break;
                            case UPDATE:
                                view.showToastMessage(
                                        R.string.message_updated,
                                        android.R.drawable.ic_popup_sync);
                                break;
                        }

                        view.closeView();
                    }

                    @Override
                    public void onError(String error) {
                        view.hideLoadingBar();
                        view.enableButtons();
                        view.showToastMessage(
                                R.string.message_not_updated,
                                android.R.drawable.ic_dialog_alert);
                    }
                });

        updateRating(message, messageAction);
    }

    private void updateRating(Message message, MessageActionFilter messageAction) {

        Rating currentRating = new Rating(message.getItemId());

        switch (messageAction) {
            case CREATE:
                currentRating.setRatingSize(1);
                currentRating.setRatingValue(message.getUserRating());
                break;
            case UPDATE:
                currentRating.setRatingValue(message.getUserRating() - previousRatingValue);
                break;
        }

        updateRating.execute(new UpdateRating.RequestValues(currentRating));
    }
}
