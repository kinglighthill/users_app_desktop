package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.BaseResponse;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAppsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import okhttp3.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/landing_screens/landing_screen_apps.fxml")
public class LandingScreenAppsController implements FxmlView<LandingScreenAppsVM>, Initializable {
    private static final String TAG = "LandingScreenAppsController: ";

    @FXML
    private TilePane mobileAppsTile, desktopAppsTile;
    @FXML
    private ScrollPane centerScrollPane;
    @FXML
    private VBox noApplicationVBox, applicationsVBox;
    @FXML
    private Button mobileAppsButton, desktopAppsButton;
    @FXML
    private ImageView searchIcon;
    @FXML
    private Label mobileAppsLabel, desktopAppsLabel, noApplicationsText;


    private OkHttpClient httpClient;
    private final Preferences preferences = AppPreferences.getPreferences();

    MainApplication application = new MainApplication();

    List<AppItem> mobileApps = new ArrayList<>();
    List<AppItem> desktopApps = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        httpClient = NetworkService.getHttpClient();

        initializeViews();
        initializeFonts();

        // Check for internet connectivity
        try {
            URL url = new URL(BASE_URL);
            URLConnection connection = url.openConnection();
            connection.connect();

            loadMobileApps();
            loadDesktopApps();

        } catch (Exception e) {
            Platform.runLater(this::showEmptyAppsScreen);
            System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
        }

        displayMobileApps(mobileApps);
        displayDesktopApps(desktopApps);


        mobileAppsButton.setOnAction(event -> {
            AppsGridScreenController.InitialData data = new AppsGridScreenController.InitialData(FXCollections.observableArrayList(mobileApps), "Mobile Apps");
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.APPS_GRID_SCREEN);
        });

        desktopAppsButton.setOnAction(event -> {
            AppsGridScreenController.InitialData data = new AppsGridScreenController.InitialData(FXCollections.observableArrayList(desktopApps), "Desktop Apps");
            ViewSwitcher.passData(data);
            ViewSwitcher.showScreen(View.APPS_GRID_SCREEN);
        });

    }

    private void initializeViews() {
//        searchIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/search_icon.png").toString()));

        mobileAppsButton.setBackground(Background.EMPTY);
        desktopAppsButton.setBackground(Background.EMPTY);

        centerScrollPane.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        mobileAppsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        desktopAppsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));

    }

    private void showEmptyAppsScreen() {
        applicationsVBox.setVisible(false);
        noApplicationVBox.setVisible(true);
        noApplicationsText.setText("No Applications Found" + System.lineSeparator() + "Check your Internet connectivity and try again");
    }

    private void loadMobileApps() {
        String END_POINT = "exam-apps";
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .get().build();

        Call call = httpClient.newCall(request);

        try (Response response = call.execute()) {

            try (ResponseBody responseBody = response.body()) {
                assert responseBody != null;
                BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                if (baseResponse.getStatus().equalsIgnoreCase("success"))
                    mobileApps.addAll(baseResponse.getData().getApps());
                else
                    Platform.runLater(this::showEmptyAppsScreen);


            } catch (Exception e) {
                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

    }

    private void loadDesktopApps() {
        String END_POINT = "exam-apps/desktop";
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .get().build();

        Call call = httpClient.newCall(request);

        try (Response response = call.execute()) {

            try (ResponseBody responseBody = response.body()) {
                assert responseBody != null;
                BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                if (baseResponse.getStatus().equalsIgnoreCase("success"))
                    desktopApps.addAll(baseResponse.getData().getApps());
                else
                    Platform.runLater(this::showEmptyAppsScreen);


            } catch (Exception e) {
                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

    }

    private void displayMobileApps(List<AppItem> mobileApps) {
        mobileAppsTile.getChildren().clear();
        mobileApps.stream().limit(3).forEach(appItem -> {
            Panel panel = new Panel();
            panel.setPrefSize(250, 150);
            ImageView appImage = new ImageView(new Image("https://storage.googleapis.com/scholarly-utme-staging.appspot.com/profile_pictures%2F5ut9XEw3khBXFYjJgRtd"));
            appImage.setFitHeight(100);
            appImage.setFitWidth(100);
            HBox hBox = new HBox(appImage);
            hBox.setAlignment(Pos.CENTER);
            panel.setTop(hBox);

            Label appName = new Label(appItem.getName());
            appName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
            appName.setTextAlignment(TextAlignment.CENTER);
            appName.setWrapText(true);
            appName.setPadding(new Insets(5, 0, 0, 0));
            panel.setBottom(appName);

            panel.setStyle("-fx-border-color: #F1F1F1; -fx-border-radius: 5;");
            panel.setPadding(new Insets(10, 0, 10, 10));

            panel.setOnMouseClicked(event -> {
                application.openBrowser(appItem.getDownloadLink());
            });

            mobileAppsTile.getChildren().add(panel);
        });
    }

    private void displayDesktopApps(List<AppItem> desktopApps) {
        desktopAppsTile.getChildren().clear();
        desktopApps.stream().limit(3).forEach(appItem -> {
            Panel panel = new Panel();
            panel.setPrefSize(250, 150);
            ImageView appImage = new ImageView(new Image(appItem.getImageUrl()));
            appImage.setFitHeight(100);
            appImage.setFitWidth(100);
            HBox hBox = new HBox(appImage);
            hBox.setAlignment(Pos.CENTER);
            panel.setTop(hBox);

            Label appName = new Label(appItem.getName());
            appName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
            appName.setTextAlignment(TextAlignment.CENTER);
            appName.setWrapText(true);
            appName.setPadding(new Insets(5, 0, 0, 0));
            panel.setBottom(appName);

            panel.setStyle("-fx-border-color: #F1F1F1; -fx-border-radius: 5;");
            panel.setPadding(new Insets(10, 0, 10, 10));

            panel.setOnMouseClicked(event -> {
                application.openBrowser(appItem.getDownloadLink());
            });

            desktopAppsTile.getChildren().add(panel);
        });
    }

}
