package com.unipy.asaris.finalproject;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

//speaking class
public class MyTts {
    private TextToSpeech tts;
    private TextToSpeech.OnInitListener initListener=
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    tts.setLanguage(Locale.forLanguageTag("EN"));
                }
            };
    public MyTts(Context context) {
        tts = new TextToSpeech(context,initListener);
    }
    public void speak(String message){
        tts.speak(message,TextToSpeech.QUEUE_ADD,null,null);
    }
}
