package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.User;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.*;

public class LandingScreenSettingsVM implements ViewModel {

    private final Preferences preferences = AppPreferences.getPreferences();

    private SimpleBooleanProperty vibration = new SimpleBooleanProperty();
    private SimpleBooleanProperty sound = new SimpleBooleanProperty();

    private final User user;

    public  LandingScreenSettingsVM() {
        String userData = preferences.get(PREF_KEY_USER_DATA, "");
        Gson gson = new Gson();
        user = gson.fromJson(userData, User.class);

        vibration.set(preferences.getBoolean(PREF_KEY_VIBRATION+user.getId(), false));
        sound.set(preferences.getBoolean(PREF_KEY_SOUND+user.getId(), false));

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

    public User getUser() {
        return user;
    }
}
