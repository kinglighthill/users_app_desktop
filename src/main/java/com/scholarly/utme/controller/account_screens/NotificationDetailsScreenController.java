package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.NotificationDetailsScreenVM;
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
import java.nio.Buffer;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/NotificationDetailsScreen.fxml")
public class NotificationDetailsScreenController implements FxmlView<NotificationDetailsScreenVM>, Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Label notificationTitle, notificationText1, notificationText2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();


        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.ACCOUNT_NOTIFICATIONS_SCREEN);
        });
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        notificationTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        notificationText1.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        notificationText2.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }
}
