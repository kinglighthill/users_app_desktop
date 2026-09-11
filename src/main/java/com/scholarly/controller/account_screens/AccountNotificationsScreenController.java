package com.scholarly.controller.account_screens;

import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.data.model.listItems.NotificationItem;
import com.scholarly.ui.cellFactories.NotificationListCellFactory;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.viewmodels.account_screens.AccountNotificationsScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountNotificationsScreen.fxml")
public class AccountNotificationsScreenController implements FxmlView<AccountNotificationsScreenVM>, Initializable {

    @FXML
    private ListView<NotificationItem> notificationsList;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        NotificationItem notification1 = new NotificationItem(null, "", "");
        NotificationItem notification2 = new NotificationItem(null, "", "");
        NotificationItem notification3 = new NotificationItem(null, "", "");
        NotificationItem notification4 = new NotificationItem(null, "", "");
        NotificationItem notification5 = new NotificationItem(null, "", "");

        ObservableList<NotificationItem> notifications = FXCollections.observableArrayList(notification1, notification2, notification3, notification4, notification5);
        notificationsList.setItems(notifications);
        notificationsList.setCellFactory(new NotificationListCellFactory());

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACCOUNT_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
