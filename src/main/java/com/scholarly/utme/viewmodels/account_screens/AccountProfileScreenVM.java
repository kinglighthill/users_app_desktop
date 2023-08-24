package com.scholarly.utme.viewmodels.account_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class AccountProfileScreenVM implements ViewModel {

    private Preferences preferences = AppPreferences.getPreferences();
    Gson gson = new Gson();

    String userId;
    UserData user;
    public AccountProfileScreenVM() {
        userId = preferences.get(PREF_KEY_USER_ID, "");
        String userData = preferences.get(PREF_KEY_USER_DATA+userId, "");
        user = gson.fromJson(userData, UserData.class);
    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return user;
    }
}
