package com.david.worldtourist.voice.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.voice.device.boundary.VoiceController;

import java.util.Observer;

import javax.inject.Inject;

public class StopVoiceRecognition extends UseCase<StopVoiceRecognition.RequestValues, StopVoiceRecognition.ResponseValues> {


    private final VoiceController voiceController;

    @Inject
    public StopVoiceRecognition(VoiceController voiceController) {
        this.voiceController = voiceController;
    }

    @Override
    public void execute(StopVoiceRecognition.RequestValues requestValues) {
        voiceController.stopSearch(requestValues);
    }

    @Override
    public void execute(StopVoiceRecognition.RequestValues requestValues,
                        Callback<StopVoiceRecognition.ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Observer observer;

        public RequestValues(Observer observer) {
            this.observer = observer;
        }

        public Observer getObserver() {
            return observer;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
