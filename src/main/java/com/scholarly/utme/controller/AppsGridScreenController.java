package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.cellFactories.AppGridCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.AppsGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AppsGridScreen.fxml")
public class AppsGridScreenController implements FxmlView<AppsGridScreenVM>, Initializable {

    @FXML
    private GridView<AppItem> appsGrid;

    @FXML
    private Button backButton;


    @InjectViewModel
    private AppsGridScreenVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        viewModel.processInitialData(getInitialData());

        appsGrid.setCellFactory(new AppGridCellFactory());
        appsGrid.setItems(viewModel.getApps());

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("appsButton");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        private ObservableList<AppItem> apps;

        public InitialData(ObservableList<AppItem> apps) {
            this.apps = apps;
        }

        public ObservableList<AppItem> getApps() {
            return apps;
        }

        public void setApps(ObservableList<AppItem> apps) {
            this.apps = apps;
        }
    }
}
