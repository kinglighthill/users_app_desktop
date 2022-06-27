package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppsGridScreenVM implements ViewModel {

    private ObservableList<AppItem> apps = FXCollections.observableArrayList();

    public void processInitialData(AppsGridScreenController.InitialData data) {
        apps.addAll(data.getApps());
    }

    public ObservableList<AppItem> getApps() {
        return apps;
    }
}
