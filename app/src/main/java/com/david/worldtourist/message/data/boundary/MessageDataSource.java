package com.david.worldtourist.message.data.boundary;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.domain.usecase.GetMessages;

public interface MessageDataSource {

    interface Remote {

        void editMessage(@NonNull EditMessage.RequestValues requestValues,
                         @NonNull UseCase.Callback<EditMessage.ResponseValues> callback);

        void getMessage(@NonNull GetMessages.RequestValues requestValues,
                        @NonNull UseCase.Callback<GetMessages.ResponseValues> callback);

        void clearMessageConnection(@NonNull ClearConnection.RequestValues requestValues);
    }


    interface Local{

        void saveMessageId(@NonNull String itemId);

        void deleteMessageId(@NonNull String itemId);
    }

}
