package com.david.worldtourist.rating.data.repository;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.rating.data.boundary.RatingDataSource;
import com.david.worldtourist.rating.data.boundary.RatingRepository;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;

public class RatingRepositoryImp implements RatingRepository {

    private static RatingRepositoryImp INSTANCE = null;

    private final RatingDataSource dataSource;

    private RatingRepositoryImp(RatingDataSource dataStorage) {
        dataSource = dataStorage;
    }

    public static RatingRepositoryImp getInstance(RatingDataSource dataStorage) {
        if (INSTANCE == null) {
            INSTANCE = new RatingRepositoryImp(dataStorage);
        }
        return INSTANCE;
    }

    @Override
    public void getRating(@NonNull GetRatingValue.RequestValues requestValues,
                          @NonNull final UseCase.Callback<GetRatingValue.ResponseValues> callback) {

        dataSource.getRating(requestValues.getItemId(), new UseCase.Callback<GetRatingValue.ResponseValues>() {
            @Override
            public void onSuccess(GetRatingValue.ResponseValues response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void updateRating(@NonNull UpdateRating.RequestValues requestValues) {

        dataSource.updateRating(requestValues.getRating());
    }

    @Override
    public void clearRatingConnection(@NonNull ClearConnection.RequestValues requestValues) {
        dataSource.clearConnection(requestValues.getItemId());
    }
}
