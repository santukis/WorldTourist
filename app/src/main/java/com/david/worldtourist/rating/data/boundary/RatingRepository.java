package com.david.worldtourist.rating.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;

public interface RatingRepository {

    void getRating(@NonNull GetRatingValue.RequestValues requestValues,
                   @NonNull UseCase.Callback<GetRatingValue.ResponseValues> callback);

    void updateRating(@NonNull UpdateRating.RequestValues requestValues);

    void clearRatingConnection(@NonNull ClearConnection.RequestValues requestValues);
}
