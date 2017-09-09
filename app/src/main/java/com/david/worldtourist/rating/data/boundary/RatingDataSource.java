package com.david.worldtourist.rating.data.boundary;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.rating.domain.model.Rating;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;

public interface RatingDataSource {

    void getRating(String itemId, UseCase.Callback<GetRatingValue.ResponseValues> callback);

    void updateRating(Rating rating);

    void clearConnection(String itemId);

}
