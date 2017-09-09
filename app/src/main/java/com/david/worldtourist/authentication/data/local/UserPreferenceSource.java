package com.david.worldtourist.authentication.data.local;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.authentication.data.boundary.UserDataSource;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.RemoveUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;
import com.david.worldtourist.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserPreferenceSource implements UserDataSource {

    private static UserPreferenceSource INSTANCE = null;

    private static final String PREF_WORLDTOURIST = "com.david.worldtourist_internal_user";
    private static final String KEY_USER = "user";

    private static final String DEFAULT_ATTRIBUTE = "";

    private SharedPreferences preferences;

    private UserPreferenceSource(Context context) {
        preferences = context.getSharedPreferences(PREF_WORLDTOURIST, Context.MODE_PRIVATE);
    }

    public static UserPreferenceSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE  = new UserPreferenceSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void saveUser(@NonNull SaveUser.RequestValues requestValues,
                         @NonNull UseCase.Callback<SaveUser.ResponseValues> callback) {
        User user = requestValues.getUser();

        if(user == User.EMPTY_USER) {
            callback.onError(Constants.EMPTY_OBJECT_ERROR);
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(user);
            preferences.edit().putString(KEY_USER, json).apply();
            callback.onSuccess(new SaveUser.ResponseValues());
        }
    }

    @Override
    public void getUser(@NonNull UseCase.Callback<GetUser.ResponseValues> callback) {

        Gson gson = new Gson();
        String json = preferences.getString(KEY_USER, DEFAULT_ATTRIBUTE);

        if(json.isEmpty()) {
            callback.onError(Constants.EMPTY_OBJECT_ERROR);
        } else {
            User user = gson.fromJson(json, new TypeToken<User>() {}.getType());
            callback.onSuccess(new GetUser.ResponseValues(user));
        }
    }

    @Override
    public void removeUser(@NonNull RemoveUser.RequestValues requestValues,
                           @NonNull UseCase.Callback<RemoveUser.ResponseValues> callback) {

        preferences.edit().remove(KEY_USER).apply();
        callback.onSuccess(new RemoveUser.ResponseValues());
    }
}
