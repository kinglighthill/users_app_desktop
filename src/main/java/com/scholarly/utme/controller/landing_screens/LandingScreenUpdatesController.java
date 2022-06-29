package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.data.model.listItems.UpdateItem;
import com.scholarly.utme.ui.cellFactories.UpdateGridCellFactory;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenUpdatesVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_updates.fxml")
public class LandingScreenUpdatesController implements FxmlView<LandingScreenUpdatesVM>, Initializable {

    @FXML
    private GridView<UpdateItem> updatesGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        UpdateItem update1 = new UpdateItem("", "", "", "");
        UpdateItem update2 = new UpdateItem("", "", "", "");
        UpdateItem update3 = new UpdateItem("", "", "", "");
        UpdateItem update4 = new UpdateItem("", "", "", "");
        UpdateItem update5 = new UpdateItem("", "", "", "");
        UpdateItem update6 = new UpdateItem("", "", "", "");

        ObservableList<UpdateItem> updates = FXCollections.observableArrayList(update1, update2, update3, update4, update5, update6);
        updatesGrid.setCellFactory(new UpdateGridCellFactory());
        updatesGrid.setItems(updates);

    }

    private void initializeViews() {

    }

    private void initializeFonts() {

    }
}
