package com.david.worldtourist.message.di.modules;

import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.itemsdetail.di.modules.RatingRepositoryModule;
import com.david.worldtourist.authentication.di.modules.UserRepositoryModule;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.message.domain.usecase.GetMessage;
import com.david.worldtourist.message.presentation.boundary.MessageView;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.presentation.boundary.MessagePresenter;
import com.david.worldtourist.message.presentation.presenter.MessagePresenterImp;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        MessageRepositoryModule.class,
        RatingRepositoryModule.class,
        PermissionControllerModule.class,
        UserRepositoryModule.class})
public class MessagePresenterModule {

    @Provides
    public MessagePresenter<MessageView> presenter(UseCaseHandler useCaseHandler,
                                                   CacheMessage cacheMessage,
                                                   GetMessage getMessage,
                                                   GetUser getUser,
                                                   EditMessage editMessage,
                                                   UpdateRating updateRating,
                                                   IsNetworkAvailable isNetworkAvailable) {

        return new MessagePresenterImp(useCaseHandler, cacheMessage, getMessage,
                getUser, editMessage, updateRating, isNetworkAvailable);
    }
}
