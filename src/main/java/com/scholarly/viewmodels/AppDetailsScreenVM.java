package com.scholarly.viewmodels;

import com.scholarly.data.model.listItems.AppItem;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

public class AppDetailsScreenVM implements ViewModel {

    private AppItem app;

    private SimpleStringProperty appName = new SimpleStringProperty();

    private SimpleStringProperty imageUrl = new SimpleStringProperty();

    public void processInitialData(AppItem data) {
        app = data;
        appName.set(app.getName());
        imageUrl.set(app.getImageUrl());
    }

    public SimpleStringProperty appNameProperty() {
        return appName;
    }

    public String getAppImageUrl() {
        return imageUrl.get();
    }
}
