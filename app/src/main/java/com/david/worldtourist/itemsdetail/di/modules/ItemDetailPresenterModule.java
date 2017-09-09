package com.david.worldtourist.itemsdetail.di.modules;

import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.items.di.modules.ItemsRepositoryModule;
import com.david.worldtourist.items.domain.usecase.GetItemCategory;
import com.david.worldtourist.itemsdetail.di.qualifiers.Message;
import com.david.worldtourist.itemsdetail.di.qualifiers.Rating;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItemFilter;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailPresenter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailView;
import com.david.worldtourist.itemsdetail.presentation.presenter.ItemsDetailPresenterImp;
import com.david.worldtourist.itemsmap.di.modules.ItemsMapRepositoryModule;
import com.david.worldtourist.itemsmap.domain.usecase.CacheMapCoordinates;
import com.david.worldtourist.authentication.di.modules.UserRepositoryModule;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.di.modules.MessageRepositoryModule;
import com.david.worldtourist.message.domain.usecase.CacheMessage;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.domain.usecase.GetMessages;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.permissions.domain.usecase.IsNetworkAvailable;
import com.david.worldtourist.preferences.di.modules.PreferenceRepositoryModule;
import com.david.worldtourist.rating.data.boundary.RatingRepository;
import com.david.worldtourist.rating.domain.usecase.GetRatingValue;
import com.david.worldtourist.rating.domain.usecase.UpdateRating;
import com.david.worldtourist.report.di.modules.ReportRepositoryModule;
import com.david.worldtourist.itemsdetail.domain.usecase.CacheReport;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ItemsRepositoryModule.class,
        ItemsMapRepositoryModule.class,
        MessageRepositoryModule.class,
        RatingRepositoryModule.class,
        UserRepositoryModule.class,
        ReportRepositoryModule.class,
        PermissionControllerModule.class,
        PreferenceRepositoryModule.class})
public class ItemDetailPresenterModule {

    @Provides
    public ItemDetailPresenter<ItemDetailView> itemDetailPresenter(UseCaseHandler useCaseHandler,
                                                                   GetItemCategory getItemCategory,
                                                                   CacheMapCoordinates cacheMapCoordinates,
                                                                   CacheMessage cacheMessage,
                                                                   CacheReport cacheReport,
                                                                   GetUser getUser,
                                                                   GetItem getItem,
                                                                   GetItemFilter getItemFilter,
                                                                   SaveItem saveItem,
                                                                   DeleteItems deleteItems,
                                                                   GetMessages getMessages,
                                                                   EditMessage updateMessage,
                                                                   GetRatingValue getRatingValue,
                                                                   UpdateRating deleteItemRating,
                                                                   @Message ClearConnection clearMessageConnection,
                                                                   @Rating ClearConnection clearRatingConnection,
                                                                   IsNetworkAvailable isNetworkAvailable) {
        return new ItemsDetailPresenterImp(useCaseHandler,
                getItemCategory,
                cacheMapCoordinates,
                cacheMessage,
                cacheReport,
                getUser,
                getItem,
                getItemFilter,
                saveItem,
                deleteItems,
                getMessages,
                updateMessage,
                getRatingValue,
                deleteItemRating,
                clearMessageConnection,
                clearRatingConnection,
                isNetworkAvailable);
    }

    @Provides
    @Message
    public ClearConnection clearMessageConnection(MessageRepository messageRepository) {
        return new ClearConnection(messageRepository);
    }

    @Provides
    @Rating
    public ClearConnection clearRatingConnection(RatingRepository ratingRepository) {
        return new ClearConnection(ratingRepository);
    }


}
