package com.david.worldtourist.voice.domain.usecase;


import com.david.worldtourist.common.domain.UseCase;
import com.david.worldtourist.voice.device.boundary.VoiceController;

import java.util.Observer;

import javax.inject.Inject;

public class StartVoiceRecognition extends UseCase<StartVoiceRecognition.RequestValues, StartVoiceRecognition.ResponseValues> {


    private final VoiceController voiceController;

    @Inject
    public StartVoiceRecognition(VoiceController voiceController) {
        this.voiceController = voiceController;
    }

    @Override
    public void execute(StartVoiceRecognition.RequestValues requestValues) {
        voiceController.startSearch(requestValues);
    }

    @Override
    public void execute(StartVoiceRecognition.RequestValues requestValues,
                        Callback<StartVoiceRecognition.ResponseValues> callback) {

    }

    @Override
    public ResponseValues executeSync() {
        return null;
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String text;
        private Observer observer;

        public RequestValues(String text, Observer observer) {
            this.text = text;
            this.observer = observer;
        }

        public String getText() {
            return text;
        }

        public Observer getObserver() {
            return observer;
        }
    }

    public static class ResponseValues implements UseCase.ResponseValues {

    }
}
