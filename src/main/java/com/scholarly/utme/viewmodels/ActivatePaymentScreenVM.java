package com.scholarly.utme.viewmodels;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

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
