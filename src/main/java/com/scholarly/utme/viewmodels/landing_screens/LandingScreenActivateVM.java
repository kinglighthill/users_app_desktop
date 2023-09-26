package com.scholarly.utme.viewmodels.landing_screens;

import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenActivateVM implements ViewModel {
    private static final String TAG = "LandingScreenActivateVM: ";

    private final String userId;
    private final boolean activated;

    public LandingScreenActivateVM(){
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        System.out.println(TAG + "User id -> " + userId);
        activated = PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false);
        System.out.println(TAG + "Activation state -> " + activated);
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActivated() {
        return activated;
    }
}
