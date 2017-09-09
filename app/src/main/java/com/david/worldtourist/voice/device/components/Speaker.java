package com.david.worldtourist.voice.device.components;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;


public class Speaker extends TextToSpeech {

    private HashMap<String, String> speechParams;


    public Speaker(Context context, OnInitListener listener) {
        super(context, listener);
        speechParams = new HashMap<>();
    }

    public void speak(String utteranceId, String text) {
        speechParams.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
        speak(text, TextToSpeech.QUEUE_FLUSH, speechParams);
    }

    public void finish() {
        stop();
    }
}
