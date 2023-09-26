package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenAccountVM implements ViewModel {
    private static final String TAG = "LandingScreenAccountVM: ";
    Gson gson = new Gson();

    private final String userId;
    private final UserData userData;
    public LandingScreenAccountVM(){
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");

        userData = gson.fromJson(userDataString, UserData.class);

    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return userData;
    }

}
