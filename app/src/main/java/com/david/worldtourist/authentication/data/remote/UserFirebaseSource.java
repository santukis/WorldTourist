package com.david.worldtourist.authentication.data.remote;


import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.UserDataSource;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.model.UserAttribute;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;
import com.david.worldtourist.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFirebaseSource implements UserDataSource {

    private static UserFirebaseSource INSTANCE = null;

    private final static String USERS_CHILD = "users";
    private static final String DEFAULT_ATTRIBUTE = "";

    private final FirebaseAuth auth;
    private final DatabaseReference usersReference;


    private UserFirebaseSource() {
        auth = FirebaseAuth.getInstance();
        usersReference = FirebaseDatabase.getInstance().getReference().child(USERS_CHILD);
    }

    public static UserFirebaseSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserFirebaseSource();
        }
        return INSTANCE;
    }

    @Override
    public void saveUser(@NonNull SaveUser.RequestValues requestValues,
                         @NonNull final UseCase.Callback<SaveUser.ResponseValues> callback) {

        final User user = requestValues.getUser();
        final DatabaseReference currentUserReference = usersReference.child(user.getId());

        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserReference.setValue(user);
                callback.onSuccess(new SaveUser.ResponseValues());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getUser(@NonNull final UseCase.Callback<GetUser.ResponseValues> callback) {

        if (auth.getCurrentUser() != null) {

            usersReference.child(auth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user;
                            if (dataSnapshot.exists()) {
                                user = dataSnapshot.getValue(User.class);
                            } else {
                                user = new User();
                                user.setId(getUserAttribute(UserAttribute.ID));
                                user.setName(getUserAttribute(UserAttribute.NAME));
                                user.setMail(getUserAttribute(UserAttribute.MAIL));
                                user.setImage(getUserAttribute(UserAttribute.PHOTO));
                                user.setProvider(getUserAttribute(UserAttribute.PROVIDER));
                            }
                            callback.onSuccess(new GetUser.ResponseValues(user));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }
                    });

        } else {
            callback.onError(Constants.EMPTY_OBJECT_ERROR);
        }
    }

    @Override
    public void removeUser(@NonNull RemoveUser.RequestValues requestValues,
                           @NonNull final UseCase.Callback<RemoveUser.ResponseValues> callback) {
        String userId = requestValues.getUserId();

        if (auth.getCurrentUser() != null) {
            auth.signOut();
            usersReference.child(userId).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        callback.onError(databaseError.getMessage());
                    } else {
                        callback.onSuccess(new RemoveUser.ResponseValues());
                    }
                }
            });
        }
    }

    private String getUserAttribute(UserAttribute attribute) {

        switch (attribute) {
            case ID:
                return auth.getCurrentUser() != null ?
                        auth.getCurrentUser().getUid() : DEFAULT_ATTRIBUTE;

            case NAME:
                return auth.getCurrentUser() != null ?
                        auth.getCurrentUser().getDisplayName() : DEFAULT_ATTRIBUTE;

            case MAIL:
                return auth.getCurrentUser() != null ?
                        auth.getCurrentUser().getEmail() : DEFAULT_ATTRIBUTE;

            case PHOTO:
                if (auth.getCurrentUser() != null) {
                    return auth.getCurrentUser().getPhotoUrl() != null ?
                            auth.getCurrentUser().getPhotoUrl().toString() : DEFAULT_ATTRIBUTE;
                }
                break;

            case PROVIDER:
                if (auth.getCurrentUser() != null) {
                    return auth.getCurrentUser().getProviders() != null ?
                            auth.getCurrentUser().getProviders().get(0) : DEFAULT_ATTRIBUTE;
                }
                break;
        }

        return DEFAULT_ATTRIBUTE;
    }
}
