package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
