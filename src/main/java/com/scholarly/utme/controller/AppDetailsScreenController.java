package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.AppDetailsScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AppDetailsScreen.fxml")
public class AppDetailsScreenController implements FxmlView<AppDetailsScreenVM>, Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ImageView appImage;

    @FXML
    private Label appTitle, appName;


    @InjectViewModel
    private AppDetailsScreenVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        viewModel.processInitialData(getInitialData());

        initializeViews();
        initializeFonts();

        appTitle.textProperty().bind(viewModel.appNameProperty());
        appName.textProperty().bind(viewModel.appNameProperty());

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("appsButton");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        appImage.setImage(new Image(getClass().getResource("/drawable/app_screen_images/" + viewModel.getAppImageUrl()).toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        appName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
    }

    private AppItem getInitialData() {
        AppItem app = (AppItem) ViewSwitcher.retrieveData();
        return app;
    }

}
