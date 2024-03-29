package com.scholarly.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.network.model.UserData;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;

import static com.scholarly.util.Constants.*;

public class LandingScreenSettingsVM implements ViewModel {

    private SimpleBooleanProperty vibration = new SimpleBooleanProperty();
    private SimpleBooleanProperty sound = new SimpleBooleanProperty();

    Gson gson = new Gson();

    private final UserData userData;

    public  LandingScreenSettingsVM() {
        String userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");

        userData = gson.fromJson(userDataString, UserData.class);

        sound.set(PreferencesManager.getBoolean(PREF_KEY_SOUND+userId, false));
        vibration.set(PreferencesManager.getBoolean(PREF_KEY_VIBRATION+userId, false));

    }

    public UserData getUser() {
        return userData;
    }
    public SimpleBooleanProperty soundProperty() {
        return sound;
    }
    public SimpleBooleanProperty vibrationProperty() {
        return vibration;
    }

}
