package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.utils.Screens;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;

public class HomeScreenVM implements ViewModel, SceneLifecycle {
    public static final String TAG = "HomeScreenVM: ";

    private SimpleObjectProperty<Screens> screenProperty = new SimpleObjectProperty();
    private SimpleObjectProperty<PQSubject> selectedSubject = new SimpleObjectProperty();

    public void processInitialData(HomeScreenController.InitialData data) {
        if (data.getScreen() != null) {
            screenProperty.set(data.getScreen());
        }
        if (data.getSelectedSubject() != null) {
            selectedSubject.set(data.getSelectedSubject());
        }
    }

    public Screens getSelectedScreen() {
        return screenProperty.get();
    }

    public SimpleObjectProperty<Screens> selectedScreenProperty() {
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
