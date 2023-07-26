package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.HelloApplication;
import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.BaseResponse;
import com.scholarly.utme.ui.cellFactories.AppListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAppsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import okhttp3.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;
import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;

@FxmlPath("/layouts/landing_screens/landing_screen_apps.fxml")
public class LandingScreenAppsController implements FxmlView<LandingScreenAppsVM>, Initializable {
    private static final String TAG = "LandingScreenAppsController: ";

    @FXML
    private TilePane mobileAppsTile, desktopAppsTile;
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

    HelloApplication application = new HelloApplication();

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
        String ACCESS_TOKEN2 = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE0ZWI4YTNiNjgzN2Y2MTU4ZWViNjA3NmU2YThjNDI4YTVmNjJhN2IiLCJ0eXAiOiJKV1QifQ.eyJ1dWlkIjoiNXV0OVhFdzNraEJYRllqSmdSdGQiLCJlbWFpbF9hZGRyZXNzIjoiYW1ha2FAZ21haWwuY29tIiwiY291bnRyeSI6Im5pZ2VyaWEiLCJpc19hY3RpdmF0aW9uX2FjdGl2ZSI6ZmFsc2UsImRldmljZV9pZCI6ImRldmljZS1pZCIsImFwcF9zbHVnIjoidXRtZSIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9zY2hvbGFybHktdXRtZS1zdGFnaW5nIiwiYXVkIjoic2Nob2xhcmx5LXV0bWUtc3RhZ2luZyIsImF1dGhfdGltZSI6MTY4OTkyMzc3MCwidXNlcl9pZCI6IjV1dDlYRXcza2hCWEZZakpnUnRkIiwic3ViIjoiNXV0OVhFdzNraEJYRllqSmdSdGQiLCJpYXQiOjE2ODk5MjM3NzAsImV4cCI6MTY4OTkyNzM3MCwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6e30sInNpZ25faW5fcHJvdmlkZXIiOiJjdXN0b20ifX0.rH6pF381MPVwDgI7R51ok_2Rcm4IyuJQRALawC63BCRjDZaW2anvrF7gvlVhAwQH_ADl850JRyX_XnQYg5w6bmQY4ol1VTz_slFJrDYTudiZma0QCpTKv8XXVJGTPP_k7-PpxgnEFol2JrKeN_pwwGMdE3bHlSl3Do2jXE4fgIYFc74U-0fEuaZ_3yum_23bxsJ6EnE_jfZHwTLHfqXdWX8xtLKz5h9BjgNdIYdXR6VkI9MEQWFyWO8kI1aR9f31RDbOe4eL5mM57BmaCIPd92hG2cvkJQiz9ej6eEG4v-2NhMbCMITfNIuuognXgVmVLexdLLcgsWpyLOaTIPuJkw";

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

                if (baseResponse.getStatus().equalsIgnoreCase("success")) {

                    mobileApps.addAll(baseResponse.getData().getApps());
                    System.out.println(TAG + "Network Operation Done! MobileApps List size -> " + mobileApps.size());

                } else if (baseResponse.getStatus().equalsIgnoreCase("error")) {
                    Platform.runLater(this::showEmptyAppsScreen);

                }

            } catch (Exception e) {
                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

        /*call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(TAG + "LoadMobileApps Got response with code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {

                        mobileApps.addAll(baseResponse.getData().getApps().stream().limit(3).toList());
                        System.out.println(TAG + "Network Operation Done! MobileApps List size -> " + mobileApps.size());

                    } else if (baseResponse.getStatus().equalsIgnoreCase("error")) {
                        Platform.runLater(() -> {
                            showEmptyAppsScreen();
                        });

                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    showEmptyAppsScreen();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });*/
    }

    private void loadDesktopApps() {
        String END_POINT = "exam-apps/desktop";
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");
        String ACCESS_TOKEN2 = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE0ZWI4YTNiNjgzN2Y2MTU4ZWViNjA3NmU2YThjNDI4YTVmNjJhN2IiLCJ0eXAiOiJKV1QifQ.eyJ1dWlkIjoiNXV0OVhFdzNraEJYRllqSmdSdGQiLCJlbWFpbF9hZGRyZXNzIjoiYW1ha2FAZ21haWwuY29tIiwiY291bnRyeSI6Im5pZ2VyaWEiLCJpc19hY3RpdmF0aW9uX2FjdGl2ZSI6ZmFsc2UsImRldmljZV9pZCI6ImRldmljZS1pZCIsImFwcF9zbHVnIjoidXRtZSIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9zY2hvbGFybHktdXRtZS1zdGFnaW5nIiwiYXVkIjoic2Nob2xhcmx5LXV0bWUtc3RhZ2luZyIsImF1dGhfdGltZSI6MTY4OTkyMzc3MCwidXNlcl9pZCI6IjV1dDlYRXcza2hCWEZZakpnUnRkIiwic3ViIjoiNXV0OVhFdzNraEJYRllqSmdSdGQiLCJpYXQiOjE2ODk5MjM3NzAsImV4cCI6MTY4OTkyNzM3MCwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6e30sInNpZ25faW5fcHJvdmlkZXIiOiJjdXN0b20ifX0.rH6pF381MPVwDgI7R51ok_2Rcm4IyuJQRALawC63BCRjDZaW2anvrF7gvlVhAwQH_ADl850JRyX_XnQYg5w6bmQY4ol1VTz_slFJrDYTudiZma0QCpTKv8XXVJGTPP_k7-PpxgnEFol2JrKeN_pwwGMdE3bHlSl3Do2jXE4fgIYFc74U-0fEuaZ_3yum_23bxsJ6EnE_jfZHwTLHfqXdWX8xtLKz5h9BjgNdIYdXR6VkI9MEQWFyWO8kI1aR9f31RDbOe4eL5mM57BmaCIPd92hG2cvkJQiz9ej6eEG4v-2NhMbCMITfNIuuognXgVmVLexdLLcgsWpyLOaTIPuJkw";

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

                if (baseResponse.getStatus().equalsIgnoreCase("success")) {

                    desktopApps.addAll(baseResponse.getData().getApps());
                    System.out.println(TAG + "Network Operation Done! DesktopApps List size -> " + desktopApps.size());

                } else if (baseResponse.getStatus().equalsIgnoreCase("error")) {
                    Platform.runLater(this::showEmptyAppsScreen);

                }

            } catch (Exception e) {
                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

        /*call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(TAG + "LoadDesktopApps Got response with code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {

                        Platform.runLater(() -> {
                            desktopAppsList.setItems(FXCollections.observableArrayList(baseResponse.getData().getApps()));
                        });

                    } else if (baseResponse.getStatus().equalsIgnoreCase("error")) {
                        Platform.runLater(() -> {
                            showEmptyAppsScreen();
                        });

                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    showEmptyAppsScreen();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });*/
    }

    private void displayMobileApps(List<AppItem> mobileApps) {
        mobileAppsTile.getChildren().clear();
        mobileApps.stream().limit(4).forEach(appItem -> {
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
