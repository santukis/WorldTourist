package com.david.worldtourist.message.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;

import javax.inject.Inject;

public class EditMessage extends UseCase<EditMessage.RequestValues, EditMessage.ResponseValues> {

    private final MessageRepository repository;

    @Inject
    public EditMessage(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

        repository.editMessage(requestValues, new Callback<ResponseValues>() {
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
        private Message message;
        private MessageActionFilter messageAction;

        public RequestValues(Message message, MessageActionFilter actionFilter) {
            this.message = message;
            messageAction = actionFilter;
        }

        public Message getMessage() {
            return message;
        }

        public MessageActionFilter getMessageAction() {
            return messageAction;
        }

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private MessageActionFilter messageAction = MessageActionFilter.NONE;

        public ResponseValues(MessageActionFilter messageActionFilter){
            messageAction = messageActionFilter;
        }

        public MessageActionFilter getMessageAction(){
            return messageAction;
        }
    }
}
