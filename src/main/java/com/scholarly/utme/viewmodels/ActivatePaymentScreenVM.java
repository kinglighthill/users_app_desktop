package com.scholarly.utme.viewmodels;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.AppPreferences;
import de.saxsys.mvvmfx.ViewModel;

import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class ActivatePaymentScreenVM implements ViewModel {

    Preferences preferences = AppPreferences.getPreferences();

    UserData user;

    public ActivatePaymentScreenVM() {
        String userId = preferences.get(PREF_KEY_USER_ID, "");
        Gson gson = new Gson();
        String userData = preferences.get(PREF_KEY_USER_DATA+userId, "");
        user = gson.fromJson(userData, UserData.class);
    }

    public UserData getUser() {
        return user;
    }
}
