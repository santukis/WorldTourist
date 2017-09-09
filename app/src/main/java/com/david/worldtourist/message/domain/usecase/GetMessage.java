package com.david.worldtourist.message.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;

import javax.inject.Inject;

public class GetMessage extends UseCase<GetMessage.RequestValues, GetMessage.ResponseValues> {

    private final MessageRepository repository;

    @Inject
    public GetMessage(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {

    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return repository.getMessage();
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {
        private Message message;
        private MessageActionFilter messageAction;

        public ResponseValues(Message message, MessageActionFilter messageAction) {
            this.message = message;
            this.messageAction = messageAction;
        }

        public Message getMessage() {
            return this.message;
        }

        public MessageActionFilter getMessageAction() {
            return messageAction;
        }
    }
}
