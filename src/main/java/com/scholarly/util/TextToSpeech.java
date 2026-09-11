package com.scholarly.util;

import com.sun.speech.freetts.VoiceManager;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

public class TextToSpeech {
    static CompositeDisposable disposables = new CompositeDisposable();

    public static void play(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        // Using RxJava's Disposable to play the speech on a background thread to avoid blocking the UI
        // 'doOnNext' runs its block of code on the specified background thread the disposable is subscribed on
        disposables.add(
                Observable.just(VoiceManager.getInstance().getVoice("kevin16"))
                        .subscribeOn(Schedulers.io())
                        .doOnNext(voice -> {
                            voice.allocate();
                            voice.speak(text);
                        })
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe()

        );
    }

    public static void dispose() {
        disposables.dispose();
    }
}
