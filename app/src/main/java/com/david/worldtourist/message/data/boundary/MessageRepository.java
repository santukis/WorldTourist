package com.david.worldtourist.message.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.domain.usecase.GetMessage;
import com.david.worldtourist.message.domain.usecase.GetMessages;

public interface MessageRepository {

    void loadMessages(@NonNull GetMessages.RequestValues requestValues,
                      @NonNull final UseCase.Callback<GetMessages.ResponseValues> callback);

    void editMessage(@NonNull EditMessage.RequestValues requestValues,
                     @NonNull final UseCase.Callback<EditMessage.ResponseValues> callback);

    void clearConnection(@NonNull ClearConnection.RequestValues requestValues);

    void cacheMessage(@NonNull CacheMessage.RequestValues requestValues);

    GetMessage.ResponseValues getMessage();
}
