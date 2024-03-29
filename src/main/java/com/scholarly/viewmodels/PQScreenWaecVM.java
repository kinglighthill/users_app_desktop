package com.scholarly.viewmodels;

import com.scholarly.controller.PQScreenControllerWaec;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;

import static com.scholarly.util.Constants.PREF_KEY_LAST_SELECTED_PRACTICE;

public class PQScreenWaecVM implements ViewModel, SceneLifecycle {
    public static final String TAG = "HomeScreenVM: ";

    private SimpleObjectProperty<String> screenProperty = new SimpleObjectProperty();
    private SimpleObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty();

    public void processInitialData(PQScreenControllerWaec.InitialData data) {
        String screen = PreferencesManager.get(PREF_KEY_LAST_SELECTED_PRACTICE, "");
        if (data.getPreviousScreen() != null) {
            screenProperty.set(data.getPreviousScreen().getName());
        } else {
            screenProperty.set(screen);
        }
        if (data.getSelectedSubject() != null) {
            selectedSubject.set(data.getSelectedSubject());
        }
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }

    public SimpleObjectProperty<String> selectedScreenProperty() {
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
