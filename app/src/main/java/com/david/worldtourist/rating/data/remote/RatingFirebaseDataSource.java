package com.david.worldtourist.rating.data.remote;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.rating.data.boundary.RatingDataSource;
import com.david.worldtourist.rating.domain.model.Rating;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;
import com.david.worldtourist.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RatingFirebaseDataSource implements RatingDataSource {

    private static RatingFirebaseDataSource INSTANCE = null;

    private static final String RATING_CHILD = "ratings";

    private DatabaseReference ratingReference;

    private ValueEventListener ratingListener;

    private RatingFirebaseDataSource() {
        ratingReference = FirebaseDatabase.getInstance().getReference().child(RATING_CHILD);
    }

    public static RatingFirebaseDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RatingFirebaseDataSource();
        }
        return INSTANCE;
    }

    /////////////////////////RatingRepository implementation///////////////////////////

    @Override
    public void getRating(String itemId, final UseCase.Callback<GetRatingValue.ResponseValues> callback) {
        ratingListener = ratingReference
                .child(itemId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Rating rating = dataSnapshot.getValue(Rating.class);
                            callback.onSuccess(new GetRatingValue.ResponseValues(rating));

                        } else {
                            callback.onError(Constants.EMPTY_OBJECT_ERROR);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void updateRating(final Rating rating) {
        ratingReference
                .child(rating.getItemId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Rating newRating = dataSnapshot.getValue(Rating.class);
                            newRating.setRatingValue(newRating.getRatingValue() + rating.getRatingValue());
                            newRating.setRatingSize(newRating.getRatingSize() + rating.getRatingSize());

                            if (newRating.getRatingSize() > 0) {
                                Map<String, Object> itemRated = new HashMap<>();
                                itemRated.put(rating.getItemId(), newRating);
                                ratingReference.updateChildren(itemRated);

                            } else {
                                ratingReference.child(newRating.getItemId()).removeValue();
                            }
                        } else {
                            ratingReference.child(rating.getItemId()).setValue(rating);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void clearConnection(String itemId) {
        if (ratingListener != null) {
            ratingReference
                    .child(itemId)
                    .removeEventListener(ratingListener);
            ratingListener = null;
        }
    }
}
