package com.scholarly.viewmodels.landing_screens;

import com.scholarly.util.Constants;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

public class LandingScreenAppsVM implements ViewModel {
    private static final String TAG = "LandingScreenAppsVM: ";

    private final String userId;

    public LandingScreenAppsVM(){
        userId = PreferencesManager.get(Constants.PREF_KEY_USER_ID, "");
        System.out.println(TAG + "Got User Id from Preferences -> " + userId);
    }

    public String getUserId() {
        return userId;
    }
}
