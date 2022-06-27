package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppsGridScreenVM implements ViewModel {

    private ObservableList<AppItem> apps = FXCollections.observableArrayList();

    private SimpleStringProperty type = new SimpleStringProperty();

    public void processInitialData(AppsGridScreenController.InitialData data) {
        apps.addAll(data.getApps());
        type.set(data.getType());
    }

    public ObservableList<AppItem> getApps() {
        return apps;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }
}
