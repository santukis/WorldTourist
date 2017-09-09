package com.david.worldtourist.voice.device.boundary;


import android.support.annotation.NonNull;

import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;

public interface VoiceController {

    void startSearch(@NonNull StartVoiceRecognition.RequestValues requestValues);

    void stopSearch(@NonNull StopVoiceRecognition.RequestValues requestValues);

    void release();
}
