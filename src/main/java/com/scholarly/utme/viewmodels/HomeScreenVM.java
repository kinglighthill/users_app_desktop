package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.util.AppPreferences;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

import java.util.prefs.Preferences;

public class HomeScreenVM implements ViewModel, SceneLifecycle {
    public static final String TAG = "HomeScreenVM: ";

    private SimpleStringProperty screenProperty = new SimpleStringProperty();

    public void processInitialData(HomeScreenController.InitialData data) {
        screenProperty.set(data.getScreen());
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }

    public SimpleStringProperty selectedScreenProperty() {
        return screenProperty;
    }

    @Override
    public void onViewAdded() {
        System.out.println(TAG + "onViewAdded called");
    }

    @Override
    public void onViewRemoved() {
        System.out.println(TAG + "onViewRemoved called");
    }
}
