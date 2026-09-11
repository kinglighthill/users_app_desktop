package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenAccountVM implements ViewModel {
    private static final String TAG = "LandingScreenAccountVM: ";
    private final Preferences preferences = AppPreferences.getPreferences();
    Gson gson = new Gson();

    private final String userId;
    private final UserData userData;
    public LandingScreenAccountVM(){
        userId = preferences.get(PREF_KEY_USER_ID, "");
        String userDataString = preferences.get(PREF_KEY_USER_DATA+userId, "");

        userData = gson.fromJson(userDataString, UserData.class);

    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return userData;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
