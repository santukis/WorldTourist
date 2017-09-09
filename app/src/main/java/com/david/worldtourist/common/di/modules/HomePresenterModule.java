package com.david.worldtourist.common.di.modules;


import com.david.worldtourist.common.di.scopes.ActivityScope;
import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.common.presentation.boundary.ActivityView;
import com.david.worldtourist.common.presentation.boundary.ActivityPresenter;
import com.david.worldtourist.common.presentation.presenter.ActivityPresenterImp;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.di.modules.UserRepositoryModule;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;

import dagger.Module;
import dagger.Provides;

@Module(includes = {UserRepositoryModule.class})
public class HomePresenterModule {

    @Provides
    @ActivityScope
    public ActivityPresenter<ActivityView> homePresenter(UseCaseHandler useCaseHandler,
                                                         GetUser getUser,
                                                         RemoveUser removeUser){
        return new ActivityPresenterImp(useCaseHandler, getUser, removeUser);
    }

    @Provides
    public GetUser getUser(UserRepository userRepository) {
        return new GetUser(userRepository);
    }

    @Provides
    public RemoveUser removeUser(UserRepository userRepository) {
        return new RemoveUser(userRepository);
    }
}
