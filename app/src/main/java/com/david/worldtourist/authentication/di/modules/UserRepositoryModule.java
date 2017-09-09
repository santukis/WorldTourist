package com.david.worldtourist.authentication.di.modules;

import android.content.Context;

import com.david.worldtourist.common.di.qualifiers.Local;
import com.david.worldtourist.common.di.qualifiers.Remote;
import com.david.worldtourist.authentication.data.boundary.UserDataSource;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.data.local.UserPreferenceSource;
import com.david.worldtourist.authentication.data.remote.UserFirebaseSource;
import com.david.worldtourist.authentication.data.repository.UserRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module
public class UserRepositoryModule {

    @Provides
    public UserRepository userRepository(@Local UserDataSource localDataSource, @Remote UserDataSource remoteDataSource){
        return UserRepositoryImp.getInstance(localDataSource, remoteDataSource);
    }

    @Local
    @Provides
    public UserDataSource localDataSource(Context context) {
        return UserPreferenceSource.getInstance(context);
    }

    @Remote
    @Provides
    public UserDataSource remoteDataSource() {
        return UserFirebaseSource.getInstance();
    }
}
