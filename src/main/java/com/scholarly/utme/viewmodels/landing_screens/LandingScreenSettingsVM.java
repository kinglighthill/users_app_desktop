package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.AppPreferences;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.*;

public class LandingScreenSettingsVM implements ViewModel {
    private final Preferences preferences = AppPreferences.getPreferences();

    private SimpleBooleanProperty vibration = new SimpleBooleanProperty();
    private SimpleBooleanProperty sound = new SimpleBooleanProperty();

    Gson gson = new Gson();

    private final String userId;
    private final UserData userData;

    public  LandingScreenSettingsVM() {
        userId = preferences.get(PREF_KEY_USER_ID, "");
        String userDataString = preferences.get(PREF_KEY_USER_DATA, "");

        userData = gson.fromJson(userDataString, UserData.class);

        vibration.set(preferences.getBoolean(PREF_KEY_VIBRATION+userId, false));
        sound.set(preferences.getBoolean(PREF_KEY_SOUND+userId, false));

    }

    public UserData getUser() {
        return userData;
    }
    public SimpleBooleanProperty vibrationProperty() {
        return vibration;
    }

    public SimpleBooleanProperty soundProperty() {
        return sound;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
