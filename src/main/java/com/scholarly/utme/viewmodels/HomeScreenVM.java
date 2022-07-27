package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.HomeScreenController;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

public class HomeScreenVM implements ViewModel {

    private SimpleStringProperty screenProperty = new SimpleStringProperty();

    public void processInitialData(HomeScreenController.InitialData data) {
        screenProperty.set(data.getScreen());
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }

}
