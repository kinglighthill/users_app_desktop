package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.User;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

public class LandingScreenAccountVM implements ViewModel {
    private static final String TAG = "LandingScreenAccountVM: ";
    private final Preferences preferences = AppPreferences.getPreferences();

    private final User user;
    public LandingScreenAccountVM(){
        String userData = preferences.get(Constants.PREF_KEY_USER_DATA, "");
        Gson gson = new Gson();
        user = gson.fromJson(userData, User.class);

    }

    public User getUser() {
        return user;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
