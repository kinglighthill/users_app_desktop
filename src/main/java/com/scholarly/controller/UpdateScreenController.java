package com.scholarly.controller;

import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.viewmodels.UpdateScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/UpdateScreen.fxml")
public class UpdateScreenController implements FxmlView<UpdateScreenVM>, Initializable {

    @FXML
    private ImageView updateImage;

    @FXML
    private Button backButton;

    @FXML
    private Label updateTitle, updateDescription, updateDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACCOUNT_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        updateImage.setImage(new Image(getClass().getResource("/drawable/update_item_image2.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        updateTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 22));
        updateDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        updateDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));
    }
}
