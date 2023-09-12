package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.AppsGridScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.RefreshRequest;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAppsVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import okhttp3.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/landing_screens/landing_screen_apps.fxml")
public class LandingScreenAppsController implements FxmlView<LandingScreenAppsVM>, Initializable {
    private static final String TAG = "LandingScreenAppsController: ";

    @InjectViewModel
    private LandingScreenAppsVM viewModel;

    @FXML
    private TilePane mobileAppsTile, desktopAppsTile;
    @FXML
    private ScrollPane centerScrollPane;
    @FXML
    private VBox applicationsVBox;
    @FXML
    private HBox noApplicationHBox;
    @FXML
    private Button mobileAppsButton, desktopAppsButton;
    @FXML
    private ImageView searchIcon, noAppsImage;
    @FXML
    private Label mobileAppsLabel, desktopAppsLabel, noApplicationsText;
    @FXML
    private TextField searchTextField;


    private OkHttpClient httpClient;
    private Preferences preferences;

    MainApplication application = new MainApplication();

    private static final List<AppItem> mobileApps = FXCollections.observableArrayList();
    private static final List<AppItem> desktopApps = FXCollections.observableArrayList();

    interface NetworkCallback {
        void resendRequest();
        void refreshToken();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences = AppPreferences.getPreferences();
        httpClient = NetworkService.getHttpClient();

        initializeViews();
        initializeFonts();

        long start = System.currentTimeMillis();

        if (mobileApps.isEmpty() || desktopApps.isEmpty()) {
            // Check for internet connectivity
            System.out.println(TAG + "Mobile or Desktop Apps Empty!");
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                String MOBILE_APPS_END_POINT = "exam-apps";
                String userId = viewModel.getUserId();

                String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");
                String REFRESH_TOKEN = preferences.get(PREF_KEY_REFRESH_TOKEN+userId, "");

                Gson gson = new Gson();

                Request request = new Request.Builder()
                        .url(BASE_URL + MOBILE_APPS_END_POINT)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .get().build();

                loadMobileApps(request, new NetworkCallback() {
                    @Override
                    public void refreshToken() {
                        System.out.println(TAG + "Refreshing token...");

                        RefreshRequest refreshTokenRequest = new RefreshRequest(REFRESH_TOKEN);

                        String json = gson.toJson(refreshTokenRequest);

                        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                        Request refreshRequest = new Request.Builder()
                                .url(REFRESH_URL)
                                .post(requestBody)
                                .build();

                        Call call = httpClient.newCall(refreshRequest);

                        try(Response response = call.execute()) {
                            if (response.code() == 200) {
                                try(ResponseBody responseBody = response.body()) {
                                    assert responseBody != null;
                                    BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                    if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                                        System.out.println(TAG + "Refreshed Token for User with Id -> " + refreshResponse.getData().getUserId());

                                        preferences.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                        preferences.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                        System.out.println(TAG + "Refreshed Token New Access Token -> " + preferences.get(PREF_KEY_ACCESS_TOKEN+userId, ""));

                                    } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
                                            alertDialog.show();
                                        });
                                    }
                                } catch (Exception e) {
                                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                                }
                                response.close();
                            }
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                                alertDialog.show();
                            });
                        }
                    }
                    @Override
                    public void resendRequest() {
                        System.out.println(TAG + "Resending request...");

                        String NEW_ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                        Request request = new Request.Builder()
                                .url(BASE_URL + MOBILE_APPS_END_POINT)
                                .header("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                                .get().build();

                        Call call = httpClient.newCall(request);

                        try(Response response = call.execute()) {
                            try (ResponseBody responseBody = response.body()) {
                                assert responseBody != null;
                                BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                                if (baseResponse.getStatus().equalsIgnoreCase("success"))
                                    mobileApps.addAll(baseResponse.getData().getApps());
                                else
                                    Platform.runLater(LandingScreenAppsController.this::showEmptyAppsScreen);

                            } catch (Exception e) {
                                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                            }

                        } catch (Exception e) {
                            Platform.runLater(LandingScreenAppsController.this::showEmptyAppsScreen);
                        }

                    }
                });

                loadDesktopApps();

            } catch (Exception e) {
                Platform.runLater(this::showEmptyAppsScreen);
                System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
            }
        }

        long end = System.currentTimeMillis();

        System.out.println(TAG + "Time taken to load Apps -> " + (end - start)+"ms");

        displayMobileApps(mobileApps);
        displayDesktopApps(desktopApps);


        searchTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            searchIcon.setVisible(!newValue);
        }));


        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isBlank()) {
                displayMobileApps(mobileApps);
                displayDesktopApps(desktopApps);
                return change;
            }

            List<AppItem> searchedMobileApps = new ArrayList<>();
            List<AppItem> searchedDesktopApps = new ArrayList<>();

            for (AppItem appItem : mobileApps) {
                if (appItem.keywords.contains(text)) {
                    searchedMobileApps.add(appItem);
                }
            }

            for (AppItem appItem : desktopApps) {
                if (appItem.keywords.contains(text)) {
                    searchedDesktopApps.add(appItem);
                }
            }

            long mobileStart = System.currentTimeMillis();
            displayMobileApps(searchedMobileApps);
            System.out.println(TAG + "Time taken to load Mobile Apps -> " + (System.currentTimeMillis() - mobileStart) + "ms");
            long desktopStart = System.currentTimeMillis();
            displayDesktopApps(searchedDesktopApps);
            System.out.println(TAG + "Time taken to load Desktop Apps -> " + (System.currentTimeMillis() - desktopStart) + "ms");

            return change;
        });

        searchTextField.setTextFormatter(textFormatter);

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
        searchIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/search_icon.png").toString()));
        noAppsImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/no_apps_image.png").toString()));

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
        noApplicationHBox.setVisible(true);
//        noApplicationsText.setText("No Applications Found" + System.lineSeparator() + "Check your Internet connectivity and try again");
    }

    private void loadMobileApps(Request request, NetworkCallback callback) {
        Gson gson = new Gson();

        Call call = httpClient.newCall(request);

        try (Response response = call.execute()) {
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                callback.refreshToken();
                callback.resendRequest();
            } else {
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
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

    }

    private void loadDesktopApps() {
        String DESKTOP_APPS_END_POINT = "exam-apps/desktop";
        String userId = viewModel.getUserId();
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + DESKTOP_APPS_END_POINT)
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
            System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
        }

    }

    private void displayMobileApps(List<AppItem> mobileApps) {
        mobileAppsTile.getChildren().clear();
        mobileApps.stream().limit(3).forEach(appItem -> {
            Panel panel = new Panel();
            panel.setPrefSize(250, 150);

            ImageView appImage = new ImageView(appItem.getImage());
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

            Platform.runLater(() -> mobileAppsTile.getChildren().add(panel));
        });
    }

    private void displayDesktopApps(List<AppItem> desktopApps) {
        desktopAppsTile.getChildren().clear();
        desktopApps.parallelStream().limit(3).forEach(appItem -> {
            Panel panel = new Panel();
            panel.setPrefSize(250, 150);

            ImageView appImage = new ImageView(appItem.getImage());
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

            Platform.runLater(() -> {
                desktopAppsTile.getChildren().add(panel);
            });
        });
    }
}

