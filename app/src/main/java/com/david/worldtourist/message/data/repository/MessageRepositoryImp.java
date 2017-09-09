package com.david.worldtourist.message.data.repository;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.message.data.boundary.MessageDataSource;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.domain.usecase.GetMessage;
import com.david.worldtourist.message.domain.usecase.GetMessages;

public class MessageRepositoryImp implements MessageRepository{

    private static MessageRepositoryImp INSTANCE = null;

    private final MessageDataSource.Remote remoteMessageStorage;
    private final MessageDataSource.Local localMessageStorage;

    private Message cachedMessage;
    private MessageActionFilter cachedMessageAction;

    private MessageRepositoryImp(MessageDataSource.Remote remoteStorage,
                                 MessageDataSource.Local localStorage) {
        remoteMessageStorage = remoteStorage;
        localMessageStorage = localStorage;
    }

    public static MessageRepositoryImp getInstance(MessageDataSource.Remote remoteSource,
                                                   MessageDataSource.Local localSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepositoryImp(remoteSource, localSource);
        }
        return INSTANCE;
    }

    @Override
    public void loadMessages(@NonNull GetMessages.RequestValues requestValues,
                             @NonNull final UseCase.Callback<GetMessages.ResponseValues> callback) {
        remoteMessageStorage.getMessage(requestValues, callback);
    }

    @Override
    public void editMessage(@NonNull EditMessage.RequestValues requestValues,
                            @NonNull final UseCase.Callback<EditMessage.ResponseValues> callback) {

        remoteMessageStorage.editMessage(requestValues, callback);


        switch (requestValues.getMessageAction()) {
            case CREATE:
                localMessageStorage.saveMessageId(requestValues.getMessage().getMessageId());
                break;

            case DELETE:
                localMessageStorage.deleteMessageId(requestValues.getMessage().getMessageId());
        }

    }

    @Override
    public void clearConnection(@NonNull ClearConnection.RequestValues requestValues) {
        remoteMessageStorage.clearMessageConnection(requestValues);
    }

    @Override
    public void cacheMessage(@NonNull CacheMessage.RequestValues requestValues) {
        cachedMessage = requestValues.getMessage();
        cachedMessageAction = requestValues.getMessageAction();
    }

    @Override
    public GetMessage.ResponseValues getMessage() {
        return new GetMessage.ResponseValues(cachedMessage, cachedMessageAction);
    }
}