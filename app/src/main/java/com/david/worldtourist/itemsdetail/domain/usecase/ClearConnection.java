package com.david.worldtourist.itemsdetail.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.rating.data.boundary.RatingRepository;

public class ClearConnection extends UseCase<ClearConnection.RequestValues,
        ClearConnection.ResponseValues> {

    private final MessageRepository messageRepository;
    private final RatingRepository ratingRepository;

    public ClearConnection(MessageRepository repository){
        messageRepository = repository;
        ratingRepository = null;
    }

    public ClearConnection(RatingRepository repository) {
        ratingRepository = repository;
        messageRepository = null;
    }

    @Override
    public void execute(RequestValues requestValues) {
        if(messageRepository != null) {
            messageRepository.clearConnection(requestValues);

        } else if (ratingRepository != null) {
            ratingRepository.clearRatingConnection(requestValues);
        }
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues{
        private String itemId;

        public RequestValues(String itemId){
            this.itemId = itemId;
        }

        public String getItemId(){
            return itemId;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues{

    }
}
