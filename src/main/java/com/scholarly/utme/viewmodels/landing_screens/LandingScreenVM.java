package com.scholarly.utme.viewmodels.landing_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

public class LandingScreenVM implements ViewModel {

    private SimpleStringProperty screenProperty = new SimpleStringProperty();

    public void processInitialData(LandingScreenController.InitialData data) {
        screenProperty.set(data.getScreen());
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }
}
