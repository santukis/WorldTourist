package com.david.worldtourist.rating.domain.usecase;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.rating.data.boundary.RatingRepository;
import com.david.worldtourist.rating.domain.model.Rating;

import javax.inject.Inject;

public class UpdateRating extends UseCase<UpdateRating.RequestValues, UpdateRating.ResponseValues> {

    private final RatingRepository repository;

    @Inject
    public UpdateRating(RatingRepository repository){
        this.repository = repository;
    }

    @Override
    public void execute(RequestValues requestValues) {
        repository.updateRating(requestValues);
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Rating rating;

        public RequestValues(@NonNull Rating rating){
            this.rating = rating;
        }

        public Rating getRating(){
            return rating;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
