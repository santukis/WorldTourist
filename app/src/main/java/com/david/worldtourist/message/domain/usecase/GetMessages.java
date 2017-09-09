package com.david.worldtourist.message.domain.usecase;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;

import javax.inject.Inject;

public class GetMessages extends UseCase<GetMessages.RequestValues, GetMessages.ResponseValues>{

    private final MessageRepository repository;

    @Inject
    public GetMessages(MessageRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.loadMessages(requestValues, new Callback<ResponseValues>() {
            @Override
            public void onSuccess(ResponseValues response) {
                getCallback().onSuccess(response);
            }

            @Override
            public void onError(String error) {
                getCallback().onError(error);
            }
        });
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {

        private final String itemId;

        public RequestValues(@NonNull String itemId){
            this.itemId = itemId;
        }

        public String getItemId(){
            return itemId;
        }

    }

    public static class ResponseValues implements UseCase.ResponseValues{

        private Message message;
        private MessageActionFilter messageAction = MessageActionFilter.NONE;

        public ResponseValues(@NonNull Message message,
                              @NonNull MessageActionFilter messageAction){
            this.message = message;
            this.messageAction = messageAction;
        }

        public Message getMessage(){
            return message;
        }

        public MessageActionFilter getMessageAction(){
            return messageAction;
        }
    }
}
