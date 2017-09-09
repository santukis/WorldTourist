package com.david.worldtourist.voice.di.modules;


import android.content.Context;

import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.voice.device.boundary.VoiceController;
import com.david.worldtourist.voice.device.controller.VoiceControllerImp;

import dagger.Module;
import dagger.Provides;

@Module
public class VoiceControllerModule {

    @Provides
    @FragmentScope
    public VoiceController voiceController(Context context) {
        return new VoiceControllerImp(context);
    }
}
