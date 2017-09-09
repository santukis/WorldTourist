package com.david.worldtourist.rating.domain.usecase;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.rating.data.boundary.RatingRepository;
import com.david.worldtourist.rating.domain.model.Rating;

import javax.inject.Inject;


public class GetRatingValue extends UseCase<GetRatingValue.RequestValues, GetRatingValue.ResponseValues> {


    private final RatingRepository repository;

    @Inject
    public GetRatingValue(RatingRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.getRating(requestValues, new Callback<ResponseValues>() {
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
        private Rating rating;

        public ResponseValues(Rating rating){
            this.rating = rating;
        }

        public Rating getRating(){
            return rating;
        }
    }
}
