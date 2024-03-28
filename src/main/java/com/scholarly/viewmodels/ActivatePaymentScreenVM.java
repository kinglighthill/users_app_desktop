package com.scholarly.viewmodels;

import com.google.gson.Gson;
import com.scholarly.network.model.UserData;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

import static com.scholarly.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class ActivatePaymentScreenVM implements ViewModel {

    UserData user;

    public ActivatePaymentScreenVM() {
        String userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        Gson gson = new Gson();
        String userData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
        user = gson.fromJson(userData, UserData.class);
    }

    public UserData getUser() {
        return user;
    }
}
