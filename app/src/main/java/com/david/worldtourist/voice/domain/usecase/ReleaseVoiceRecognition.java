package com.david.worldtourist.voice.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.voice.device.boundary.VoiceController;

import javax.inject.Inject;

public class ReleaseVoiceRecognition extends UseCase<ReleaseVoiceRecognition.RequestValues, ReleaseVoiceRecognition.ResponseValues> {


    private final VoiceController voiceController;

    @Inject
    public ReleaseVoiceRecognition(VoiceController voiceController) {
        this.voiceController = voiceController;
    }

    @Override
    public void execute(RequestValues requestValues) {
        voiceController.release();
    }

    @Override
    public void execute(RequestValues requestValues, Callback<ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {

    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
