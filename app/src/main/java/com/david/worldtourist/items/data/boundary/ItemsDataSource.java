package com.david.worldtourist.items.data.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.items.domain.usecase.GetItems;
import com.david.worldtourist.itemsdetail.domain.usecase.GetItem;
import com.david.worldtourist.itemsdetail.domain.usecase.SaveItem;
import com.david.worldtourist.useritems.domain.usecases.DeleteItems;
import com.david.worldtourist.useritems.domain.usecases.GetUserItems;

public interface ItemsDataSource {

    interface Remote {

        void getItem(@NonNull GetItem.RequestValues requestValues,
                     @NonNull UseCase.Callback<GetItem.ResponseValues> callback);

        void getItems(@NonNull GetItems.RequestValues requestValues,
                      @NonNull UseCase.Callback<GetItems.ResponseValues> callback);
    }

    interface Local {

        void getItem(@NonNull GetItem.RequestValues requestValues,
                     @NonNull UseCase.Callback<GetItem.ResponseValues> callback);

        void getUserItems(@NonNull GetUserItems.RequestValues requestValues,
                          @NonNull UseCase.Callback<GetUserItems.ResponseValues> callback);

        void saveItem(@NonNull SaveItem.RequestValues requestValues,
                      @NonNull UseCase.Callback<SaveItem.ResponseValues> callback);

        void deleteItems(@NonNull DeleteItems.RequestValues requestValues,
                         @NonNull UseCase.Callback<DeleteItems.ResponseValues> callback);

    }
}


