package com.david.worldtourist.voice.domain.model;


import java.util.ArrayList;
import java.util.List;

public class VoiceResult {

    private final int NO_STATE = 0;
    public static final int RESULTS_RECOGNITION = 1;
    public static final int ERROR_NETWORK_TIMEOUT = 2;
    public static final int ERROR_NETWORK = 3;
    public static final int ERROR_AUDIO = 4;
    public static final int ERROR_SERVER = 5;
    public static final int ERROR_CLIENT = 6;
    public static final int ERROR_SPEECH_TIMEOUT = 7;
    public static final int ERROR_NO_MATCH = 8;
    public static final int ERROR_RECOGNIZER_BUSY = 9;
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 10;
    public static final int READY_FOR_SPEECH = 11;

    private int state = NO_STATE;
    private List<String> sentences = new ArrayList<>();

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<String> getSentences() {
        return sentences;
    }

    public void setSentences(List<String> sentences) {
        this.sentences = sentences;
    }
}
