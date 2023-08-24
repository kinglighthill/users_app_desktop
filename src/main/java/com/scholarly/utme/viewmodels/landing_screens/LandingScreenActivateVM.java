package com.scholarly.utme.viewmodels.landing_screens;

import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenActivateVM implements ViewModel {
    private static final String TAG = "LandingScreenActivateVM: ";
    Preferences preferences = AppPreferences.getPreferences();

    private final String userId;
    private final boolean activated;

    public LandingScreenActivateVM(){
        userId = preferences.get(PREF_KEY_USER_ID, "");
        activated = preferences.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false);
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActivated() {
        return activated;
    }
}
