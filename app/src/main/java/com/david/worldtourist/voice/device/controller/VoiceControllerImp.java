package com.david.worldtourist.voice.device.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;

import com.david.worldtourist.voice.device.boundary.VoiceController;
import com.david.worldtourist.voice.device.components.Speaker;
import com.david.worldtourist.voice.domain.model.VoiceResult;
import com.david.worldtourist.voice.domain.usecase.StartVoiceRecognition;
import com.david.worldtourist.voice.domain.usecase.StopVoiceRecognition;

import java.util.List;
import java.util.Locale;
import java.util.Observable;


@SuppressWarnings("deprecation")
public class VoiceControllerImp extends Observable implements VoiceController, RecognitionListener,
        TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener{

    private static final String TAG = VoiceControllerImp.class.getSimpleName();

    private static final String STARTING_SPEECH = "starting";
    private static final int MAX_RESULTS = 5;

    private Handler handler;
    private Speaker speaker;
    private SpeechRecognizer speechRecognizer;
    private Intent recognitionIntent;

    public VoiceControllerImp(Context context) {
        handler = new Handler(Looper.getMainLooper());
        speaker = new Speaker(context, this);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);

        recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, MAX_RESULTS);
    }

    //////////////////////////DeviceVoice implementation////////////////////////////
    @Override
    public void startSearch(@NonNull StartVoiceRecognition.RequestValues requestValues) {
        addObserver(requestValues.getObserver());
        speaker.speak(STARTING_SPEECH, requestValues.getText());
    }

    @Override
    public void stopSearch(@NonNull StopVoiceRecognition.RequestValues requestValues) {
        speaker.finish();
        speechRecognizer.stopListening();
    }

    @Override
    public void release() {
        speechRecognizer.destroy();
        speaker.shutdown();
        speechRecognizer = null;
        speaker = null;
    }

    //////////////////////Recognition Listener implementation////////////////////////
    @Override
    public void onReadyForSpeech(Bundle params) {
        VoiceResult voiceResult = new VoiceResult();
        voiceResult.setState(VoiceResult.READY_FOR_SPEECH);
        setChanged();
        notifyObservers(voiceResult);
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOsSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        VoiceResult voiceResult = new VoiceResult();

        switch(error) {
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                voiceResult.setState(VoiceResult.ERROR_NETWORK_TIMEOUT);
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                voiceResult.setState(VoiceResult.ERROR_NETWORK);
                break;
            case SpeechRecognizer.ERROR_AUDIO:
                voiceResult.setState(VoiceResult.ERROR_AUDIO);
                break;
            case SpeechRecognizer.ERROR_SERVER:
                voiceResult.setState(VoiceResult.ERROR_SERVER);
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                voiceResult.setState(VoiceResult.ERROR_CLIENT);
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                voiceResult.setState(VoiceResult.ERROR_SPEECH_TIMEOUT);
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                voiceResult.setState(VoiceResult.ERROR_NO_MATCH);
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                voiceResult.setState(VoiceResult.ERROR_RECOGNIZER_BUSY);
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                voiceResult.setState(VoiceResult.ERROR_INSUFFICIENT_PERMISSIONS);
                break;
        }

        setChanged();
        notifyObservers(voiceResult);
        deleteObservers();
    }

    @Override
    public void onResults(Bundle results) {
        if(results != null) {
            VoiceResult voiceResult = new VoiceResult();
            List<String> sentences = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            voiceResult.setState(VoiceResult.RESULTS_RECOGNITION);
            voiceResult.setSentences(sentences);

            setChanged();
            notifyObservers(voiceResult);
            deleteObservers();
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    ///////////////////TextToSpeech.OnInitListener implementation/////////////////////
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            speaker.setOnUtteranceCompletedListener(this);
            speaker.setLanguage(Locale.getDefault());
        }
    }

    ///////////////////OnUtteranceCompletedListener implementation/////////////////////
    @Override
    public void onUtteranceCompleted(final String utteranceId) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                switch (utteranceId) {
                    case STARTING_SPEECH:
                        speechRecognizer.startListening(recognitionIntent);
                        break;
                }
            }
        });
    }
}