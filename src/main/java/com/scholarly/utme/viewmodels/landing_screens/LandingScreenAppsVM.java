package com.scholarly.utme.viewmodels.landing_screens;

import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

public class LandingScreenAppsVM implements ViewModel {
    private static final String TAG = "LandingScreenAppsVM: ";
    Preferences preferences = AppPreferences.getPreferences();

    private final String userId;

    public LandingScreenAppsVM(){
        userId = preferences.get(Constants.PREF_KEY_USER_ID, "");
        System.out.println(TAG + "Got User Id from Preferences -> " + userId);
    }

    public String getUserId() {
        return userId;
    }
}
