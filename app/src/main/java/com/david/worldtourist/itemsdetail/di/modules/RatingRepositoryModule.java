package com.david.worldtourist.itemsdetail.di.modules;

import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.rating.data.boundary.RatingDataSource;
import com.david.worldtourist.rating.data.boundary.RatingRepository;
import com.david.worldtourist.rating.data.remote.RatingFirebaseDataSource;
import com.david.worldtourist.rating.data.repository.RatingRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module
public class RatingRepositoryModule {

    @Provides
    @FragmentScope
    public RatingRepository ratingRepository(RatingDataSource ratingDataSource) {
        return RatingRepositoryImp.getInstance(ratingDataSource);
    }

    @Provides
    @FragmentScope
    public RatingDataSource ratingDataSource () {
        return RatingFirebaseDataSource.getInstance();
    }
}
