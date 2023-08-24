package com.scholarly.utme.viewmodels.landing_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.util.AppPreferences;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenVM implements ViewModel {
    Preferences preferences = AppPreferences.getPreferences();

    private final String userId;

    private final boolean activated;

    private final SimpleStringProperty screenProperty = new SimpleStringProperty();

    public LandingScreenVM() {
        userId = preferences.get(PREF_KEY_USER_ID, "");
        activated = preferences.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false);
    }
    public void processInitialData(LandingScreenController.InitialData data) {
        screenProperty.set(data.getScreenToShow());
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }
}
