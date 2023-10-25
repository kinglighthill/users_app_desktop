package com.scholarly.utme.controller;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.RefreshRequest;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.AppsGridScreenVM;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import okhttp3.*;
import org.controlsfx.control.GridView;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/AppsGridScreen.fxml")
public class AppsGridScreenController implements FxmlView<AppsGridScreenVM>, Initializable {
    private static final String TAG = "AppsGridScreenController: ";

    @InjectViewModel
    private AppsGridScreenVM viewModel;

    @FXML
    private GridView<AppItem> appsGrid;
    @FXML
    private TilePane appsTilePane;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView searchIcon, noAppsImage;
    @FXML
    private Button backButton;
    @FXML
    private Label appsTitle;
    @FXML
    private HBox noApplicationHBox;

    private static final List<AppItem> mobileApps = FXCollections.observableArrayList();
    private static final List<AppItem> desktopApps = FXCollections.observableArrayList();

    private final OkHttpClient httpClient = NetworkService.getHttpClient();

    MainApplication application = new MainApplication();

    interface NetworkCallback {
        void resendRequest();
        void refreshToken();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        String appsType = getInitialData().type;

        initializeViews();
        initializeFonts();

        if (appsType.equalsIgnoreCase("Mobile")) {
            appsTitle.setText("Mobile Apps");
            if (mobileApps.isEmpty()) {
                System.out.println(TAG + "Mobile Apps is empty");
                try {
                    // Check for Internet Connectivity
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    getMobileApps();

                } catch (Exception e) {
                    Platform.runLater(this::showEmptyAppsScreen);
                    System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
                }
            } else {
                System.out.println(TAG + "Mobile Apps is not empty");
                displayApps(mobileApps);
            }
        } else if (appsType.equalsIgnoreCase("Desktop")) {
            appsTitle.setText("Desktop Apps");
            if (desktopApps.isEmpty()) {
                System.out.println(TAG + "Desktop Apps is empty");
                try {
                    // Check for Internet Connectivity
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    getDesktopApps();

                } catch (Exception e) {
                    Platform.runLater(this::showEmptyAppsScreen);
                    System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
                }
            } else {
                System.out.println(TAG + "Desktop Apps is not empty");
                displayApps(desktopApps);
            }
        }

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isBlank()) {
                if (appsType.equalsIgnoreCase("Mobile")) {
                    displayApps(mobileApps);
                } else if (appsType.equalsIgnoreCase("Desktop")) {
                    displayApps(desktopApps);
                }

                return change;
            }

            List<AppItem> searchedApps = new ArrayList<>();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    if (appsType.equalsIgnoreCase("Mobile")) {
                        for (AppItem appItem : mobileApps) {
                            if (appItem.keywords.contains(text)) {
                                searchedApps.add(appItem);
                            }
                        }
                    } else if (appsType.equalsIgnoreCase("Desktop")) {
                        for (AppItem appItem : desktopApps) {
                            if (appItem.keywords.contains(text)) {
                                searchedApps.add(appItem);
                            }
                        }
                    }

                    Platform.runLater(() -> {
                        displayApps(searchedApps);
                    });
                    return null;
                }
            };
            Thread thread = new Thread(task);
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
        noAppsImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/no_apps_image.png").toString()));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private void displayApps(List<AppItem> apps) {
        appsTilePane.getChildren().clear();
        apps.stream().limit(3).forEach(appItem -> {
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
            panel.setOnMouseEntered(e -> panel.setStyle("-fx-border-color: #A2CAA6; -fx-border-radius: 5; -fx-cursor: hand;"));
            panel.setOnMouseExited(e -> panel.setStyle("-fx-border-color: #F1F1F1; -fx-border-radius: 5;"));

            Platform.runLater(() -> appsTilePane.getChildren().add(panel));
        });
    }

    private void getMobileApps() {
        String MOBILE_APPS_END_POINT = "exam-apps";
        String userId = viewModel.getUserId();

        String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + MOBILE_APPS_END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .get().build();
        loadMobileApps(request, new NetworkCallback() {
            @Override
            public void refreshToken() {
                System.out.println(TAG + "Refreshing token...");
                String REFRESH_TOKEN = PreferencesManager.get(PREF_KEY_REFRESH_TOKEN+userId, "");

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

                                PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                System.out.println(TAG + "Refreshed Token New Access Token -> " + PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, ""));

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

                String NEW_ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                Request request = new Request.Builder()
                        .url(BASE_URL + MOBILE_APPS_END_POINT)
                        .header("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                        .get().build();

                Call call = httpClient.newCall(request);

                try(Response response = call.execute()) {
                    try (ResponseBody responseBody = response.body()) {
                        assert responseBody != null;
                        BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                        if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                            mobileApps.addAll(baseResponse.getData().getApps());
                            Platform.runLater(() -> displayApps(mobileApps));
                        } else {
                            Platform.runLater(() -> showEmptyAppsScreen());
                        }

                    } catch (Exception e) {
                        System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                    }

                } catch (Exception e) {
                    Platform.runLater(() -> showEmptyAppsScreen());
                }

            }
        });
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

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                        mobileApps.addAll(baseResponse.getData().getApps());
                        Platform.runLater(() -> displayApps(mobileApps));
                    } else {
                        Platform.runLater(this::showEmptyAppsScreen);
                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                    Platform.runLater(this::showEmptyAppsScreen);

                }
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
            Platform.runLater(this::showEmptyAppsScreen);
        }

    }

    private void getDesktopApps() {
        String DESKTOP_APPS_END_POINT = "exam-apps/desktop";
        String userId = viewModel.getUserId();

        String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + DESKTOP_APPS_END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .get().build();
        loadDesktopApps(request, new NetworkCallback() {
            @Override
            public void refreshToken() {
                System.out.println(TAG + "Refreshing token...");
                String REFRESH_TOKEN = PreferencesManager.get(PREF_KEY_REFRESH_TOKEN+userId, "");

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

                                PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                System.out.println(TAG + "Refreshed Token New Access Token -> " + PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, ""));

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

                String NEW_ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                Request request = new Request.Builder()
                        .url(BASE_URL + DESKTOP_APPS_END_POINT)
                        .header("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                        .get().build();

                Call call = httpClient.newCall(request);

                try(Response response = call.execute()) {
                    try (ResponseBody responseBody = response.body()) {
                        assert responseBody != null;
                        BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                        if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                            desktopApps.addAll(baseResponse.getData().getApps());
                            Platform.runLater(() -> displayApps(desktopApps));
                        } else {
                            Platform.runLater(() -> showEmptyAppsScreen());
                        }

                    } catch (Exception e) {
                        System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                    }

                } catch (Exception e) {
                    Platform.runLater(() -> showEmptyAppsScreen());
                }

            }
        });
    }

    private void loadDesktopApps(Request request, NetworkCallback callback) {
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

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                        desktopApps.addAll(baseResponse.getData().getApps());
                        Platform.runLater(() -> displayApps(desktopApps));
                    } else {
                        Platform.runLater(this::showEmptyAppsScreen);
                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                    Platform.runLater(this::showEmptyAppsScreen);
                }
            }
        } catch (Exception e) {
            System.out.println("Request failed with exception -> " + e.getMessage());
            Platform.runLater(this::showEmptyAppsScreen);
        }

    }

    private void showEmptyAppsScreen() {
        appsTilePane.setVisible(false);
        noApplicationHBox.setVisible(true);
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        private String type;

        public InitialData(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

}
