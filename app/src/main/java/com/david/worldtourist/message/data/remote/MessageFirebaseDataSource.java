package com.david.worldtourist.message.data.remote;

import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.itemsdetail.domain.usecase.ClearConnection;
import com.david.worldtourist.message.data.boundary.MessageDataSource;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.message.domain.usecase.EditMessage;
import com.david.worldtourist.message.domain.usecase.GetMessages;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class MessageFirebaseDataSource implements MessageDataSource.Remote {

    private static MessageFirebaseDataSource INSTANCE = null;
    
    private static final String MESSAGE_CHILD = "messages";
    private static final int ITEMS_DOWNLOADED_LIMIT = 10;

    private DatabaseReference messageReference;

    private ChildEventListener messageListener;

    private MessageFirebaseDataSource() {
        messageReference = FirebaseDatabase.getInstance().getReference().child(MESSAGE_CHILD);
    }

    public static MessageFirebaseDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageFirebaseDataSource();
        }
        return INSTANCE;
    }

    /////////////////////////MessageDataSource implementation///////////////////////////
    @Override
    public void editMessage(@NonNull EditMessage.RequestValues requestValues,
                            @NonNull final UseCase.Callback<EditMessage.ResponseValues> callback) {

        MessageActionFilter actionFilter = requestValues.getMessageAction();
        Message message = requestValues.getMessage();

        DatabaseReference messageReference = this.messageReference.child(
                requestValues.getMessage().getItemId());

        switch (actionFilter) {
            case CREATE:
                messageReference.push().setValue(message, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            callback.onError(databaseError.getMessage());
                        } else {
                            callback.onSuccess(
                                    new EditMessage.ResponseValues(MessageActionFilter.CREATE));
                        }
                    }
                });
                break;

            case UPDATE:
                Map<String, Object> userMessage = new HashMap<>();
                userMessage.put(message.getMessageId(), message);
                messageReference.updateChildren(userMessage, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            callback.onError(databaseError.getMessage());
                        } else {
                            callback.onSuccess(
                                    new EditMessage.ResponseValues(MessageActionFilter.UPDATE));
                        }
                    }
                });
                break;

            case DELETE:
                messageReference.child(message.getMessageId()).removeValue(
                        new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError,
                                                   DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    callback.onError(databaseError.getMessage());
                                } else {
                                    callback.onSuccess(
                                            new EditMessage.ResponseValues(MessageActionFilter.DELETE));
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void getMessage(@NonNull GetMessages.RequestValues requestValues,
                           @NonNull final UseCase.Callback<GetMessages.ResponseValues> callback) {

        String itemId = requestValues.getItemId();

        messageListener = messageReference
                .child(itemId)
                .limitToLast(ITEMS_DOWNLOADED_LIMIT)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.hasChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            message.setMessageId(dataSnapshot.getKey());
                            callback.onSuccess(
                                    new GetMessages.ResponseValues(message, MessageActionFilter.CREATE));
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setMessageId(dataSnapshot.getKey());
                        callback.onSuccess(
                                new GetMessages.ResponseValues(message, MessageActionFilter.UPDATE));
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setMessageId(dataSnapshot.getKey());
                        callback.onSuccess(
                                new GetMessages.ResponseValues(message, MessageActionFilter.DELETE));
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void clearMessageConnection(@NonNull ClearConnection.RequestValues requestValues) {
        if (messageListener != null) {
            messageReference
                    .child(requestValues.getItemId())
                    .removeEventListener(messageListener);
            messageListener = null;
        }
    }
}
