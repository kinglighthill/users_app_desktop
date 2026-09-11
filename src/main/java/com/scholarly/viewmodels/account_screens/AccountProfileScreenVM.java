package com.scholarly.viewmodels.account_screens;

import com.google.gson.Gson;
import com.scholarly.network.model.UserData;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

import static com.scholarly.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class AccountProfileScreenVM implements ViewModel {

    Gson gson = new Gson();

    String userId;
    UserData user;
    public AccountProfileScreenVM() {
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        String userData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
        user = gson.fromJson(userData, UserData.class);
    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return user;
    }
}
