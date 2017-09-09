package com.david.worldtourist.message.di.modules;

import android.content.Context;

import com.david.worldtourist.message.data.boundary.MessageDataSource;
import com.david.worldtourist.message.data.boundary.MessageRepository;
import com.david.worldtourist.message.data.local.MessagesSQLiteDataSource;
import com.david.worldtourist.message.data.remote.MessageFirebaseDataSource;
import com.david.worldtourist.message.data.repository.MessageRepositoryImp;

import dagger.Module;
import dagger.Provides;

@Module()
public class MessageRepositoryModule {


    @Provides
    public MessageRepository messageRepository(MessageDataSource.Remote remoteMessageDataSouce,
                                               MessageDataSource.Local localMessageDataSource) {
        return MessageRepositoryImp.getInstance(remoteMessageDataSouce, localMessageDataSource);
    }

    @Provides
    public MessageDataSource.Remote remoteMessageDataSource() {
        return MessageFirebaseDataSource.getInstance();
    }

    @Provides
    public MessageDataSource.Local localMessageDataSource(Context context) {
        return new MessagesSQLiteDataSource(context);
    }
}
