package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.data.model.newDb.Subject;
import com.scholarly.utme.ui.utils.Screen;
import com.scholarly.utme.util.AppPreferences;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.prefs.Preferences;

public class HomeScreenVM implements ViewModel, SceneLifecycle {
    public static final String TAG = "HomeScreenVM: ";

    private SimpleObjectProperty<Screen> screenProperty = new SimpleObjectProperty();
    private SimpleObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty();

    public void processInitialData(HomeScreenController.InitialData data) {
        if (data.getScreen() != null) {
            screenProperty.set(data.getScreen());
        }
        if (data.getSelectedSubject() != null) {
            selectedSubject.set(data.getSelectedSubject());
        }
    }

    public Screen getSelectedScreen() {
        return screenProperty.get();
    }

    public SimpleObjectProperty<Screen> selectedScreenProperty() {
        return screenProperty;
    }

    public PQSubject getSelectedSubject() {
        return selectedSubject.get();
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
