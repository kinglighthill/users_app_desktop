package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.cellFactories.AppListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAppsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_apps.fxml")
public class LandingScreenAppsController implements FxmlView<LandingScreenAppsVM>, Initializable {

    @FXML
    private ListView<AppItem> mobileAppsList, desktopAppsList;

    @FXML
    private Button mobileAppsButton, desktopAppsButton;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Label mobileAppsLabel, desktopAppsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        AppItem app1 = new AppItem("JAMB UTME", "jamb_logo.png", "This is the jamb app description");
        AppItem app2 = new AppItem("WAEC", "waec_logo.png", "This is the waec app description");
        AppItem app3 = new AppItem("FUTO", "futo_logo.png", "This is the futo app description");
        AppItem app4 = new AppItem("JAMB UTME", "jamb_logo.png", "This is the jamb app description");
        AppItem app5 = new AppItem("JAMB UTME", "jamb_logo.png", "This is the jamb app description");

        ObservableList<AppItem> apps = FXCollections.observableArrayList(app1, app2, app3, app4, app5);
        mobileAppsList.setItems(apps);
        mobileAppsList.setCellFactory(new AppListCellFactory());

        AppItem dApp1 = new AppItem("Scholarly Kiddies", "scholarly_logo.png", "This is the jamb app description");
        AppItem dApp2 = new AppItem("Scholarly Enterprise", "scholarly_logo.png", "This is the waec app description");
        AppItem dApp3 = new AppItem("Scholarly Kiddies", "scholarly_logo.png", "This is the futo app description");
        AppItem dApp4 = new AppItem("Scholarly Enterprise", "scholarly_logo.png", "This is the jamb app description");
        AppItem dApp5 = new AppItem("Scholarly Kiddies", "scholarly_logo.png", "This is the jamb app description");

        ObservableList<AppItem> dApps = FXCollections.observableArrayList(dApp1, dApp2, dApp3, dApp4, dApp5);
        desktopAppsList.setItems(dApps);
        desktopAppsList.setCellFactory(new AppListCellFactory());


        mobileAppsButton.setOnAction(event -> {
            AppsGridScreenController.InitialData data = new AppsGridScreenController.InitialData(apps, "Mobile Apps");
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.APPS_GRID_SCREEN);
        });

        desktopAppsButton.setOnAction(event -> {
            AppsGridScreenController.InitialData data = new AppsGridScreenController.InitialData(dApps, "Desktop Apps");
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.APPS_GRID_SCREEN);
        });

    }

    private void initializeViews() {
        searchIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/search_icon.png").toString()));

        mobileAppsButton.setBackground(Background.EMPTY);
        desktopAppsButton.setBackground(Background.EMPTY);
        mobileAppsList.setBackground(Background.EMPTY);
        desktopAppsList.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        mobileAppsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        desktopAppsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

    }
}
