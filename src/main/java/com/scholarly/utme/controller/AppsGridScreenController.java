package com.scholarly.utme.controller;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.cellFactories.AppGridCellFactory;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.AppsGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AppsGridScreen.fxml")
public class AppsGridScreenController implements FxmlView<AppsGridScreenVM>, Initializable {
    private static final String TAG = "AppsGridScreenController: ";

    @InjectViewModel
    private AppsGridScreenVM viewModel;

    @FXML
    private GridView<AppItem> appsGrid;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView searchIcon;
    @FXML
    private Button backButton;
    @FXML
    private Label appsTitle;

    private static final List<AppItem> appItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFonts();

        if (appItems.isEmpty()) {
            appItems.addAll(viewModel.getApps());
        }

        appsTitle.textProperty().bind(viewModel.typeProperty());
        appsGrid.setCellFactory(new AppGridCellFactory());


//        System.out.println(TAG + "App Items -> " + appItems);
        appsGrid.setItems(FXCollections.observableArrayList(appItems));

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isBlank()) {
                appsGrid.setItems(FXCollections.observableArrayList(appItems));
                return change;
            }

            List<AppItem> searchedApps = new ArrayList<>();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    for (AppItem appItem : appItems) {
                        if (appItem.keywords.contains(text)) {
                            searchedApps.add(appItem);
                        }
                    }

                    Platform.runLater(() -> {
                        appsGrid.setItems(FXCollections.observableArrayList(searchedApps));
                    });
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            return change;
        });

        searchTextField.setTextFormatter(textFormatter);

        searchTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            searchIcon.setVisible(!newValue);
        }));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.APPS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));
        searchIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/search_icon.png").toString()));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        private String type;

        private ObservableList<AppItem> apps;

        public InitialData(ObservableList<AppItem> apps, String type) {
            this.apps = apps;
            this.type = type;
        }

        public ObservableList<AppItem> getApps() {
            return apps;
        }

        public void setApps(ObservableList<AppItem> apps) {
            this.apps = apps;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
