package com.scholarly.viewmodels;

import com.scholarly.util.Constants;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;

public class AppsGridScreenVM implements ViewModel {
    private static final String TAG = "AppsGridScreenVM: ";

    private final String userId;

    public AppsGridScreenVM(){
        userId = PreferencesManager.get(Constants.PREF_KEY_USER_ID, "");
        System.out.println(TAG + "Got User Id from Preferences -> " + userId);
    }

    public String getUserId() {
        return userId;
    }
}
